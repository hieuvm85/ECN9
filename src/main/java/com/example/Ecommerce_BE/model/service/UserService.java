package com.example.Ecommerce_BE.model.service;

import com.example.Ecommerce_BE.model.entity.Users;

public interface UserService {
	Users findByUsername(String username);
	boolean existsByUserName(String username);
	boolean existsByEmail(String email);
	Users saveOrUpdate(Users user);
}
