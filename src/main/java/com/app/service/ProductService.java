package com.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dtos.CreateProductRequest;
import com.app.dtos.ProductDTO;
import com.app.dtos.ProductResponse;
import com.app.exceptions.APIException;
import com.app.pojo.Product;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId) throws APIException;
//	
//	public Product updateProduct(Long productId,Product req) throws APIException;
	
	public ProductDTO findProductById(Long id) throws APIException;
	
	public List<Product> findProductByCategory(String category);
	
	public ProductResponse getAllProducts(String category,List<String>colors,List<String>sizes,Integer minPrice,Integer maxPrice,
			Integer munDiscount,String sort,String stock,Integer pageNumber,Integer pageSize);
	
	List<Product> findAllProducts();
//	
	

}
