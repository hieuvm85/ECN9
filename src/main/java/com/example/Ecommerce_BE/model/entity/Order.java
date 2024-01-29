package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
@Entity
@Data
@Table(name = "order")
public class Order { // mot order chi chua nhung san pham cua cung mot shop

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private LocalDateTime created;
	private int totalMoneyItem;
	private int totalMonneyShip;
	private int amount;
	
	@Enumerated(EnumType.STRING)
	private EPaymentOption paymentOption;
	
	@Enumerated(EnumType.STRING)	
	private EStatusOrder statusOrder;
	
	@OneToMany(mappedBy = "order")
	@JsonManagedReference
	private List<Cart> carts;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "address_id")
	private Address addressDelivery;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToOne(mappedBy = "order")
	@JsonManagedReference
	private FeedBack feedBack;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public int getTotalMoneyItem() {
		return totalMoneyItem;
	}

	public void setTotalMoneyItem(int totalMoneyItem) {
		this.totalMoneyItem = totalMoneyItem;
	}

	public int getTotalMonneyShip() {
		return totalMonneyShip;
	}

	public void setTotalMonneyShip(int totalMonneyShip) {
		this.totalMonneyShip = totalMonneyShip;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public EPaymentOption getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(EPaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}

	public EStatusOrder getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(EStatusOrder statusOrder) {
		this.statusOrder = statusOrder;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public Address getAddressDelivery() {
		return addressDelivery;
	}

	public void setAddressDelivery(Address addressDelivery) {
		this.addressDelivery = addressDelivery;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public FeedBack getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(FeedBack feedBack) {
		this.feedBack = feedBack;
	}
	
	
	
}
