package com.myretail.api.product.service;

import java.util.Optional;

import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;
/**
 * 
 * @author lekshmynair
 * provides methods to i) get product details from Target's red sky API and price info from Cassandra 
 *                     ii) updates product price in the data store 
 */
public interface ProductService {

	/**
	 * Method to read product details from Target redsky API
	 * @param id - product ID
	 * @return Optional<Product> - Product details and price 
	 * @throws MyRetailFatalException
	 */
	public Optional<Product> getProductDetails(Long id) throws MyRetailFatalException;
	
	/**
	 * Method to update price of a product in data store
	 * @param productPrice - product details with new price
	 * @throws MyRetailFatalException
	 */
	public void updatePrice(ProductPrice prodPrice) throws MyRetailFatalException;
}
