package com.example.Ecommerce_BE.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Address;
import com.example.Ecommerce_BE.model.service.AddressService;
import com.example.Ecommerce_BE.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	AddressRepository addressRepository;

	@Override
	public Address validAddress(Address address) {
		// TODO Auto-generated method stub
		
		if(addressRepository.existsByCityAndDistrictAndWardAndDetail(address.getCity(),
				address.getDistrict(), address.getWard(), address.getDetail()))
		{	
//			List<Address> addresses = ;
			return addressRepository.findByCityAndDistrictAndWardAndDetail(address.getCity(),
					address.getDistrict(), address.getWard(), address.getDetail());
		}
		else {
			return addressRepository.save(address);
		}
	}
	
	
	
}
