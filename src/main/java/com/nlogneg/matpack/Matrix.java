package com.nlogneg.matpack;

import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public abstract class Matrix {
	
	private final int rows, cols;
	
	public static final double PRECISION = 0.000001; //Magic number (10^-6)

	public Matrix(int rows, int cols){
		this.rows = rows;
		this.cols = cols;
	}
	
	public final int getNumRows(){
		return rows;
	}
	
	public final int getNumCols(){
		return cols;
	}
	
	public boolean equals(Matrix matrixB){
		if(getNumRows() != matrixB.getNumRows() || getNumCols() != matrixB.getNumCols()){
			return false;
		}
		
		for(int i = 0; i < getNumRows(); i++){
			for(int j = 0; j < getNumCols(); j++){
				double equalityResult = Math.abs(getElement(i, j) - matrixB.getElement(i, j));
				
				if(PRECISION < equalityResult){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public final boolean checkAdditionAndSubtractionDimenions(Matrix b){
		return rows == b.getNumRows() && cols == b.getNumCols();
	}
	
	public final boolean checkMultiplicationDimensions(Matrix b){
		return cols == b.getNumRows();
	}
	
	public final boolean checkInverseDimensions(){
		return rows == cols;
	}
	
	@Override
	public String toString(){
		int rows, cols;
		
		rows = getNumRows();
		cols = getNumCols();
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[");
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				buffer.append(getElement(i, j)).append(" ");
			}
			buffer.append("\n");
		}
		buffer.append("]\n");
		
		return buffer.toString();
	}
	
	
	public abstract Matrix copyMatrix();
	
	public abstract double getElement(int row, int col) throws MatrixOutOfBoundsException;
	public abstract void setElement(int row, int col, double value);
	
}
