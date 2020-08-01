package com.example.demo;

import java.util.List;

public class Order {
	private long orderId;
	private List<Product> products;
	
	public Order() {
		
	}
	
	public Order(long orderId, List<Product> products) {
		super();
		this.orderId = orderId;
		this.products = products;
	}
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
}
