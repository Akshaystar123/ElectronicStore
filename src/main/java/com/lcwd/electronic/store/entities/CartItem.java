package com.lcwd.electronic.store.entities;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @OneToOne
    private Product product;
    private int quantity;
    private int totalPrice;

    //mapping cart
}
