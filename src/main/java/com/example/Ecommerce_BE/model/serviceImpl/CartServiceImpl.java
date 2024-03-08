package com.example.Ecommerce_BE.model.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Cart;
import com.example.Ecommerce_BE.model.service.CartService;
import com.example.Ecommerce_BE.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart saveOrUpdate(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.save(cart);
	}

	@Override
	public boolean deleteCart(int id) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(id);
		return true;
	}

	@Override
	public Cart checkCartContain(int idCus, int idProduct) {
		// TODO Auto-generated method stub
		return cartRepository.findByCustomerIdAndProductIdAndStatusBought(idCus, idProduct, false);
	}

	@Override
	public List<Cart> viewMyCart(int idCus) {
		// TODO Auto-generated method stub
		return cartRepository.findByCustomerIdAndStatusBought(idCus, false);
	}

	@Override
	public Cart getById(int id) {
		// TODO Auto-generated method stub
		return cartRepository.getById(id);
	}

	@Override
	public List<Cart> getCartToFeedback(int idCus) {
		// TODO Auto-generated method stub
		LocalDateTime deadline = LocalDateTime.now();
		return cartRepository.findByCustomerIdAndStatusBoughtAndDateTimeCreatedAfter(idCus, true,deadline.minusDays(5));
	}
	
}
