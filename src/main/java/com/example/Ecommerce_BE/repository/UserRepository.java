package com.example.Ecommerce_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{
	Users findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
