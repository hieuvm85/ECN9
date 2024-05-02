package com.example.Ecommerce_BE.model.service;

import java.time.LocalDate;

public interface StatisticsService {
	double getInterestbyDate(LocalDate startDate, LocalDate endDate);
}
