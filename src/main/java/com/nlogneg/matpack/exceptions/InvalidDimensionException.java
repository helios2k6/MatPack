package com.nlogneg.matpack.exceptions;

public class InvalidDimensionException extends Exception{

	private static final long serialVersionUID = 2233074223977512780L;
	
	public InvalidDimensionException(){
		super("Invalid dimensions used for matrix operation");
	}
}
