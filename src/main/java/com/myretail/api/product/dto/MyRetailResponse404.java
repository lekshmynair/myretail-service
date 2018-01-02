package com.myretail.api.product.dto;

/**
 * 
 * @author lekshmynair Send customized response message for error code 404 (Not
 *         found)
 */
public class MyRetailResponse404 extends MyRetailResponse {

    public MyRetailResponse404() {
        super("Product Not Found");
    }
}
