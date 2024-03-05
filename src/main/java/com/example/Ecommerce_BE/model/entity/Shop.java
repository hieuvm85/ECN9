package com.example.Ecommerce_BE.model.entity;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
@Entity
@Data
@Table(name = "shop")
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String nameShop;
	private String linkImageAvatarShop;
	private String linkImageShop;
	
	@Enumerated(EnumType.STRING)
	private EStatusShop status;
	
	
	
	@OneToOne
	@JsonBackReference
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "address_id")
	private Address addressShop;
	
	@OneToMany(mappedBy = "shop")
	@JsonBackReference
	List<Product> products;

	@OneToMany(mappedBy = "shop")
	@JsonBackReference
	List<Order> orders;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameShop() {
		return nameShop;
	}

	public void setNameShop(String nameShop) {
		this.nameShop = nameShop;
	}

	public String getLinkImageAvatarShop() {
		return linkImageAvatarShop;
	}

	public void setLinkImageAvatarShop(String linkImageAvatarShop) {
		this.linkImageAvatarShop = linkImageAvatarShop;
	}

	public String getLinkImageShop() {
		return linkImageShop;
	}

	public void setLinkImageShop(String linkImageShop) {
		this.linkImageShop = linkImageShop;
	}

	public EStatusShop getStatus() {
		return status;
	}

	public void setStatus(EStatusShop status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getAddressShop() {
		return addressShop;
	}

	public void setAddressShop(Address addressShop) {
		this.addressShop = addressShop;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	
}
