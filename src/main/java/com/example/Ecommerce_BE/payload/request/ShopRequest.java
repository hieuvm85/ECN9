package com.example.Ecommerce_BE.payload.request;

import com.example.Ecommerce_BE.model.entity.Address;

public class ShopRequest {
	private String nameShop;
	private Address addressShop;
	public String getNameShop() {
		return nameShop;
	}
	public void setNameShop(String nameShop) {
		this.nameShop = nameShop;
	}
	public Address getAddressShop() {
		return addressShop;
	}
	public void setAddressShop(Address addressShop) {
		this.addressShop = addressShop;
	}
	
	
}
