package com.example.Ecommerce_BE.payload.response;

import java.util.List;

public class DataChartResponse {

	private List<?> label;
	private List<?> value;
	public List<?> getLabel() {
		return label;
	}
	public void setLabel(List<?> label) {
		this.label = label;
	}
	public List<?> getValue() {
		return value;
	}
	public void setValue(List<?> value) {
		this.value = value;
	} 
		
}
