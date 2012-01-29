package com.nlogneg.matpack;

import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public abstract class Matrix {
	private final int rows, cols;
	
	public static final double PRECISION = 0.000001; //Magic number (10^-6)
	
	public static void printMatrix(Matrix matrix){
		int rows, cols;
		
		rows = matrix.getRows();
		cols = matrix.getCols();
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[");
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				buffer.append(matrix.getElement(i, j)).append(" ");
			}
			buffer.append("\n");
		}
		buffer.append("]\n");
	}
	
	public Matrix(int rows, int cols){
		this.rows = rows;
		this.cols = cols;
	}
	
	public int getRows(){
		return rows;
	}
	
	public int getCols(){
		return cols;
	}
	
	public boolean equals(Matrix matrixB){
		if(getRows() != matrixB.getRows() || getCols() != matrixB.getCols()){
			return false;
		}
		
		for(int i = 0; i < getRows(); i++){
			for(int j = 0; j < getCols(); j++){
				double equalityResult = Math.abs(getElement(i, j) - matrixB.getElement(i, j));
				
				if(PRECISION < equalityResult){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public final boolean checkAdditionAndSubtractionDimenions(Matrix b){
		if(rows == b.getRows() && cols == b.getCols()){
			return true;
		}
		return false;
	}
	
	public final boolean checkMultiplicationDimensions(Matrix b){
		if(cols == b.getRows()){
			return true;
		}
		return false;
	}
	
	public final boolean checkInverseDimensions(){
		if(rows == cols){
			return true;
		}
		return false;
	}
	
	public abstract Matrix copyMatrix();
	
	public abstract double getElement(int row, int col) throws MatrixOutOfBoundsException;
	public abstract void setElement(int row, int col, double value);
	
}
