package com.myretail.api.product.controller;

import java.io.IOException;
import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.myretail.api.product.domain.CurrentPrice;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.dto.MyRetailResponse400;
import com.myretail.api.product.dto.MyRetailResponse404;
import com.myretail.api.product.dto.MyRetailResponse500;
import com.myretail.api.product.exception.MyRetailFatalException;
import com.myretail.api.product.service.ProductService;

/**
 * 
 * @author lekshmynair ProductController class
 */

@RestController
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService prodService;

    /**
     * Returns product details and price info associated with product ID
     * 
     * @param id
     *            ProductID numeric
     * @return Product and price info if the product is valid; otherwise, returns
     *         error message.
     */
    @RequestMapping(value = "/products/v1/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getProductDetails(@PathVariable("id") String id) {
        ResponseEntity<Object> responseEntity = null;
        Long prodId = null;
        try {
            prodId = validateProductID(id);
        } catch (MyRetailFatalException me) {
            log.warn("Bad request for productID: {}", id);
            return new ResponseEntity<Object>(new MyRetailResponse400(), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Product> product = prodService.getProductDetails(prodId);
            if (product.isPresent()) {
                String jsonResult = createJSONObject(product.get());
                responseEntity = new ResponseEntity<Object>(jsonResult, HttpStatus.OK);
            } else { // product not found
                responseEntity = new ResponseEntity<Object>(new MyRetailResponse404(), HttpStatus.NOT_FOUND);
                log.warn("Product ID {} not found - red sky API", id);
            }
        }catch (MyRetailFatalException e) {
            responseEntity = new ResponseEntity<Object>(new MyRetailResponse500(), HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("Error getting product details for product ID: {}", id);
        }
        return responseEntity;
    }

    /**
     * Returns updated product price after the price is updated in data store
     * 
     * @param id
     *            productID for the product to be udpated
     * @param input
     *            JSON containing price amount and currency code
     * @return JSON containing product id, product name, price amount(new), currency
     *         code
     */
    @RequestMapping(value = "/products/v1/{id}/price", method = RequestMethod.PUT)
    @ResponseBody
    public Object updatePrice(@PathVariable("id") String id, @RequestBody String input) {
        ResponseEntity<Object> responseEntity = null;
        Optional<ProductPrice> prodPrice;
        Long prodId = null;
        try {
            prodId = validateProductID(id);
            prodPrice = validatePriceRequestBody(input);
        } catch (MyRetailFatalException me) {
            log.warn("Bad request for productID: {}", id);
            return new ResponseEntity<Object>(new MyRetailResponse400(), HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<Product> product = prodService.getProductDetails(prodId);
            if (product.isPresent()) { /* update price if the product exists in red sky */
                prodPrice.get().setProduct(product.get());
                prodService.updatePrice(prodPrice.get());
                // After price is updated successfully, set the price of the product to new
                // price
                product.get().setProdPrice(prodPrice.get());
                String jsonResult = createJSONObject(product.get());
                log.info("Returning price for product: {}", product.get());
                responseEntity = new ResponseEntity<Object>(jsonResult, HttpStatus.OK);
            } else { // product not found
                log.warn("Product not found for id: {}", id);
                responseEntity = new ResponseEntity<Object>(new MyRetailResponse404(), HttpStatus.NOT_FOUND);
            }
        } catch (MyRetailFatalException me) {
            log.error("updatePrice, Price update error for product ID: {}", id);
            responseEntity = new ResponseEntity<Object>(new MyRetailResponse500(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    private Long validateProductID(String id) throws MyRetailFatalException {
        Long productID = null;
        try {
            productID = Long.valueOf(id);
        } catch (NumberFormatException ne) {
            throw new MyRetailFatalException("Product ID not numeric", ne);
        }
        return productID;
    }

    private Optional<ProductPrice> validatePriceRequestBody(String input) throws MyRetailFatalException {
        Optional<ProductPrice> prodPrice = Optional.empty();
        try {
            CurrentPrice currPrice = new ObjectMapper().readValue(input, CurrentPrice.class);
            prodPrice = Optional.of(new ProductPrice(currPrice.getValue(), currPrice.getCurrencyCode()));
        } catch (IOException ie) {
            log.error("Error parsing price request body: {}", input);
            throw new MyRetailFatalException("Error parsing price request", ie);
        }
        return prodPrice;
    }

    /*
     * This method creates the response JSON string with all needed fields :
     * productID, product Name, current Price Value, currency code
     */
    private String createJSONObject(Product prod) throws MyRetailFatalException {
        String result = null;
        String nullValue = null;
        try {
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
                root.put("current_price", nullValue);
            }
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            return result;
        } catch (IOException ie) {
            log.error("Error creating JSON response");
            throw new MyRetailFatalException("Error creating JSON response", ie);
        }
    }
}