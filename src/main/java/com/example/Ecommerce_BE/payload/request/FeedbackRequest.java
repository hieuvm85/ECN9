
package com.example.Ecommerce_BE.payload.request;

import java.time.LocalDateTime;
import java.util.List;



import com.example.Ecommerce_BE.payload.response.CartResponse;

public class FeedbackRequest {
	private String comment;
	private int star;
	private LocalDateTime created;
	private List<String> linkImage;
	private CartResponse cartResponse;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	public List<String> getLinkImage() {
		return linkImage;
	}
	public void setLinkImage(List<String> linkImage) {
		this.linkImage = linkImage;
	}
	public CartResponse getCartResponse() {
		return cartResponse;
	}
	public void setCartResponse(CartResponse cartResponse) {
		this.cartResponse = cartResponse;
	}
	
	
}
