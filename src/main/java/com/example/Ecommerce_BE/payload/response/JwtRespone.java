package com.example.Ecommerce_BE.payload.response;

import java.util.List;

public class JwtRespone {
	private String token;
	private String type= "Bearer ";
	private int id;
	private String username;
	private String email;
	private List<String> listRoles;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getListRoles() {
		return listRoles;
	}
	public void setListRoles(List<String> listRoles) {
		this.listRoles = listRoles;
	}
	public JwtRespone(String token, int id, String username, String email, List<String> listRoles) {
		super();
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.listRoles = listRoles;
	}
	
}
