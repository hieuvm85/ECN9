package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.ProductDescriptionDetail;
import com.example.Ecommerce_BE.model.service.ProductDescriptionDetailService;
import com.example.Ecommerce_BE.repository.ProductDescriptionDetailRepository;

@Service
public class ProductDescriptionDetailServiceImpl implements ProductDescriptionDetailService{

	@Autowired
	private ProductDescriptionDetailRepository productDescriptionDetailRepository;
	@Override
	public ProductDescriptionDetail saveOrUpdate(ProductDescriptionDetail productDescriptionDetail) {
		// TODO Auto-generated method stub
		return productDescriptionDetailRepository.save(productDescriptionDetail);
	}

}
