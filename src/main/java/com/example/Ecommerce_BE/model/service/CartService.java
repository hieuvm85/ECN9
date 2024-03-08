package com.example.Ecommerce_BE.model.service;

import java.util.List;

import com.example.Ecommerce_BE.model.entity.Cart;

public interface CartService {
	Cart saveOrUpdate(Cart cart);
	boolean deleteCart(int id);
	Cart checkCartContain(int idCus,int idProduct);
	List<Cart> viewMyCart(int idCus);
	Cart getById(int id);
	List<Cart> getCartToFeedback(int idCus);
}
