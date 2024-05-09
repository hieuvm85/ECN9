package com.example.Ecommerce_BE.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

public interface OrderService {
	Order saveOrUpdate(Order order);
	boolean orderAll(List<Order> orders);
	Order getById(int id);
	List<Order> getByCustomerAndStatusOrder(int idCus,EStatusOrder eStatusOrder);
	List<Order> getByShopAndStatusOrder(int idShop,EStatusOrder eStatusOrder);
	List<Order> getByEstatusOrderAndCreatedBetween(EStatusOrder eStatusOrder, LocalDateTime startDate,LocalDateTime endDate);
	List<Order>  getByCreatedBetween( LocalDateTime startDate, LocalDateTime endDate);
}
