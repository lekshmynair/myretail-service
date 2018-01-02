package com.myretail.api.product.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;
import com.myretail.api.product.cache.ProductCache;
import com.myretail.api.product.dao.PriceDAO;
import com.myretail.api.product.dao.PriceDAOImpl;
import com.myretail.api.product.dao.ProductDAO;
import com.myretail.api.product.dao.ProductDAOImpl;
import com.myretail.api.product.service.ProductService;
import com.myretail.api.product.service.ProductServiceImpl;

//@Configuration
public class TestConfig {
	
	@Bean
	public ProductService productService() {
		return new ProductServiceImpl();
	}
	
	@Bean
	public ProductCache productCache() {
		ProductCache cache = new ProductCache();
		cache.init();
		return cache;
	} 
	
	@Bean
	public ProductDAO productDAO() {
		return new ProductDAOImpl();
	}
	
	@Bean
	public PriceDAO priceDAO() {
		return new PriceDAOImpl();
	}
	
	
	@Bean
	public Session CassandraSession() {
		return Mockito.mock(Session.class);
	}
}
