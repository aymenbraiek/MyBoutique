package com.biat.MyBoutique.domain;

import com.biat.MyBoutique.domain.enumeration.CartStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
1. This Lombok annotation generates getters for all fields, a useful toString method, and
hashCode and equals implementations that check all non-transient fields. Will also generate
setters for all non-final fields, as well as a constructor.
2. This Lombok annotation generates a no-args constructor.
3. This Lombok annotation generates an all-args constructor. An all-args constructor requires
one argument for every field in the class.
4. Column definition: name, length and a definition of a Nullability Constraint.
5. This is an @Entity and its corresponding @Table will be named cart.
6. Validation annotations used to control the integrity of the data.

*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="cart")
public class Cart extends AbstractEntity{
    private static final long serialVersionUID = 1L;
    @OneToOne
    @JoinColumn(unique = true)
    private Order order;

    @ManyToOne
    private Customer customer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CartStatus status;
    public Cart(Customer customer) {
        this.customer = customer;
        this.status = CartStatus.NEW;
    }

}
