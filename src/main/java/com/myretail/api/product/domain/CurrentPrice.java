package com.myretail.api.product.domain;
/**
 * 
 * @author lekshmynair
 * class to read price from the request for updatePrice PUT requests
 */
public class CurrentPrice {
	
	String currencyCode;
	double value;
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	@Override
	public String toString() {
		return "CurrentPrice [value=" + value + ", currencyCode=" + currencyCode + "]";
	}
	
	
}
