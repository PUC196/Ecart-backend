package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.exceptions.OrderException;
import com.app.pojo.Address;
import com.app.pojo.Order;
import com.app.pojo.OrderItem;
import com.app.pojo.User;
import com.app.repository.OrderItemRepository;
@Service
public class OrderItemServiceImpl implements OrderItemService{
  @Autowired
	private OrderItemRepository orderItemRepo;
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		
		return orderItemRepo.save(orderItem);
		
	}

	
}