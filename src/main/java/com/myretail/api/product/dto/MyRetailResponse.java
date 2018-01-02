package com.myretail.api.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author lekshmynair This DTO sends response message
 */
public class MyRetailResponse {

    @JsonProperty("message")
    private String message;

    public MyRetailResponse(String msg) {
        message = msg;
    }

}
