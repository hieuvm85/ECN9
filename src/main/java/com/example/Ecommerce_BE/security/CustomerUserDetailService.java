package com.example.Ecommerce_BE.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Users;
import com.example.Ecommerce_BE.repository.UserRepository;

@Service
public class CustomerUserDetailService implements UserDetailsService{
	@Autowired
	UserRepository userepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Users user= userepository.findByUsername(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("username not found");
		}
		return CustomerUserDetail.mapUserToUserDetail(user);
	}
}
