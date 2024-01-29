package com.example.Ecommerce_BE.model.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CUSTOMER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer extends Users{
	
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "address_id")
	private Address adress;
	
	@OneToOne(mappedBy = "customer")
	@JsonBackReference
	private Shop shop;
	
	@OneToOne(mappedBy = "customer")
	@JsonBackReference
	private Wallet wallet;
	
	@OneToMany(mappedBy = "customer")
	@JsonBackReference
	private List<FeedBack> feedBacks;
	
	@OneToMany(mappedBy = "customer")
	@JsonBackReference
	private List<Cart> carts;
	
	@OneToMany(mappedBy = "customer")
	@JsonBackReference
	private List<Order> orders;
	
	@OneToMany(mappedBy = "customer")
	@JsonManagedReference
	private List<Notification> notifications;

	public Address getAdress() {
		return adress;
	}

	public void setAdress(Address adress) {
		this.adress = adress;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public List<FeedBack> getFeedBacks() {
		return feedBacks;
	}

	public void setFeedBacks(List<FeedBack> feedBacks) {
		this.feedBacks = feedBacks;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	
}
