package com.myretail.api.product.dto;

/**
 * 
 * @author lekshmynair
 * Send customized response message for error code 500 (Internal server error)
 */
public class MyRetailResponse500 extends MyRetailResponse{

	public MyRetailResponse500() {
		super("Unexpected Error occured, please try again");
	}
}
