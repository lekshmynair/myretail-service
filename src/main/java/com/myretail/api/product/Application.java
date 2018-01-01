package com.myretail.api.product;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.EnableRetry;

import com.myretail.api.product.cache.ProductCache;
import com.myretail.api.product.dao.PriceDAO;
import com.myretail.api.product.dao.ProductDAO;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;
import com.myretail.api.product.service.ProductService;

@SpringBootApplication
@EnableRetry
public class Application {
	static Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String... args) {
		log.info("Starting myretail service");
		SpringApplication.run(Application.class, args);
		log.info("Starting the service s....done");
		//
		/*ApplicationContext ctx = SpringApplication.run(Application.class, args);
		ProductCache cache = ctx.getBean(ProductCache.class);
		Optional<Product> product = Optional.empty();
		try {
			Long id = Long.valueOf(16696652L);
			product = cache.getProductDetails(id);
			log.info("Product  : " + product.get().toString());
		} catch (MyRetailFatalException me) {
			log.info("Exception thrown :" + me.getMessage());
		}*/
		/*PriceDAO priceDao = ctx.getBean(PriceDAO.class);
		ProductDAO prodDao = ctx.getBean(ProductDAO.class);
		Optional<Product> product = Optional.empty();
		try {
			product = prodDao.findProductDetails(16696652L);
			log.info("Product  : " + product.get().toString());
		} catch (MyRetailFatalException me) {
			log.info("Exception thrown :" + me.getMessage());
		}

		if (product.isPresent()) {
			try {
				Optional<ProductPrice> price = priceDao.getPriceByPoductId(product.get());
				if (price.isPresent()) {
					log.info("price = " + price.get().toString());
				}
			} catch (MyRetailFatalException me) {
				log.info("Exception thrown :" + me.getMessage());

			}
		}*/
		
		/*ProductService prodService = ctx.getBean(ProductService.class);
		Optional<Product> product = Optional.empty();
		try {
			product = prodService.getProductDetails(16696652L);
			log.info("Product  : " + product.get().toString());
			
		} catch (MyRetailFatalException me) {
			log.info("Exception thrown :" + me.getMessage());
		}
		product.get().getProdPrice().setAmount(45.99);
		try {
			prodService.updatePrice(product.get().getProdPrice());
			product = prodService.getProductDetails(16696652L);
			log.info("Product  : " + product.get().toString());
			
		} catch (MyRetailFatalException me) {
			log.info("Exception thrown :" + me.getMessage());
		}*/
	}
}
