package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String notification;
	private LocalDateTime created;
	
	@ManyToOne
	private Customer customer;
	
}
