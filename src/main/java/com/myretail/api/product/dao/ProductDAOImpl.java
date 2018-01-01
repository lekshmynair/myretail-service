package com.myretail.api.product.dao;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.exception.MyRetailFatalException;

/**
 * 
 * @author lekshmynair
 * Implements ProductDAO Interface
 */
@Repository
public class ProductDAOImpl implements ProductDAO {
	private static Logger log = LoggerFactory.getLogger(ProductDAOImpl.class);

	@Value("${redsky.baseUrl}")
	private String baseURL;

	@Value("${redsky.filter}")
	private String urlTrail;
/**
 * Method to read product and details from Target red sky API and price info from data store	
 */
	@Retryable(value= MyRetailFatalException.class, backoff = @Backoff(delay = 500))
	@Override
	public Optional<Product> findProductDetails(Long id) throws MyRetailFatalException {
		
		Optional<Product> optProduct = Optional.empty();
		
			String prodURL = getProductURL(id.longValue()); // get the redsky URL
			log.info("ProductDAOImpl --> Product Url : " + prodURL);
			Optional<String> prodName = callProductAPI(prodURL); // make call to redsky API

			if (prodName.isPresent()) {
				Product prod = new Product(id, prodName.get());
				optProduct = Optional.of(prod);
				log.info("ProductDAOImpl --> Product desc for id " + id + ": " + prodName.get());
			}
		return optProduct;
	}

	private Optional<String> callProductAPI(String prodURL) throws MyRetailFatalException {
		Optional<String> prodName = Optional.empty();

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> response = null;
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {

			response = restTemplate.exchange(prodURL, HttpMethod.GET, entity, String.class);
			String jsonResultString = response.getBody();
			prodName = parseResponse(jsonResultString);
			//to do what if product name is empty
			log.info("ProductDAOImpl --> Product desc for id : " + prodName.get());
		} catch (RestClientResponseException re) {
			
			if (re.getRawStatusCode() == 404) {  
				if (response.getBody() == null || response.getBody() == "") { // invalid URL
					log.info("ProductDAOImpl --> redsky api returned 404, no response body, invalid URL");
					throw new MyRetailFatalException("Error getting product details", re);
				} else { 
					log.info("ProductDAOImpl --> redsky api returned 404");
				}
			} else {
				log.error("ProductDAOImpl --> Error getting product details");
				throw new MyRetailFatalException("Error getting product details", re);
			}
		}
		return prodName;
	}

	//method to get the product url reading properties
	private String getProductURL(long id) {
		StringBuffer prodURL = new StringBuffer();
		log.info("ProductDAOImpl --> BaseURL from config: " + baseURL + ", filter: " + urlTrail);
		prodURL.append(baseURL).append(id).append(urlTrail);
		log.info("ProductDAOImpl --> Prod URL: " + prodURL.toString());
		return prodURL.toString().trim();
		
	}

	//method to extract product field info from the JSON string returned from redsky API
	private Optional<String> parseResponse(String productString) throws MyRetailFatalException {
		Optional<String> prodName = Optional.empty();
		ObjectMapper mapper = new ObjectMapper();
		log.info("prodString: " + productString);
		try {
			JsonNode rootNode = mapper.readTree(productString);
			prodName = Optional.of(
					rootNode.get("product").get("item").get("product_description").get("general_description").asText());

			return prodName;
		} catch (IOException io) {
			log.error("ProductDAOImpl --> ParseResponse() - Error extracting product name"); 
			throw new MyRetailFatalException("Error getting Product details", io);
		}
	}

}
