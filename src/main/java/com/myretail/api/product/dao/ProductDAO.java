package com.myretail.api.product.dao;

import java.util.Optional;

import com.myretail.api.product.domain.Product;
import com.myretail.api.product.exception.MyRetailFatalException;

/**
 * 
 * @author lekshmynair Method to read product details from red sky API
 */
public interface ProductDAO {
    /**
     * Method to read product info from red sky API
     * 
     * @param id - product ID
     * @return Optional<Product> - product and price info for the ID passed
     * @throws MyRetailFatalException
     */
    public Optional<Product> findProductDetails(Long id) throws MyRetailFatalException;
}
