package com.example.Ecommerce_BE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.EStatusProduct;
import com.example.Ecommerce_BE.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	List<Product> findByCensorship(EStatusProduct censorship);
}
