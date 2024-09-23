package com.app.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
     
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
     @ToString.Exclude
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
	@Column(name="cart_items")
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name="total_price")
	private double totalPrice=0.0;
    
    @Column(name="total_item")
	private Integer totalItem;
	private int totalDiscountedPrice;
	
	private int discount;
}
