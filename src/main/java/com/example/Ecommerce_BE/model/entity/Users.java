package com.example.Ecommerce_BE.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;


@Entity
@Data
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// username la so dien thoai
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column( nullable = false)
	@JsonIgnore
	private String password;
	
	
	@Column
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate created;
	
	@Column(unique = true, nullable = false)
	private String email;
		
	@Column  
	private boolean status;
	
	@Embedded
	private Name name;
	
	private String linkImageAvatar;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id" ))
	private List<Roles> listRoles= new ArrayList<>();
 
	
	
	
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
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

	public List<Roles> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<Roles> listRoles) {
		this.listRoles = listRoles;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getLinkImageAvatar() {
		return linkImageAvatar;
	}

	public void setLinkImageAvatar(String linkImageAvatar) {
		this.linkImageAvatar = linkImageAvatar;
	}
	
	
	
}
