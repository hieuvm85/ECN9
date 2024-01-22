package com.example.Ecommerce_BE.payload.request;

import java.util.List;

public class SingupRequest {
	private String username;
	private String password;
	private String email;
	private boolean status = true;
	
	private List<String> listRoles;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<String> getListRoles() {
		return listRoles;
	}
	public void setListRoles(List<String> listRoles) {
		this.listRoles = listRoles;
	}
	
	
}
