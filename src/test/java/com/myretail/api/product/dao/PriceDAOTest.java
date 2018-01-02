package com.myretail.api.product.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.myretail.api.product.config.TestConfig;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.exception.MyRetailFatalException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class PriceDAOTest {
    @Mock
    Session session;

    @Mock
    Product prod;

    @Mock
    ResultSet resultset;

    @Autowired
    PriceDAO priceDAO;

    Optional<ProductPrice> productPrice = Optional.empty();
    Optional<Product> product = Optional.empty();
    private static final Long productID = Long.valueOf(16696652L);
    Double amount = Double.valueOf(99.55);
    String prodName = "Beats Solo 2 Wireless Headphone Black";
    String currencyCode = "USD";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        product = Optional.of(new Product(productID, prodName));
        productPrice = Optional.of(new ProductPrice(amount, currencyCode));

    }

    @Test
    public void testPriceNotFound() throws MyRetailFatalException {
        Mockito.when(session.execute(any(Statement.class))).thenReturn(null);
        productPrice = priceDAO.getPriceByProductId(product.get());
        assertTrue(!productPrice.isPresent());
    }

    @Test(expected = MyRetailFatalException.class)
    public void testPriceReadException() throws MyRetailFatalException {
        Mockito.when(session.execute(any(Statement.class))).thenThrow(new Exception());
        productPrice = priceDAO.getPriceByProductId(product.get());
        assertTrue(false);
    }

}
