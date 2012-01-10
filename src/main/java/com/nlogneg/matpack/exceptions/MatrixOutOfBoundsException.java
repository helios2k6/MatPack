package com.nlogneg.matpack.exceptions;

public class MatrixOutOfBoundsException extends ArrayIndexOutOfBoundsException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6413174132753345575L;

	public MatrixOutOfBoundsException(){
		super("Could not find element at coordiante");
	}
}
