package com.myretail.api.product.dao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;

/**
 * 
 * @author lekshmynair Implements PriceDAO Interface
 */

@Repository
public class PriceDAOImpl implements PriceDAO {
    private static Logger log = LoggerFactory.getLogger(PriceDAOImpl.class);
    @Autowired
    Session cassandraSession;

    // Read price of a product from data store
    @Override
    public Optional<ProductPrice> getPriceByProductId(Product prod) throws MyRetailFatalException {
        Optional<ProductPrice> price = Optional.empty();
        try {
            ResultSet results = cassandraSession.execute("SELECT price_amount from price where product_id = ? ",
                    prod.getId());
            if (results != null) {
                Row row = results.one();
                if (row != null) {
                    ProductPrice prodPrice = mapRow(row);
                    prodPrice.setProduct(prod);
                    price = Optional.of(prodPrice);
                }
            }
        } catch (Exception e) {
            log.error("Error retrieving price for the product:{}. Error: {} ", prod.getId(), e);
            throw new MyRetailFatalException("Price read error", e);
        }
        return price;
    }

    // updates price of a product in data store
    @Override
    public void updateProductPrice(ProductPrice price) throws MyRetailFatalException {
        try {
            cassandraSession.execute("insert into price(product_id, price_amount) values(?, ?)",
                    price.getProduct().getId(), price.getAmount());
        } catch (Exception e) {
            log.error("PriceDAOImpl --> Error updating price for the productID: {} with amount: {}. Error: {}",
                    price.getProduct().getId(), price.getAmount(),e);
            throw new MyRetailFatalException("Price update error", e);
        }
    }

    private ProductPrice mapRow(Row row) {
        ProductPrice prodPrice = new ProductPrice();
        prodPrice.setAmount(Double.valueOf(row.getDouble("price_amount")));
        return prodPrice;
    }
}
