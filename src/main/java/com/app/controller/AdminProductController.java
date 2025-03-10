package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dtos.APIResponse;
import com.app.dtos.CreateProductRequest;
import com.app.exceptions.APIException;
import com.app.pojo.Product;
import com.app.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/")
	public ResponseEntity<Product>createProduct(@RequestBody CreateProductRequest req){
		
		Product product=productService.createProduct(req);
		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{productId}/delete")
	public ResponseEntity<APIResponse>deleteProduct(@PathVariable Long productId) throws APIException {
		
		productService.deleteProduct(productId);
		APIResponse res=new APIResponse();
		res.setMessage("product deleted successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
//	
	@GetMapping("/all")
	public ResponseEntity<List<Product>>findAllProduct(){
		
		List<Product>products=productService.findAllProducts();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
//	@PutMapping("/{productId}/update")
//	public ResponseEntity<Product>updateProduct(@RequestBody Product req,@PathVariable Long productId) throws APIException
//	{
//		Product product=productService.updateProduct(productId, req);
//		return new ResponseEntity<Product>(product,HttpStatus.CREATED);
//	}
	
	@PostMapping("/creates")
	public ResponseEntity<APIResponse>createMultipleProduct(@RequestBody CreateProductRequest[] req){
		
		for(CreateProductRequest product:req)
		{
			productService.createProduct(product);
		}
		APIResponse res=new APIResponse();
		res.setMessage("product created successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}

}
