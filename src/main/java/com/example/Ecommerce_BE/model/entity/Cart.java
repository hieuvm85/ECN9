package com.example.Ecommerce_BE.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Cart {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	//
	private boolean statusBought;
	
	@ManyToOne
	private Customer customer;
	@ManyToOne
	private Product product;
	
	@ManyToOne
	@JsonBackReference
	private Order order;
	
	
	
	
}
