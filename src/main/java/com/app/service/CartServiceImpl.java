package com.app.service;

import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.pojo.Cart;
import com.app.pojo.CartItem;
import com.app.pojo.Product;
import com.app.pojo.User;
import com.app.dtos.AddItemRequest;
import com.app.dtos.CartDTO;
import com.app.dtos.ProductDTO;
import com.app.exceptions.CartItemException;
import com.app.exceptions.ProductException;
import com.app.exceptions.UserException;
import com.app.repository.CartItemRepository;
import com.app.repository.CartRepository;
import com.app.repository.ProductRepository;
import com.app.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	CartItemService cartItemService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public String addProductToCart(Long userId, AddItemRequest req) {
		Cart cart = createCart();

		Product product = productRepository.findById(req.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", req.getProductId()));

		System.out.println("product " + product);

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), req.getProductId());

//        if (cartItem != null) {
//           // throw new APIException("Product " + product.getTitle() + " already exists in the cart");
//        	cartItem.setQuantity(1);
//        }

//        if (product.getQuantity() == 0) {
//            throw new APIException(product.getTitle() + " is not available");
//        }
//
//        if (product.getQuantity() < req.getQuantity()) {
//            throw new APIException("Please, make an order of the " + product.getTitle()
//                    + " less than or equal to the quantity " + product.getQuantity() + ".");
//        }
		if (cartItem == null) {
			CartItem newCartItem = new CartItem();

			newCartItem.setProduct(product);
			newCartItem.setCart(cart);
			newCartItem.setQuantity(1);
			newCartItem.setSize(req.getSize());
			newCartItem.setPrice(product.getPrice());

			newCartItem.setDiscountedPrice(product.getDiscountedPrice());
			System.out.println(newCartItem);

			cartItemRepository.save(newCartItem);

			product.setQuantity(product.getQuantity());

			cart.setTotalPrice(cart.getTotalPrice() + (product.getDiscountedPrice() * req.getQuantity()));

			cartRepository.save(cart);

			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<CartItem> cartItems = cart.getCartItems();

			Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
				ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
				map.setQuantity(item.getQuantity());
				return map;
			});

			cartDTO.setProducts(productStream.toList());
			System.out.println(cartDTO);
		}

		return "item added to cart";
	}
    @Override
	public String addCartItem(Long userId, AddItemRequest req) throws APIException {
    	Cart cart = createCart();
		//Cart cart=cartRepo.findByUserId(userId);
		//Product product=productService.findProductById(req.getProductId());
    	Product product = productRepository.findById(req.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", req.getProductId()));
		CartItem isPresent=cartItemService.isCartItemExist(cart, product,req.getSize(), userId);
		if(isPresent==null)
		{
			CartItem cartItem=new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			
			int price=req.getQuantity()*product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			
			CartItem createdCartItem=cartItemService.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
			
		}
		
		return " Item Add To Cart";
		
	}

//    @Override
//    public List<CartDTO> getAllCarts() {
//        List<Cart> carts = cartRepository.findAll();
//
//        if (carts.size() == 0) {
//            throw new APIException("No cart exists");
//        }
//
//        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
//            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//            List<ProductDTO> products = cart.getCartItems().stream().map(cartItem -> {
//                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
//                productDTO.setQuantity(cartItem.getQuantity()); // Set the quantity from CartItem
//                return productDTO;
//            }).collect(Collectors.toList());
//
//
//            cartDTO.setProducts(products);
//
//            return cartDTO;
//
//        }).collect(Collectors.toList());
//
//        return cartDTOs;
//    }

	@Override
	public Cart getCart(String emailId, Long cartId) {
		Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;

		for (CartItem cartItem : cart.getCartItems()) {
			totalPrice += cartItem.getPrice();
			totalDiscountedPrice += cartItem.getDiscountedPrice();
			totalItem += cartItem.getQuantity();

		}

		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscount(totalPrice - totalDiscountedPrice);
		System.out.println(cart);

		return cartRepository.save(cart);
	}

	@Transactional
	@Override
	 public String updateProductQuantityInCart(Long cartItemId, Integer quantity) {
        // Retrieve the cart item by its ID
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            Cart cart = cartItem.getCart(); // Get the associated cart

            // Update the quantity in the cart item
            cartItem.setQuantity(quantity);

            // Update the price and discounted price of the cart item based on the new quantity
            double updatedPrice = cartItem.getProduct().getPrice() * quantity;
            cartItem.setPrice(updatedPrice);

            int updatedDiscountedPrice = cartItem.getProduct().getDiscountedPrice() * quantity;
            cartItem.setDiscountedPrice(updatedDiscountedPrice);

            // Save the updated cart item
            cartItemRepository.save(cartItem);

            // Recalculate the total price of the cart and save it
            updateCartTotals(cart);

            // Return the updated cart
            return "updated successfully";
        } else {
            throw new IllegalArgumentException("Cart item not found for id: " + cartItemId);
        }
    }

    // Helper method to recalculate the total price and total items in the cart
    private void updateCartTotals(Cart cart) {
        double totalPrice = 0.0;
        int totalDiscountedPrice = 0;
        int totalItems = 0;

        for (CartItem item : cart.getCartItems()) {
            totalPrice += item.getPrice();
            totalDiscountedPrice += item.getDiscountedPrice();
            totalItems += item.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItems);
    }
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException("Cart item not found."));

        // Remove the association between CartItem and Cart
        if (cartItem.getCart() != null) {
            Cart cart = cartItem.getCart();
            cart.getCartItems().remove(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice());
            cart.setTotalItem(cart.getTotalItem() - 1);
            cartRepository.save(cart); // Update the cart
        }

        // Remove the association between CartItem and Product
        if (cartItem.getProduct() != null) {
            Product product = cartItem.getProduct();
            cartItem.setProduct(null); // Detach the product from the cartItem
        }

        // Now delete the CartItem
        cartItemRepository.delete(cartItem);
    }
    
//    @Override
//	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
//		CartItem cartItem=findCartItemById(cartItemId);
//		
//		User user=userService.findUserById(cartItem.getUserId());
//		
//		User reqUser=userService.findUserById(userId);
//		
//		if(user.getId().equals(reqUser.getId()))
//		{
//			cartItemRepo.deleteById(cartItemId);
//			
//		}
//		else {
//			throw new UserException("you can't remove another users item");
//		}
//		
//		
//	}
//    
//	public String updateProductQuantityInCart(Long cartItemId, Integer quantity) {
//		String emailId = authUtil.loggedInEmail();
//		Cart userCart = cartRepository.findCartByEmail(emailId);
//		Long cartId = userCart.getCartId();
//
//		Cart cart = cartRepository.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//		// Retrieve the cart item by its ID
//		Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
//
//		if (optionalCartItem.isPresent()) {
//			CartItem cartItem = optionalCartItem.get();
//
//			// Update the quantity
//			cartItem.setQuantity(quantity);
//
//			// Recalculate the price based on quantity and product price
//			double updatedPrice = cartItem.getProduct().getPrice() * quantity;
//			cartItem.setPrice(updatedPrice);
//
//			// Update discounted price if applicable
//			int updatedDiscountedPrice = cartItem.getProduct().getDiscountedPrice() * quantity;
//			cartItem.setDiscountedPrice(updatedDiscountedPrice);
//
//			// Save and return the updated cart item
//			return cartItemRepository.save(cartItem);
////    	        } else {
////    	            throw new IllegalArgumentException("Cart item not found for id: " + cartItemId);
////    	        }
//		}
//
//		
		

//        
		// CartItem cartItem =
		// cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

//		

//     
//
//		cart.setTotalPrice(cart.getTotalPrice() + (product.getDiscountedPrice() * req.getQuantity()));
////
//		cartRepository.save(cart);
////        
//
//		return "updated successfully";
//	}

//
	private Cart createCart() {
		Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
		System.out.println("logged in email " + authUtil.loggedInEmail());
		System.out.println(userCart);
		if (userCart != null) {
			return userCart;
		}

		Cart cart = new Cart();
		cart.setTotalPrice(0.00);
		cart.setUser(authUtil.loggedInUser());
		System.out.println("user: " + authUtil.loggedInUser());
		Cart newCart = cartRepository.save(cart);
		System.out.println(newCart);

		return newCart;
	}

//
//    @Transactional
//    @Override
//    public String deleteProductFromCart(Long cartId, Long productId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new ResourceNotFoundException("Product", "productId", productId);
//        }
//
//        cart.setTotalPrice(cart.getTotalPrice() -
//                (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
//
//        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
//    }
//

//    @Override
//    public void updateProductInCarts(Long cartId, Long productId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
//        }
//
//        double cartPrice = cart.getTotalPrice()
//                - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//        cartItem.setProductPrice(product.getSpecialPrice());
//
//        cart.setTotalPrice(cartPrice
//                + (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        cartItem = cartItemRepository.save(cartItem);
//    }

}
