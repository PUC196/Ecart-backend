package com.app.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.app.pojo.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
      
//    @NotBlank
    @NotNull
    @Size(min = 5, message = "Category name must contain atleast 5 characters")
    private String categoryName;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="parent_category_id")
	private Category parentCategory;
    
    private int level;
   @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Product> products;
}
