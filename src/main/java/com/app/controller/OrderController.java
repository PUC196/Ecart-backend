package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exceptions.APIException;
import com.app.exceptions.OrderException;
//import com.app.exceptions.UserException;
import com.app.pojo.Address;
import com.app.pojo.Order;
import com.app.pojo.User;
import com.app.service.OrderService;
import com.app.service.UserService;
import com.app.util.AuthUtil;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthUtil authUtil;
	
	@PostMapping("/")
	public ResponseEntity<Order>createOrder(@RequestBody Address shippingAddress,
			@RequestHeader("Authorization") String jwt) throws APIException{
		User user=authUtil.loggedInUser();
		
		Order order=orderService.createOrder(user,shippingAddress);
		
		System.out.println("order "+order);
		
		return new ResponseEntity<Order>(order,HttpStatus.CREATED);
	}
	
//	@GetMapping("/user")
//	public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
//		
//		User user=userService.findUserProfileByJwt(jwt);
//		List<Order>orders=orderService.usersOrderHistory(user.getId());
//		
//		return new ResponseEntity<>(orders,HttpStatus.CREATED);
//	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Order>findOrderById(
			@PathVariable("id")Long orderId,
			@RequestHeader("Authorization") String jwt) throws APIException,OrderException{
		
		User user=authUtil.loggedInUser();
		Order order=orderService.findOrderById(orderId);
		
		return new ResponseEntity<>(order,HttpStatus.CREATED);
	}

}
