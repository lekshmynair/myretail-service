package com.myretail.api.product.dto;

/**
 * 
 * @author lekshmynair
 * Send customized response message for error code 400 (Bad request)
 */
public class MyRetailResponse400 extends MyRetailResponse {
	
	public MyRetailResponse400() {
		super("Invalid input format, must be numeric");
	}
}
