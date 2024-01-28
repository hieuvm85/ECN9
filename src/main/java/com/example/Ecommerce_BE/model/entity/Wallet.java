package com.example.Ecommerce_BE.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int balance;
	
	@OneToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
