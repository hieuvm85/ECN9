package com.example.Ecommerce_BE.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Ecommerce_BE.model.service.StatisticsService;
import com.example.Ecommerce_BE.payload.request.EType;
import com.example.Ecommerce_BE.payload.request.StatisticsRequest;
import com.example.Ecommerce_BE.payload.response.DataChartResponse;
import com.example.Ecommerce_BE.payload.response.MessageResponse;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
	
	@Autowired
	StatisticsService statisticsService;
	
	@GetMapping("/interest")
	public ResponseEntity<?> getInterest(@RequestBody StatisticsRequest statisticsRequest){
		List<LocalDate[]> labels = statisticsRequest.splitDates();
		DataChartResponse dataChartResponse = new DataChartResponse();
		dataChartResponse.setLabel(labels);
		List<Double> values = new ArrayList<>(); 
		
		
		for(LocalDate[] dates:labels) {
			values.add(statisticsService.getInterestbyDate(dates[0], dates[1]));
		}
		dataChartResponse.setValue(values);
		return ResponseEntity.ok( dataChartResponse );
	}
}
