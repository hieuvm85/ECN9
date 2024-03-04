package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity
@Data
@Table(name = "cart")
public class Cart {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private LocalDateTime dateTimeCreated;
	private int quantity;
	//
	private boolean statusBought;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "order_id")
	private Order order;
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isStatusBought() {
		return statusBought;
	}

	public void setStatusBought(boolean statusBought) {
		this.statusBought = statusBought;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}
	
	
	
	
}
