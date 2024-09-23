package com.app.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartItemId;
	@ToString.Exclude
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;
     @ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private String size;

	private Integer quantity;

	private double price;

	private int discountedPrice;

	private Long userId;
}
