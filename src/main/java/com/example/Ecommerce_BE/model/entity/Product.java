package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
@Entity
@Data
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String title;
	private int quantity;
	private int originalPrice;
	private int sellingPrice;
	private float weight;// kg
	private boolean statusSale;// true la ban false la khong ban
	private String description;
	private float rate;
	private LocalDateTime dateTimeCreated;
	private int quantitySold;
	
	@Enumerated(EnumType.STRING)
	private EStatusProduct censorship; // co su chap thuan cua ban kiem duyet moderator

	
	
	@ElementCollection
	@CollectionTable(name = "link_mages_product")
	private List<String> linkImages;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "shop_id")
	private Shop shop;
	
	@OneToMany(mappedBy = "product")
	@JsonManagedReference
	private List<FeedBack> feedBacks;
	
	@OneToMany(mappedBy = "product")
	@JsonBackReference
	private List<Cart> cart;
	
	@OneToMany(mappedBy = "product")
	@JsonManagedReference
	private List<ProductDescriptionDetail> productDescriptionDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public boolean isStatusSale() {
		return statusSale;
	}

	public void setStatusSale(boolean statusSale) {
		this.statusSale = statusSale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public EStatusProduct getCensorship() {
		return censorship;
	}

	public void setCensorship(EStatusProduct censorship) {
		this.censorship = censorship;
	}

	public List<String> getLinkImages() {
		return linkImages;
	}

	public void setLinkImages(List<String> linkImages) {
		this.linkImages = linkImages;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<FeedBack> getFeedBacks() {
		return feedBacks;
	}

	public void setFeedBacks(List<FeedBack> feedBacks) {
		this.feedBacks = feedBacks;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public List<ProductDescriptionDetail> getProductDescriptionDetails() {
		return productDescriptionDetails;
	}

	public void setProductDescriptionDetails(List<ProductDescriptionDetail> productDescriptionDetails) {
		this.productDescriptionDetails = productDescriptionDetails;
	}

	public LocalDateTime getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(int sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	
}
