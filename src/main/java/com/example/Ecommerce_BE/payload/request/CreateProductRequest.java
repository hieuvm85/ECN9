package com.example.Ecommerce_BE.payload.request;

import java.util.List;

import com.example.Ecommerce_BE.model.entity.ProductDescriptionDetail;

public class CreateProductRequest {


	private String title;
	private int quantity;
	private float weight;// kg
	private String description;
	private List<String> linkImages;
	private List<ProductDetail> productDescriptionDetails;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getLinkImages() {
		return linkImages;
	}
	public void setLinkImages(List<String> linkImages) {
		this.linkImages = linkImages;
	}
	public List<ProductDetail> getProductDescriptionDetails() {
		return productDescriptionDetails;
	}
	public void setProductDescriptionDetails(List<ProductDetail> productDescriptionDetails) {
		this.productDescriptionDetails = productDescriptionDetails;
	}
	
	public static class ProductDetail{
		private String title;
		private String description;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}
}
