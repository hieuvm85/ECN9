package com.example.Ecommerce_BE.model.entity;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Customer extends Users{
	
	
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address deliveryAdress;
	
	@OneToOne
	private Shop shop;
	
	@OneToOne
	private Wallet wallet;
	
	@OneToMany
	private List<FeedBack> feedBacks;
	
	@OneToMany
	private List<Cart> carts;
	
	@OneToMany
	private List<Order> orders;
	
	@OneToMany
	private List<Notification> notifications;
	
}
