package com.example.Ecommerce_BE.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Notification;
import com.example.Ecommerce_BE.model.service.NotificationService;
import com.example.Ecommerce_BE.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
	private NotificationRepository notificationRepository;
	@Override
	public Notification saveOrUpdate(Notification notification) {
		// TODO Auto-generated method stub
		return notificationRepository.save(notification);
	}
	@Override
	public Notification getById(int id) {
		// TODO Auto-generated method stub
		return notificationRepository.getById(id);
	}

}
