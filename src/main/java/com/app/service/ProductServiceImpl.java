package com.app.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.app.dtos.CreateProductRequest;
import com.app.dtos.ProductDTO;
import com.app.dtos.ProductResponse;
import com.app.exceptions.APIException;
import com.app.pojo.Category;
import com.app.pojo.Product;
import com.app.pojo.Size;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductRepository;

import jakarta.transaction.Transactional;
@Service

public class ProductServiceImpl  implements ProductService{
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	 @Autowired
	    private ModelMapper modelMapper;

	@Override
	public Product createProduct(CreateProductRequest req) {
	    Category topLevel=categoryRepo.findByCategoryName(req.getTopLevelCategory()) ;
	    
	    if(topLevel==null)
	    {
	    	Category topLevelCategory=new Category();
	    	topLevelCategory.setCategoryName(req.getTopLevelCategory());
	    	topLevelCategory.setLevel(1);
	    	
	    	topLevel=categoryRepo.save(topLevelCategory);
	    }
	    
Category secondLevel=categoryRepo.findByNameAndParent(req.getSecondLevelCategory(),topLevel.getCategoryName()) ;
	    
	    if(secondLevel==null)
	    {
	    	Category secondLevelCategory=new Category();
	    	secondLevelCategory.setCategoryName(req.getSecondLevelCategory());
	    	secondLevelCategory.setParentCategory(topLevel);
	    	secondLevelCategory.setLevel(2);
	    	
	    	secondLevel=categoryRepo.save(secondLevelCategory);
	    }
	    
Category thirdLevel=categoryRepo.findByNameAndParent(req.getThirdLevelCategory(),secondLevel.getCategoryName()) ;
	    
	    if(thirdLevel==null)
	    {
	    	Category thirdLevelCategory=new Category();
	    	thirdLevelCategory.setCategoryName(req.getThirdLevelCategory());
	    	thirdLevelCategory.setParentCategory(secondLevel);
	    	thirdLevelCategory.setLevel(3);
	    	
	    	thirdLevel=categoryRepo.save(thirdLevelCategory);
	    }
	    
	    Product product=new Product();
	    product.setTitle(req.getTitle());
	    product.setColor(req.getColor());
	    product.setDescription(req.getDescription());
	    product.setDiscountedPrice(req.getDiscountedPrice());
	    product.setDiscountPercent(req.getDiscountPercent());
	    product.setImageUrl(req.getImageUrl());
	    product.setBrand(req.getBrand());
	    product.setPrice(req.getPrice());
	    product.setSizes(req.getSize());;
	    product.setQuantity(req.getQuantity());
	    product.setCategory(thirdLevel);
	    product.setCreatedAt(LocalDateTime.now());
	    
	    Product savedProduct=productRepo.save(product);
	    
	    return savedProduct;
	    
	    
		
	}

	@Override
//	public String deleteProduct(Long productId) throws APIException {
//		//Product product=findProductById(productId);
//		  Optional<Product> opt = productRepo.findById(productId);
//		    if (opt.isPresent()) {
//		        Product product = opt.get();
//		
//		product.getSizes().clear();
//		productRepo.delete(product);
//		return "Product deleted Successfully";
//		    }
//	}
	public String deleteProduct(Long productId) throws APIException {
	    // Find the product by its ID
	    Optional<Product> opt = productRepo.findById(productId);
	    
	    // Check if the product exists
	    if (opt.isPresent()) {
	        Product product = opt.get();
	        
//	        // Ensure that the sizes collection is initialized
//	        if (product.getSizes() != null) {
//	            product.getSizes().clear();  // Clear the sizes if it is not null
//	        }
//	        
//	        // Delete the product from the repository
//	        productRepo.delete(product);
//	        
	        System.out.println("Before clearing sizes: " + product.getSizes());
	        product.getSizes().clear();
	        System.out.println("After clearing sizes: " + product.getSizes());

	        
	        // Save the product to reflect the changes
	       productRepo.save(product);
	        
	        // Now delete the product
	        productRepo.delete(product);
	        return "Product deleted successfully";
	    } else {
	        // Handle the case where the product does not exist
	        throw new APIException("Product with ID " + productId + " not found");
	    }
	}


//	@Override
//	public Product updateProduct(Long productId, Product req) throws APIException {
//		Product product=findProductById(productId);
//		
//		if(req.getQuantity()!=0)
//		{
//			product.setQuantity(req.getQuantity());
//		}
//		return  productRepo.save(product);
//	}
//	public Product findProductById(Long id) throws APIException {
//	Optional<Product> opt=productRepo.findById(id);
//	if(opt.isPresent())
//	{
//		return opt.get();
//	}
//	throw new APIException("Product not found with id "+id);
//}
@Transactional
	@Override

	public ProductDTO findProductById(Long id) {
	    Optional<Product> opt = productRepo.findById(id);
	    if (opt.isPresent()) {
	        Product product = opt.get();
	        // Convert Product to ProductDTO
	        ProductDTO productDTO = new ProductDTO();
	        productDTO.setProductId(product.getProductId());
	        productDTO.setTitle(product.getTitle());
	        productDTO.setDiscountedPrice(product.getDiscountedPrice());
	        productDTO.setDiscountPercent(product.getDiscountPercent());
	        productDTO.setDescription(product.getDescription());
	        productDTO.setQuantity(product.getQuantity());
	        productDTO.setPrice(product.getPrice());
	        productDTO.setBrand(product.getBrand());
	        productDTO.setColor(product.getColor());
	        productDTO.setImageUrl(product.getImageUrl());
	        productDTO.setNumRatings(product.getNumRatings());
	        productDTO.setSizes(product.getSizes());
	        productDTO.setCategoryId(id);  // Simplified category
	        
	        return productDTO;
	    }
	    throw new APIException("Product not found with id " + id);
	}
	 @Override
	    public List<Product> findAllProducts() {
	        return productRepo.findAll();
	    }

	@Override
	public List<Product> findProductByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}
	@Transactional
	@Override
	public ProductResponse getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		Pageable pageble=PageRequest.of(pageNumber, pageSize);
		category = category.trim();

		
		List<Product>products=productRepo.filterProducts(category,minPrice,maxPrice,minDiscount );
		System.out.println("products"+products);
		if ("price_low".equals(sort)) {
	        products.sort(Comparator.comparing(Product::getDiscountedPrice));
	    } else if ("price_high".equals(sort)) {
	        products.sort(Comparator.comparing(Product::getDiscountedPrice).reversed());
	    }
		System.out.println("products "+products);
		if(!colors.isEmpty())
		{
			products=products.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
		}
		System.out.println("products "+products);
		if(stock!=null)
		{
			if(stock.equals("in_stock"))
			{
				products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
			}
			else if(stock.equals("out_of_stock"))
			{
				products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
			}
			
		}
		System.out.println("products "+products);
		
		int startIndex=(int) pageble.getOffset();
		int endIndex=Math.min(startIndex+pageble.getPageSize(),products.size());
		
		List<Product> pageContent=products.subList(startIndex, endIndex);
		
		Page<Product>filteredProducts=new PageImpl<>(pageContent,pageble,products.size());
		
		 List<Product> products1 = filteredProducts.getContent();

	        List<ProductDTO> productDTOS = products1.stream()
	                .map(product -> modelMapper.map(product, ProductDTO.class))
	                .toList();

	        ProductResponse productResponse = new ProductResponse();
	        productResponse.setContent(productDTOS);
	        productResponse.setPageNumber(filteredProducts.getNumber());
	        productResponse.setPageSize(filteredProducts.getSize());
	        productResponse.setTotalElements(filteredProducts.getTotalElements());
	        productResponse.setTotalPages(filteredProducts.getTotalPages());
	        productResponse.setLastPage(filteredProducts.isLast());
	        return productResponse;
//		System.out.println(" filtered products "+filteredProducts);
		
//		return filteredProducts;
		
	}
			
			
			
	
	
	

}
