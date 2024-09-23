package com.app.dtos;

import java.time.LocalDateTime;
import java.util.Set;

import com.app.pojo.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long productId;
    private String title;
    private int discountedPrice;
    private int discountPercent;
    private String description;
    private Integer quantity;
    private double price;
    private String brand;
    private String color;
    private LocalDateTime createdAt;
    private String imageUrl;
    private int numRatings;
    private Set<Size> sizes;
    private Long categoryId;  // 
}
