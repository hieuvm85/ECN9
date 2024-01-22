package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Users;
import com.example.Ecommerce_BE.model.service.UserService;
import com.example.Ecommerce_BE.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Users findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public boolean existsByUserName(String username) {
		// TODO Auto-generated method stub
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.existsByEmail(email);
	}

	@Override
	public Users saveOrUpdate(Users user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

}
