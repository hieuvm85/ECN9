package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.Wallet;
import com.example.Ecommerce_BE.model.service.WalletService;
import com.example.Ecommerce_BE.repository.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService{

	@Autowired
	private WalletRepository walletRepository;
	
	@Override
	public Wallet saveOrUpdate(Wallet wallet) {
		// TODO Auto-generated method stub
		return walletRepository.save(wallet);
	}
	
}
