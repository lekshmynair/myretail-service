package com.myretail.api.product.dao;

import java.util.Optional;

import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;
/**
 * 
 * @author lekshmynair
 * Methods to retrieve price info and update price for a product from/into the data store
 */
public interface PriceDAO {
/**
 * Method to retrieve price info for a product from the data store
 * @param prod - product for which price is to be read (with product ID populated)
 * @return Optional<ProductPrice> - price details for the product 
 * @throws MyRetailFatalException
 */
	public Optional<ProductPrice> getPriceByProductId(Product prod) throws MyRetailFatalException ;
	
/**
 * Method to update price info of a product 	
 * @param price - price details of the product with new amount and currency code
 * @throws MyRetailFatalException
 */
	public void updateProductPrice(ProductPrice price) throws MyRetailFatalException;
}
