package com.example.Ecommerce_BE.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecommerce_BE.model.entity.FeedBack;
import com.example.Ecommerce_BE.model.service.FeedbackService;
import com.example.Ecommerce_BE.repository.FeedBackRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService{

	@Autowired
	private FeedBackRepository feedBackRepository;
	@Override
	public FeedBack saveOrUpdate(FeedBack feedBack) {
		// TODO Auto-generated method stub
		return feedBackRepository.save(feedBack);
	}
	
	
}
