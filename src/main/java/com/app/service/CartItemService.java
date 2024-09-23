package com.app.service;

import com.app.exceptions.CartItemException;
import com.app.exceptions.UserException;
import com.app.pojo.Cart;
import com.app.pojo.CartItem;
import com.app.pojo.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws CartItemException,UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size,Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException,UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
