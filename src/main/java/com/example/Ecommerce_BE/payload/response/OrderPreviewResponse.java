package com.example.Ecommerce_BE.payload.response;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.Ecommerce_BE.model.entity.Address;
import com.example.Ecommerce_BE.model.entity.EPaymentOption;
import com.example.Ecommerce_BE.model.entity.EStatusOrder;

public class OrderPreviewResponse {

	private int totalMoneyItem;
	private int totalMonneyShip;
	private int amount;
	private String message;
	private Address addressDelivery;
	@Enumerated(EnumType.STRING)
	private EPaymentOption paymentOption;
	private List<CartResponse> carts;
	private Shop shop;
	
	public static class Shop {
        private int id;
        private String nameShop;
        private String linkImageAvatarShop;

        // Constructor
        public Shop(int id, String nameShop, String linkImageAvatarShop) {
            this.id = id;
            this.nameShop = nameShop;
            this.linkImageAvatarShop = linkImageAvatarShop;
        }

        // Getters and setters
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Address getAddressDelivery() {
		return addressDelivery;
	}

	public void setAddressDelivery(Address addressDelivery) {
		this.addressDelivery = addressDelivery;
	}

	public EPaymentOption getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(EPaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}

	public List<CartResponse> getCarts() {
		return carts;
	}

	public void setCarts(List<CartResponse> carts) {
		this.carts = carts;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	
	
}
