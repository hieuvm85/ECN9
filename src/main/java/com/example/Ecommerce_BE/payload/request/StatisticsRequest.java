package com.example.Ecommerce_BE.payload.request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class StatisticsRequest {
	private LocalDate startDate;
	private LocalDate endDate;
	private EType type;
	
	
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public EType getType() {
		return type;
	}
	public void setType(EType type) {
		this.type = type;
	} 
	public List<LocalDate[]> splitDates() {
        List<LocalDate[]> dateRanges = new ArrayList<>();

        switch (type) {
        	case DATE:
	            LocalDate currentDate = startDate;
	            LocalDate adjustedEndDate = endDate.plusDays(1); // Thêm một ngày vào endDate
	            while (currentDate.isBefore(adjustedEndDate)) {
	                LocalDate nextDate = currentDate.plusDays(1);
	                dateRanges.add(new LocalDate[]{currentDate, nextDate});
	                currentDate = nextDate;
	            }
	            break;
        	case WEEK:
                LocalDate startOfWeek = startDate.with(DayOfWeek.MONDAY); // Ngày bắt đầu là thứ Hai gần nhất
                while (startOfWeek.isBefore(endDate) || startOfWeek.equals(endDate)) {
                    LocalDate endOfWeek = startOfWeek.plusDays(6); // Kết thúc tuần là Chủ nhật
                    if (endOfWeek.isAfter(endDate)) { // Đảm bảo không vượt quá endDate
                        endOfWeek = endDate;
                    }
                    dateRanges.add(new LocalDate[]{startOfWeek, endOfWeek});
                    startOfWeek = endOfWeek.plusDays(1); // Bắt đầu tuần tiếp theo là ngày sau ngày kết thúc
                }
                break;
        	case MONTH:
                LocalDate startOfMonth = startDate.with(TemporalAdjusters.firstDayOfMonth());
                while (startOfMonth.isBefore(endDate) || startOfMonth.equals(endDate)) {
                    LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
                    if (endOfMonth.isAfter(endDate)) { // Đảm bảo không vượt quá endDate
                        endOfMonth = endDate;
                    }
                    dateRanges.add(new LocalDate[]{startOfMonth, endOfMonth});
                    startOfMonth = startOfMonth.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()); // Bắt đầu tháng tiếp theo
                }
                break;
            case YEAR:
                LocalDate startOfYear = startDate.with(TemporalAdjusters.firstDayOfYear());
                while (startOfYear.isBefore(endDate) || startOfYear.equals(endDate)) {
                    LocalDate endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear());
                    if (endOfYear.isAfter(endDate)) { // Đảm bảo không vượt quá endDate
                        endOfYear = endDate;
                    }
                    dateRanges.add(new LocalDate[]{startOfYear, endOfYear});
                    startOfYear = startOfYear.plusYears(1).with(TemporalAdjusters.firstDayOfYear()); // Bắt đầu năm tiếp theo
                }
                break;
        }

        return dateRanges;
    }
	
}
