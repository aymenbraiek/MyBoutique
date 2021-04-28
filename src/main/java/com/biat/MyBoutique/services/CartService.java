package com.biat.MyBoutique.services;

import com.biat.MyBoutique.web.dto.CartDto;
import com.biat.MyBoutique.domain.Cart;
import com.biat.MyBoutique.domain.enumeration.CartStatus;
import com.biat.MyBoutique.domain.Customer;
import com.biat.MyBoutique.domain.Order;
import com.biat.MyBoutique.repository.CartRepository;
import com.biat.MyBoutique.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
1. Lombok annotation used to generate a constructor with required arguments. Required
arguments are final fields and fields with constraints such as @NonNull.
2. Lombok annotation used to generate a logger in the class. When used, you then have a
static final log field, initialized to the name of your class, which you can then use to
write log statements.
3. Indicates that an annotated class is a Service, originally defined by Domain-Driven
Design (Evans, 2003) as “an operation offered as an interface that stands alone in the model,
with no encapsulated state.”
4. Spring @Transactional annotation is used for defining a transaction boundary.
5. We injected the Repository in the Service using a constructor injection and not using
the field or setter injection. The constructor used for the injection is generated using
Lombok.
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public List<CartDto> findAll() {
        log.debug("Request to get all Carts");
        return this.cartRepository.findAll()
                .stream()
                .map(CartService::mapToDto)
                .collect(Collectors.toList());
    }
    public List<CartDto> findAllActiveCarts() {
        return this.cartRepository.findByStatus(CartStatus.NEW)
                .stream()
                .map(CartService::mapToDto)
                .collect(Collectors.toList());
    }
    public CartDto create(Long customerId) {
        if (this.getActiveCart(customerId) == null) {
            Customer customer = this.customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalStateException("The Customer does not exist!"));
            Cart cart = new Cart(
                    null,
                    customer,
                    CartStatus.NEW
            );
            Order order = this.orderService.create(cart);
            cart.setOrder(order);
            return mapToDto(this.cartRepository.save(cart));
        } else {
            throw new IllegalStateException("There is already an active cart");
        }
    }
    @Transactional(readOnly = true)
    public CartDto findById(Long id) {
        log.debug("Request to get Cart : {}", id);
        return this.cartRepository.findById(id).map(CartService::mapToDto).orElse(null);
    }
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        Cart cart = this.cartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find cart with id " + id));
        cart.setStatus(CartStatus.CANCELED);
        this.cartRepository.save(cart);
    }
    public CartDto getActiveCart(Long customerId) {
        List<Cart> carts = this.cartRepository
                .findByStatusAndCustomerId(CartStatus.NEW, customerId);
        if (carts != null) {
            if (carts.size() == 1) {
                return mapToDto(carts.get(0));
            }
            if (carts.size() > 1) {
                throw new IllegalStateException("Many active carts detected !!!");
            }
        }
        return null;
    }
    public static CartDto mapToDto(Cart cart) {
        if (cart != null) {
            return new CartDto(
                    cart.getId(),
                    cart.getOrder().getId(),
                    CustomerService.mapToDto(cart.getCustomer()),
                    cart.getStatus().name()
            );
        }
        return null;
    }
}
