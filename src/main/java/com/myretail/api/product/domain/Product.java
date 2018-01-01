package com.myretail.api.product.domain;

/**
 * 
 * @author lekshmynair
 * class to hold product information for an item
 */

public class Product {
	
	Long id;   // product id
	
	String name; // product name
	
	ProductPrice prodPrice; // price
	
	public Product(Long id, String name) {
		this.id = id;
		this.name = name;
		prodPrice = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductPrice getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(ProductPrice prodPrice) {
		this.prodPrice = prodPrice;
	}

	@Override
	public String toString() {
		return "Product [id=" + id.longValue() + ", name=" + name + ", prodPrice=" + prodPrice.getAmount() + ", currency_Code=" + prodPrice.getCurrencyCode() + "]";
	}
	
	
	
}
