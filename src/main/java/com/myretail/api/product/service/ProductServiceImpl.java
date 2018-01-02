package com.myretail.api.product.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myretail.api.product.cache.ProductCache;
import com.myretail.api.product.dao.PriceDAO;
import com.myretail.api.product.dao.ProductDAO;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;

/**
 * 
 * @author lekshmynair ProductRestServiceImpl implements ProductService
 *         Interface
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductDAO prodDAO;

    @Autowired
    PriceDAO priceDAO;

    @Autowired
    ProductCache prodCache;

    /**
     * Method to get product details from Target redsky API and price info from data
     * store
     */
    @Override
    public Optional<Product> getProductDetails(Long id) throws MyRetailFatalException {
        Optional<Product> product = Optional.empty();
        try {
            product = prodCache.getProductDetails(id);
            if (product.isPresent()) {
                Optional<ProductPrice> prodPrice = priceDAO.getPriceByProductId(product.get());
                if (prodPrice.isPresent()) {
                    product.get().setProdPrice(prodPrice.get());
                } else {
                    product.get().setProdPrice(null);
                }
            } else { // product not found
                log.info("ProductServiceImpl --> Product not found for product ID : " + id);
            }
        } catch (MyRetailFatalException e) {
            log.error("ProductRestServiceImpl --> Error reading Product details for product ID : " + id, e);
            throw new MyRetailFatalException("Error getting product details", e);
        }
        return product;
    }

    /**
     * Method to update price info into data store
     */
    @Override
    public void updatePrice(ProductPrice prodPrice) throws MyRetailFatalException {
        try {
            priceDAO.updateProductPrice(prodPrice);
            log.info("ProductServiceImpl --> price updated successfully");
        } catch (Exception e) {
            log.error("ProductServiceImpl --> Price update error for ID: " + prodPrice.getProduct().getId(), e);
            throw new MyRetailFatalException("Price update error", e);
        }
        return;
    }

}
