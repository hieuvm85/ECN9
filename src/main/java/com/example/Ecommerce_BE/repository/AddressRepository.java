package com.example.Ecommerce_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{
	boolean existsByCityAndDistrictAndWardAndDetail(String city, String district, String ward, String detail);
	Address findByCityAndDistrictAndWardAndDetail(String city, String district, String ward, String detail);
}
