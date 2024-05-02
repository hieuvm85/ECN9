package com.example.Ecommerce_BE.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findByCustomerIdAndStatusOrder(int customerId,EStatusOrder statusOrder);
	List<Order> findByShopIdAndStatusOrder(int shopId,EStatusOrder statusOrder);
	List<Order>  findByStatusOrderAndCreatedBetween(EStatusOrder statusOrder, LocalDateTime startDate, LocalDateTime endDate);
} 
