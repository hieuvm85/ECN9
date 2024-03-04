package com.example.Ecommerce_BE.model.service;

import java.util.List;

import com.example.Ecommerce_BE.model.entity.Notification;

public interface NotificationService {
	
	Notification saveOrUpdate(Notification notification);
	Notification getById(int id);
}
