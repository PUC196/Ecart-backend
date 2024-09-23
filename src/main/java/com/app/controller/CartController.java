package com.app.controller;

import com.app.pojo.Cart;
import com.app.pojo.User;
import com.app.dtos.AddItemRequest;

import com.app.dtos.APIResponse;
import com.app.dtos.CartDTO;
import com.app.exceptions.CartItemException;
import com.app.exceptions.UserException;
import com.app.repository.CartRepository;
import com.app.service.CartService;
import com.app.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addProductToCart(@RequestBody AddItemRequest req){
    	User user=authUtil.loggedInUser();
    	System.out.println(user);
         cartService.addCartItem(user.getUserId(),req);
         
         APIResponse res=new APIResponse();
 		res.setMessage("item added to cart");
 		res.setStatus(true);
 		return new ResponseEntity<>(res,HttpStatus.OK);
    }

//    @GetMapping("/carts")
//    public ResponseEntity<List<CartDTO>> getCarts() {
//        List<CartDTO> cartDTOs = cartService.getAllCarts();
//        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
//    }
//
    @GetMapping("/")
    public ResponseEntity<Cart> getCartById(){
        String emailId = authUtil.loggedInEmail();
        System.out.println(emailId);
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        Cart cart1 = cartService.getCart(emailId, cartId);
        return new ResponseEntity<Cart>(cart1, HttpStatus.OK);
        
//        @GetMapping("/")
//    	//@Operation(description="find cart by user id")
//    	public ResponseEntity<Cart>findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
//    		
//    		User user=userService.findUserProfileByJwt(jwt);
//    		Cart cart=cartService.findUserCart(user.getId());
//    		
//    		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
//    	}
    }
    @PutMapping("/{cartItemId}")
    public ResponseEntity<APIResponse> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestBody Map<String, Object> data) {
        Integer quantity = (Integer) data.get("quantity");
         cartService.updateProductQuantityInCart(cartItemId, quantity);
         APIResponse res=new APIResponse();
  		res.setMessage("updated successfull");
  		res.setStatus(true);
  		return new ResponseEntity<>(res,HttpStatus.OK);
    }
    
    @DeleteMapping("/cart_items/{cartItemId}")
    public ResponseEntity<APIResponse> deleteCartItem(@PathVariable Long cartItemId) throws UserException,CartItemException {
        
            cartService.deleteCartItem(cartItemId);
            
            APIResponse res=new APIResponse();
      		res.setMessage("delete item from cart");
      		res.setStatus(true);
      		return new ResponseEntity<>(res,HttpStatus.OK);

    }
    
   
    }

//
//    @PutMapping("/cart/products/{productId}/quantity/{operation}")
//    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
//                                                     @PathVariable String operation) {
//
//        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
//                operation.equalsIgnoreCase("delete") ? -1 : 1);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/carts/{cartId}/product/{productId}")
//    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
//                                                        @PathVariable Long productId) {
//        String status = cartService.deleteProductFromCart(cartId, productId);
//
//        return new ResponseEntity<String>(status, HttpStatus.OK);
//    }

