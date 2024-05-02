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
	private Shop shop = new Shop();
	
	public static class Shop {
        private int id;
        private String nameShop;
        private String linkImageAvatarShop;
        private Address address= new Address();
        
        public Shop() {
		}

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
        
        public static class Address {
        	private int id;
        	
        	private String city;
        	private String district;
        	private String ward;
        	private String detail;
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public String getDistrict() {
				return district;
			}
			public void setDistrict(String district) {
				this.district = district;
			}
			public String getWard() {
				return ward;
			}
			public void setWard(String ward) {
				this.ward = ward;
			}
			public String getDetail() {
				return detail;
			}
			public void setDetail(String detail) {
				this.detail = detail;
			}
			public Address(int id, String city, String district, String ward, String detail) {
				super();
				this.id = id;
				this.city = city;
				this.district = district;
				this.ward = ward;
				this.detail = detail;
			}
			public Address() {

			}
			public void setAll(int id, String city, String district, String ward, String detail) {
				this.id = id;
				this.city = city;
				this.district = district;
				this.ward = ward;
				this.detail = detail;
			}
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}
		public void setAll(int id, String nameShop, String linkImageAvatarShop) {
            this.id = id;
            this.nameShop = nameShop;
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
