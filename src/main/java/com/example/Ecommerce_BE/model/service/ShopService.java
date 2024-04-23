package com.example.Ecommerce_BE.model.service;


import com.example.Ecommerce_BE.model.entity.Shop;

public interface ShopService {
	Shop saveOrUpdate(Shop shop);
	boolean existsByNameShop(String nameShop);
	Shop getShopById(int idShop);		
}
