package com.example.Ecommerce_BE.model.service;

import java.util.List;

import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

public interface OrderService {
	Order saveOrUpdate(Order order);
	boolean orderAll(List<Order> orders);
}
