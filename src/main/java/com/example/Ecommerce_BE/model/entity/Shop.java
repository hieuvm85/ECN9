package com.example.Ecommerce_BE.model.entity;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String nameShop;
	private String linkImageAvatarShop;
	private String linkImageShop;
	private EStatusShop status;
	
	@OneToOne
	private Customer customer;
	
	@OneToMany
	List<Product> products;
}
