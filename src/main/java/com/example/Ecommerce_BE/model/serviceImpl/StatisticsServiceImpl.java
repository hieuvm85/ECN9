package com.example.Ecommerce_BE.model.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.EStatusOrder;
import com.example.Ecommerce_BE.model.entity.Order;
import com.example.Ecommerce_BE.model.service.StatisticsService;
import com.example.Ecommerce_BE.repository.OrderRepository;


@Service
public class StatisticsServiceImpl implements StatisticsService {
	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public double getInterestbyDate(LocalDate startDate, LocalDate endDate) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);	
        List<Order> deliveredOrders = orderRepository.findByStatusOrderAndCreatedBetween(EStatusOrder.DELEVERED, startDateTime, endDateTime);
        double sum=0;
		for( Order order: deliveredOrders) {
			sum+= order.getTotalMoneyItem();
		}
        
		return sum ;
	}

}
