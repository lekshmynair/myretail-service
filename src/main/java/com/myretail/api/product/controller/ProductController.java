package com.myretail.api.product.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.myretail.api.product.domain.CurrentPrice;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.dto.MyRetailResponse400;
import com.myretail.api.product.dto.MyRetailResponse404;
import com.myretail.api.product.dto.MyRetailResponse500;
import com.myretail.api.product.service.ProductService;

/**
 * 
 * @author lekshmynair
 *  ProductController class
 */

@RestController
public class ProductController {
	private static Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductService prodService;
	
	/* path     : /products/v1/id 
	 * method   : getProductDetails
	 * response : Json string with product id, product name, price amount, currency code */
	
	@RequestMapping(value = "/products/v1/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object getProductDetails(@PathVariable("id") String id) {
		
		ResponseEntity <Object> responseEntity = null;
		try {
			Long prodId = new Long(id);
			Optional<Product> product = prodService.getProductDetails(prodId);
			if (product.isPresent()) {
				
				log.info("ProductController --> getProductDetails - read product info successfully");
				String jsonResult = createJSONObject(product.get());
				responseEntity = new ResponseEntity <Object>(jsonResult, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity <Object> (new MyRetailResponse404(), HttpStatus.NOT_FOUND);
			}
		} catch (NumberFormatException ne) {
			log.error("ProductController --> Product ID " + id + " not numeric");
			responseEntity = new ResponseEntity <Object> (new MyRetailResponse400(), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			log.error("ProductController --> Error getting product details for product ID: " + id);
			responseEntity =  new ResponseEntity <Object>(new MyRetailResponse500(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	/* path    : /products/v1/id/price 
	 * method  : getProductDetails
	 * request : Json string with price & currencyCode  price = {"value":99.99, "currency_code:"USD"}
	 * response: Json string with product id, product name, price amount(new), currency code */
	@RequestMapping(value = "/products/v1/{id}/price", method = RequestMethod.PUT)
	@ResponseBody
	public Object updatePrice(@PathVariable("id") String id, @RequestBody String input) {
		
		ResponseEntity <Object> responseEntity = null;
		try {
			log.info("inside update");
			long prodId = Long.parseLong(id);
			log.info("prod id :" + prodId);
			Optional<ProductPrice> prodPrice = parseRequestPrice(input);
			
			/* if valid price info read from request, update the product price */
			if (prodPrice.isPresent()) { 
				/* read product info for the ID passed*/
				Optional<Product> product = prodService.getProductDetails(prodId);
				
				/* update price if the product exists in red sky*/
				if (product.isPresent()) {
					prodPrice.get().setProduct(product.get());
					prodService.updatePrice(prodPrice.get());
					
					/* if price updated successfully, set the price of the product object to new price*/
					product.get().setProdPrice(prodPrice.get());
					log.debug("ProductController-->  updatePrice, Price updated successfully");
					String jsonResult = createJSONObject(product.get());
					responseEntity =  new ResponseEntity <Object>(jsonResult, HttpStatus.OK);
				} else {
					responseEntity =  new ResponseEntity <Object>(new MyRetailResponse404(), HttpStatus.NOT_FOUND);
				}
			} else {
				responseEntity =  new ResponseEntity<Object>(new MyRetailResponse400(), HttpStatus.BAD_REQUEST);
			}
		} catch (NumberFormatException | IOException nio) {
			responseEntity = new ResponseEntity<Object> (new MyRetailResponse400(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			//TODO: log exception as well
			log.error("ProductController-->  updatePrice, Price update error for product ID: " + id);
			responseEntity =  new ResponseEntity <Object> (new MyRetailResponse500(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	private Optional<ProductPrice> parseRequestPrice(String input) throws JSONException, IOException {
		Optional<ProductPrice> prodPrice = Optional.empty();
		CurrentPrice currPrice = null;
		ObjectMapper mapper = new ObjectMapper();
		log.info("reading");
		try {
		currPrice = mapper.readValue(input, CurrentPrice.class);
		} catch(JsonMappingException je) {
			je.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		log.info("current price: " + currPrice);
		log.info("ProductController-->  parseRequestPrice, value = " + currPrice.getValue() + ", currency_code = " + currPrice.getCurrencyCode());
		ProductPrice price = new ProductPrice(currPrice.getValue(), currPrice.getCurrencyCode());
		prodPrice = Optional.of(price);
		return prodPrice;
	}
	
	private String createJSONObject(Product prod) throws IOException {
		String result = null;

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		root.put("id", prod.getId());
		root.put("name", prod.getName());
		if (prod.getProdPrice() != null) {
			ObjectNode priceNode = mapper.createObjectNode();
			priceNode.put("value", prod.getProdPrice().getAmount());
			priceNode.put("currency_code", prod.getProdPrice().getCurrencyCode());
			root.set("current_price", priceNode);
		} else {
			root.put("current_price", "null");
		}
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
		log.debug("result:" + result);

		return result;
	}
}