package com.example.Ecommerce_BE.model.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Name {
	private String fistName;
	private String midName;
	private String lastName;
	public String getFistName() {
		return fistName;
	}
	public void setFistName(String fistName) {
		this.fistName = fistName;
	}
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
	