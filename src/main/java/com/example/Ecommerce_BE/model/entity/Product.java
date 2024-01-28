package com.example.Ecommerce_BE.model.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String title;
	private int quantity;
//	private float weight;// kg
	private boolean status;
	private float rate;
	
	
	@ElementCollection
	@CollectionTable(name = "link_mages_product")
	private List<String> linkImages;
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;
	
	@OneToMany
	private List<FeedBack> feedBacks;
	
	@OneToMany
	private List<Cart> cart;
	
}
