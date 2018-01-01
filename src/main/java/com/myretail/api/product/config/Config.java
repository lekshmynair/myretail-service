package com.myretail.api.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myretail.api.product.cache.ProductCache;

@Configuration
public class Config {
/*
	@Bean
	public ProductDAO productDAO() {
		return new ProductDAOImpl();
	}
	
	@Bean
	public PriceDAO priceDAO() {
		return new PriceDAOImpl();
	}*/
	
	/*@Bean
	public ProductService productService() {
		return new ProductRestServiceImpl();
	}*/
	
	@Bean
	public ProductCache productCache() {
		ProductCache cache = new ProductCache();
		cache.init();
		return cache;
	}
}
