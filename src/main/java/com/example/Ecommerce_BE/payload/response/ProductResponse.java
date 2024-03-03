package com.example.Ecommerce_BE.payload.response;


import java.util.List;

import com.example.Ecommerce_BE.model.entity.Shop;


public class ProductResponse {
	
	private int id;	
	private String title;
	private float rate;
	private int quantitySold;	
	private int originalPrice;
	private int sellingPrice;
	private String linkImage;	
	private Shop shop;
	
//	ProductResponse(int id,String title,float rate,int quantitySold,String linkImage,Shop shop ){
//		
//	}
	
	public int getId() {
		return id;
	}
	
	public ProductResponse(int id, String title, float rate, int quantitySold, int originalPrice, int sellingPrice,
			String linkImage, Shop shop) {
		super();
		this.id = id;
		this.title = title;
		this.rate = rate;
		this.quantitySold = quantitySold;
		this.originalPrice = originalPrice;
		this.sellingPrice = sellingPrice;
		this.linkImage = linkImage;
		this.shop = shop;
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
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public int getQuantitySold() {
		return quantitySold;
	}
	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}
	public String getLinkImage() {
		return linkImage;
	}
	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}	
	
	
}
