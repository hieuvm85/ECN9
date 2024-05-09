package com.example.Ecommerce_BE.payload.response;

import java.time.LocalDate;

public class Transport {
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	private int totalPayCashOrder;
	private int totalPayWalletOrder;
	private int totalMoneyItemPayCashOrder;
	private int totalMoneyItemPayWalletOrder;
	private int totalCancelOrder;
	private int totalMoneyShipCancelOrder;
	private int totalMoneyShipPayCashOrder;
	private int totalMoneyShipPayWalletOrder;
	private int amountReceivable;
	
	
	public int getTotalPayCashOrder() {
		return totalPayCashOrder;
	}
	public void setTotalPayCashOrder(int totalPayCashOrder) {
		this.totalPayCashOrder = totalPayCashOrder;
	}
	public int getTotalPayWalletOrder() {
		return totalPayWalletOrder;
	}
	public void setTotalPayWalletOrder(int totalPayWalletOrder) {
		this.totalPayWalletOrder = totalPayWalletOrder;
	}
	public int getTotalMoneyItemPayCashOrder() {
		return totalMoneyItemPayCashOrder;
	}
	public void setTotalMoneyItemPayCashOrder(int totalMoneyItemPayCashOrder) {
		this.totalMoneyItemPayCashOrder = totalMoneyItemPayCashOrder;
	}
	public int getTotalMoneyItemPayWalletOrder() {
		return totalMoneyItemPayWalletOrder;
	}
	public void setTotalMoneyItemPayWalletOrder(int totalMoneyItemPayWalletOrder) {
		this.totalMoneyItemPayWalletOrder = totalMoneyItemPayWalletOrder;
	}
	public int getTotalMoneyShipPayCashOrder() {
		return totalMoneyShipPayCashOrder;
	}
	public void setTotalMoneyShipPayCashOrder(int totalMoneyShipPayCashOrder) {
		this.totalMoneyShipPayCashOrder = totalMoneyShipPayCashOrder;
	}
	public int getTotalMoneyShipPayWalletOrder() {
		return totalMoneyShipPayWalletOrder;
	}
	public void setTotalMoneyShipPayWalletOrder(int totalMoneyShipPayWalletOrder) {
		this.totalMoneyShipPayWalletOrder = totalMoneyShipPayWalletOrder;
	}
	public int getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(int amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public int getTotalCancelOrder() {
		return totalCancelOrder;
	}
	public void setTotalCancelOrder(int totalCancelOrder) {
		this.totalCancelOrder = totalCancelOrder;
	}
	public int getTotalMoneyShipCancelOrder() {
		return totalMoneyShipCancelOrder;
	}
	public void setTotalMoneyShipCancelOrder(int totalMoneyShipCancelOrder) {
		this.totalMoneyShipCancelOrder = totalMoneyShipCancelOrder;
	}
	
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
	public Transport(int totalPayCashOrder, int totalPayWalletOrder, int totalMoneyItemPayCashOrder,
			int totalMoneyItemPayWalletOrder, int totalCancelOrder, int totalMoneyShipCancelOrder,
			int totalMoneyShipPayCashOrder, int totalMoneyShipPayWalletOrder, int amountReceivable) {
		super();
		this.totalPayCashOrder = totalPayCashOrder;
		this.totalPayWalletOrder = totalPayWalletOrder;
		this.totalMoneyItemPayCashOrder = totalMoneyItemPayCashOrder;
		this.totalMoneyItemPayWalletOrder = totalMoneyItemPayWalletOrder;
		this.totalCancelOrder = totalCancelOrder;
		this.totalMoneyShipCancelOrder = totalMoneyShipCancelOrder;
		this.totalMoneyShipPayCashOrder = totalMoneyShipPayCashOrder;
		this.totalMoneyShipPayWalletOrder = totalMoneyShipPayWalletOrder;
		this.amountReceivable = amountReceivable;
	}
	public Transport() {
		super();
	}
	
	
}
