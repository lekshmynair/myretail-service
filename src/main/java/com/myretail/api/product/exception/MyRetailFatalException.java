package com.myretail.api.product.exception;
/**
 * 
 * @author lekshmynair
 * New Exception class for throwing MyRetailFatalException 
 */
public class MyRetailFatalException extends Exception{
	
	public MyRetailFatalException() {
		super();
	}
	
	public MyRetailFatalException(String message, Exception e) {
		super(message, e);
	}

}
