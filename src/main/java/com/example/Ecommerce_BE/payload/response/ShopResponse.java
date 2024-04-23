package com.example.Ecommerce_BE.payload.response;

import java.util.List;


public class ShopResponse {

	public static class Shop{
		private int id;
		
		private String nameShop;
		private String linkImageAvatarShop;
		private String linkImageShop;
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
	}
	private Shop shop = new Shop();
	private List<ProductResponse> productResponses;
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public List<ProductResponse> getProductResponses() {
		return productResponses;
	}
	public void setProductResponses(List<ProductResponse> productResponses) {
		this.productResponses = productResponses;
	}
	public void setShop(int id, String nameShop, String linkImageAvatarShop, String linkImageShop) {
		this.shop.setId(id);
		this.shop.setNameShop(nameShop);
		this.shop.setLinkImageAvatarShop(linkImageAvatarShop); 
		this.shop.setLinkImageShop(linkImageShop);
	}
	
}
