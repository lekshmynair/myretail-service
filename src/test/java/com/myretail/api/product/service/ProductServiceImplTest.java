package com.myretail.api.product.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.myretail.api.product.cache.ProductCache;
import com.myretail.api.product.config.TestConfig;
import com.myretail.api.product.dao.PriceDAO;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProductServiceImplTest {
	@Mock
	ProductCache mockCache;
	
	@Mock
	PriceDAO priceDAO;
	
	@Autowired
	@InjectMocks
	ProductService prodService;
	
	Optional<Product> validProduct = Optional.empty();
	Optional<Product> emptyProduct = Optional.empty();
	Optional<ProductPrice> prodPrice = Optional.empty(); 
	private static final Long productID = Long.valueOf(16696652L);
	Double amount = Double.valueOf(99.55);
	String prodName = "Beats Solo 2 Wireless Headphone Black";
	String currencyCode = "USD";
	Double expectedAmount = Double.valueOf(99.55);
	String expectedprodName = "Beats Solo 2 Wireless Headphone Black";
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		validProduct = Optional.of(new Product(productID, prodName));
		prodPrice = Optional.of(new ProductPrice(amount, currencyCode));
	}

	@Test
	public void testValidProduct() throws Exception {
		when(mockCache.getProductDetails(productID)).thenReturn(validProduct);
		when(priceDAO.getPriceByProductId(validProduct.get())).thenReturn(prodPrice);
		
		Optional<Product> prod = prodService.getProductDetails(productID);
		assertTrue(expectedAmount.doubleValue() == prod.get().getProdPrice().getAmount().doubleValue());
		assertTrue(expectedprodName.equalsIgnoreCase(prod.get().getName()));
	}

	@Test
	public void testProductNotFound() throws Exception {
		when(mockCache.getProductDetails(productID)).thenReturn(emptyProduct);
		when(priceDAO.getPriceByProductId(validProduct.get())).thenReturn(Optional.empty());
		
		Optional<Product> prod = prodService.getProductDetails(productID);
		//System.out.println("amount:" + prod.get().getProdPrice().getAmount() + ", expected amount: " + expectedAmount.doubleValue()+", name: " + prod.get().getName());
		//System.out.println(prod.isPresent());
		assertTrue(!prod.isPresent());
	}
	
	@Test(expected = MyRetailFatalException.class)
	public void testGetProductException() throws Exception {
		when(mockCache.getProductDetails(productID)).thenReturn(validProduct);
		
		when(priceDAO.getPriceByProductId(validProduct.get())).thenThrow(new MyRetailFatalException());
		try {
			Optional<Product> prod = prodService.getProductDetails(productID);
			assertTrue(false);
		} catch(MyRetailFatalException me) {
			assertTrue(true);
			throw new MyRetailFatalException();
		} 
			
	}
}
