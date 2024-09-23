package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dtos.ProductDTO;
import com.app.dtos.ProductResponse;

import com.app.exceptions.APIException;
import com.app.pojo.Product;
import com.app.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<ProductResponse> findProductByCategoryHandler(@RequestParam String category,
			@RequestParam List<String>color,@RequestParam List<String>size,@RequestParam Integer minPrice,
			@RequestParam Integer maxPrice,@RequestParam Integer minDiscount,@RequestParam String sort,
			@RequestParam String stock,@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
		
		ProductResponse productResponse=productService.getAllProducts(category, color,size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
		
		System.out.println(" Products page size : "+productResponse);
		System.out.println("Total products : "+productResponse);
		
		System.out.println("complete products");
		
		return new ResponseEntity<>(productResponse,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/products/id/{productId}")
	public ResponseEntity<ProductDTO> findProductByIdHandler(@PathVariable Long productId) throws APIException{
		
		ProductDTO product=productService.findProductById(productId);
		System.out.println("product "+product);
		
		return new ResponseEntity<ProductDTO>(product,HttpStatus.ACCEPTED);
}
}
	