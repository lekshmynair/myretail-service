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
@ContextConfiguration(classes = { TestConfig.class })
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

    Optional<Product> updProduct = Optional.empty();
    Optional<ProductPrice> newProdPrice = Optional.empty();
    private static final Long updProductID = Long.valueOf(13860428L);
    Double updAmount = Double.valueOf(21.99);
    String updProdName = "Blu-ray BIG LEBOWSKI, THE Movies";
    Double newAmount = Double.valueOf(55.55);
    String expectedUpdProdName = "Blu-ray BIG LEBOWSKI, THE Movies";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validProduct = Optional.of(new Product(productID, prodName));
        prodPrice = Optional.of(new ProductPrice(amount, currencyCode));

        updProduct = Optional.of(new Product(updProductID, updProdName));
        newProdPrice = Optional.of(new ProductPrice(newAmount, currencyCode));
        newProdPrice.get().setProduct(updProduct.get()); // set new price for the product
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
        assertTrue(!prod.isPresent());
    }

    @Test(expected = MyRetailFatalException.class)
    public void testGetProductException() throws Exception {
        when(mockCache.getProductDetails(productID)).thenReturn(validProduct);

        when(priceDAO.getPriceByProductId(validProduct.get())).thenThrow(new MyRetailFatalException());
        try {
            Optional<Product> prod = prodService.getProductDetails(productID);
            assertTrue(false);
        } catch (MyRetailFatalException me) {
            assertTrue(true);
            throw new MyRetailFatalException();
        }

    }

    @Test
    public void testUpdatePrice() {
        try {
            Mockito.doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) {
                    Object[] args = invocation.getArguments();
                    return null;
                }
            }).when(priceDAO).updateProductPrice(newProdPrice.get());
            prodService.updatePrice(newProdPrice.get());
            assertTrue(true);
        } catch (MyRetailFatalException me) {
            assertTrue(false);
        }
    }

    @Test(expected = MyRetailFatalException.class)
    public void testUpdatePriceException() throws MyRetailFatalException {
        Mockito.doThrow(new MyRetailFatalException()).when(priceDAO).updateProductPrice(newProdPrice.get());
        prodService.updatePrice(newProdPrice.get());
        assertTrue(false);
    }

}
