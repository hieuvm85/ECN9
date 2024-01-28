package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Order { // mot order chi chua nhung san pham cua cung mot shop

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private LocalDateTime created;
	private int totalMoneyItem;
	private int totalMonneyShip;
	private int amount;
		
	private EStatusOrder statusOrder;
	
	@OneToMany
	private List<Cart> carts;
	
	@OneToOne
	private Address address;
	
	@ManyToOne
	private Customer customer;
	
	
}
