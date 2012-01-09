package com.nlogneg.matpack.sparseHashMatrix;

public class MatrixElement {
	private final int row;
	private final int col;
	
	private double value;
	
	public MatrixElement(int row, int col){
		this.row = row;
		this.col = col;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
}
