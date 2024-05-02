package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Customer;
import com.example.Ecommerce_BE.model.service.CustomerService;
import com.example.Ecommerce_BE.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Customer saveOrUpdate(Customer customer) {
		// TODO Auto-generated method stub
		return customerRepository.save(customer);
	}

	@Override
	public Customer findCustomerByUsername(String username) {
		// TODO Auto-generated method stub
		return customerRepository.findCustomerByUsername(username);
	}

	@Override
	public Customer findCustomerById(int idCus) {
		// TODO Auto-generated method stub
		return customerRepository.getById(idCus);
	}

}
