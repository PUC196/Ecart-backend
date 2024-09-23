package com.app.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.app.pojo.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")

public class Product {

   

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @jakarta.validation.constraints.Size(min = 3, message = "Product name must contain atleast 3 characters")
    //private String productName;
   // private String image;
    private String title;
    
	@Column(name="discount_price")
	private int discountedPrice;
	@Column(name="discount_percent")
	private int discountPercent;
	

    @NotBlank
    @jakarta.validation.constraints.Size(min = 6, message = "Product description must contain atleast 6 characters")
    private String description;
    private Integer quantity;
    private double price;
    //private double discount;
    //private double specialPrice;
    private String brand;
	private String color;
	private LocalDateTime createdAt;
	@Column(name="image_url")
	private String imageUrl;
	@Column(name="num_ratings")
	private int numRatings;
	@Embedded
	@ElementCollection(fetch = FetchType.EAGER)
	//@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Column(name="sizes")
	private Set<Size> sizes=new HashSet<>();
     @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

//    @ManyToOne
//    @JoinColumn(name = "seller_id")
//    private User user;
//     @ToString.Exclude
//    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
//    private List<CartItem> products = new ArrayList<>();

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", title=" + title + ", discountedPrice=" + discountedPrice
				+ ", discountPercent=" + discountPercent + ", description=" + description + ", quantity=" + quantity
				+ ", price=" + price + ", brand=" + brand + ", color=" + color + ", createdAt=" + createdAt
				+ ", imageUrl=" + imageUrl + ", numRatings=" + numRatings + ", sizes=" + sizes + "]";
	}
    
}
