package com.example.Ecommerce_BE.model.service;

import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Customer;


public interface CustomerService {
	Customer saveOrUpdate(Customer customer);
	Customer findCustomerByUsername(String username);
	Customer findCustomerById(int idCus);
}
