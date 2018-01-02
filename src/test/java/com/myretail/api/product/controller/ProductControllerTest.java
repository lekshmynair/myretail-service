package com.myretail.api.product.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.api.product.config.TestConfig;
import com.myretail.api.product.domain.CurrentPrice;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.domain.ProductPrice;
import com.myretail.api.product.dto.MyRetailResponse404;
import com.myretail.api.product.dto.MyRetailResponse500;
import com.myretail.api.product.exception.MyRetailFatalException;
import com.myretail.api.product.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class ProductControllerTest {
    @Mock
    ProductService prodService;

    @InjectMocks
    ProductController controller;

    MockMvc mockMvc;
    Optional<Product> validProduct = Optional.empty();
    Optional<Product> emptyProduct = Optional.empty();
    Optional<ProductPrice> prodPrice = Optional.empty();
    private static final Long productID = Long.valueOf(16696652L);

    Optional<Product> updProduct = Optional.empty();
    Optional<ProductPrice> updProdPrice = Optional.empty();
    Optional<ProductPrice> newProdPrice = Optional.empty();
    private static final Long updProductID = Long.valueOf(13860428L);
    String stringID = "16696652";
    String updStringID = "13860428";
    Double amount = Double.valueOf(99.55);
    Double newAmount = Double.valueOf(55.99);
    double value = 55.99;
    String prodName = "Beats Solo 2 Wireless Headphone Black";
    String updProdName = "Blu-ray BIG LEBOWSKI, THE Movies";
    String currencyCode = "USD";
    String validResponse = "{\n" + "  \"id\" : 16696652,\n"
            + "  \"name\" : \"Beats Solo 2 Wireless Headphone Black\",\n" + "  \"current_price\" : {\n"
            + "    \"value\" : 99.55,\n" + "    \"currency_code\" : \"USD\"\n" + "  }\n" + "}";
    String errorResponse = "{\"message\":\"Unexpected Error occured, please try again\"}";
    String NotFndErrorResponse = "{\"message\":\"Product Not Found\"}";
    String validUpdResponse = "{\n" + "  \"id\" : 13860428,\n" + "  \"name\" : \"Blu-ray BIG LEBOWSKI, THE Movies\",\n"
            + "  \"current_price\" : {\n" + "    \"value\" : 55.99,\n" + "    \"currency_code\" : \"USD\"\n" + "  }\n"
            + "}";
    CurrentPrice currPrice = new CurrentPrice(value, currencyCode);
    ResponseEntity<Object> expectedResponseEntityGood = null;
    ResponseEntity<Object> expectedResponseEntityNotFnd = null;
    ResponseEntity<Object> expectedResponseEntityError = null;
    ResponseEntity<Object> updExpectedResponseEntityGood = null;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // variables for GetProductDetail
        validProduct = Optional.of(new Product(productID, prodName));
        prodPrice = Optional.of(new ProductPrice(amount, currencyCode));
        validProduct.get().setProdPrice(prodPrice.get());
        prodPrice.get().setProduct(validProduct.get());
        expectedResponseEntityGood = new ResponseEntity<Object>(validResponse, HttpStatus.OK);
        expectedResponseEntityNotFnd = new ResponseEntity<Object>(new MyRetailResponse404(), HttpStatus.NOT_FOUND);
        expectedResponseEntityError = new ResponseEntity<Object>(new MyRetailResponse500(),
                HttpStatus.INTERNAL_SERVER_ERROR);

        // variables for testupdateService
        updProduct = Optional.of(new Product(updProductID, updProdName));
        updProdPrice = Optional.of(new ProductPrice(amount, currencyCode));
        updProduct.get().setProdPrice(updProdPrice.get());
        updProdPrice.get().setProduct(updProduct.get());
        newProdPrice = Optional.of(new ProductPrice(newAmount, currencyCode));
        newProdPrice.get().setProduct(updProduct.get());

        expectedResponseEntityGood = new ResponseEntity<Object>(validResponse, HttpStatus.OK);
        expectedResponseEntityNotFnd = new ResponseEntity<Object>(new MyRetailResponse404(), HttpStatus.NOT_FOUND);
        expectedResponseEntityError = new ResponseEntity<Object>(new MyRetailResponse500(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        updExpectedResponseEntityGood = new ResponseEntity<Object>(validResponse, HttpStatus.OK);

        // create mockMvc
        final StaticApplicationContext applicationContext = new StaticApplicationContext();
        final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
        webMvcConfigurationSupport.setApplicationContext(applicationContext);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setHandlerExceptionResolvers(webMvcConfigurationSupport.handlerExceptionResolver()).build();
    }

    @Test
    public void testGetProductDetail() throws Exception {
        when(prodService.getProductDetails(productID)).thenReturn(validProduct);
        MvcResult mvcResult = mockMvc
                .perform(get("/products/v1/" + stringID)
                        .accept(org.springframework.http.MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk()).andReturn();
        assertEquals(validResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testGetProductDetailNotFound() throws Exception {
        when(prodService.getProductDetails(productID)).thenReturn(emptyProduct);
        MvcResult mvcResult = mockMvc
                .perform(get("/products/v1/" + stringID)
                        .accept(org.springframework.http.MediaType.parseMediaType("application/json")))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals(NotFndErrorResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testGetProductDetailException() throws Exception {
        when(prodService.getProductDetails(productID)).thenThrow(new MyRetailFatalException());
        MvcResult mvcResult = mockMvc
                .perform(get("/products/v1/" + stringID)
                        .accept(org.springframework.http.MediaType.parseMediaType("application/json")))
                .andExpect(status().is5xxServerError()).andReturn();
        assertEquals(errorResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testupdateService() throws Exception {
        when(prodService.getProductDetails(updProductID)).thenReturn(updProduct);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(prodService).updatePrice(newProdPrice.get());
        MvcResult mvcResult = mockMvc
                .perform(put("/products/v1/" + updStringID + "/price").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(currPrice)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(validUpdResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testupdateServiceProdNotFound() throws Exception {
        when(prodService.getProductDetails(updProductID)).thenReturn(emptyProduct);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(prodService).updatePrice(newProdPrice.get());
        MvcResult mvcResult = mockMvc
                .perform(put("/products/v1/" + updStringID + "/price").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(currPrice)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals(NotFndErrorResponse, mvcResult.getResponse().getContentAsString());
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
