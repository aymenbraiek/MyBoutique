package com.biat.MyBoutique.repository;

import com.biat.MyBoutique.domain.Cart;
import com.biat.MyBoutique.domain.enumeration.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
1. Indicates that an annotated class is a “Repository”, originally defined by Domain-Driven
Design (Evans, 2003) as “a mechanism for encapsulating storage, retrieval, and search
behavior which emulates a collection of objects”.
2. JPA specific extension of Repository. This will enable Spring Data to find this interface and
automatically create an implementation for it.
3. These methods are implementing queries automatically using Spring Data Query Builder
Mecanism.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findByStatus(CartStatus status);
    List<Cart> findByStatusAndCustomerId(CartStatus status, Long customerId);
}
