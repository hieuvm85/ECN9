package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Shop;
import com.example.Ecommerce_BE.model.service.ShopService;
import com.example.Ecommerce_BE.repository.ShopRepository;

@Service
public class ShopServiceImpl implements ShopService{
	
	@Autowired
	ShopRepository shopRepository;
	
	@Override
	public Shop saveOrUpdate(Shop shop) {
		// TODO Auto-generated method stub
		shopRepository.save(shop);
		return shop;
	}

	@Override
	public boolean existsByNameShop(String nameShop) {
		// TODO Auto-generated method stub
		return shopRepository.existsByNameShop(nameShop);
	}

}
