package com.example.Ecommerce_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
