package com.example.Ecommerce_BE.model.entity;

public enum EStatusOrder {
	WAITE_CONFIRM, // cho nguoi ban hang xac nhan don
	PREPARE_GOODS,// cho nguoi ban hang chuan bi hang
	DELIVERING,// hang dang duoc giao
	CANCELLED,// don hang bi huy
	DELEVERED,// hang da giao thanh cong
	DELIVERY_REJECTION,// giao khong thanh cong
	RETURN_GOODS,// hoan tra hang
}
