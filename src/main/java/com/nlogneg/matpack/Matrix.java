package com.nlogneg.matpack;

import com.nlogneg.matpack.exceptions.InvalidDimensionException;
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
				
				if(equalityResult > PRECISION){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public abstract Matrix copyMatrix();
	
	public abstract double getElement(int row, int col) throws MatrixOutOfBoundsException;
	public abstract void setElement(int row, int col, double value);
	
	/**
	 * Add two matrices together (this + matrixB). Throws InvalidDimensionException if
	 * the matrices cannot be added together
	 * @param matrixB Matrix to add to this matrix
	 * @return a newly constructed matrix with the sum of the two matrices
	 */
	public abstract Matrix add(Matrix matrixB) throws InvalidDimensionException;
	
	/**
	 * Subtract two matrices (this - matrixB). Throws InvalidDimensionException if
	 * the matrices cannot be subtracted
	 * @param matrixB
	 * @return
	 * @throws InvalidDimensionException
	 */
	public abstract Matrix subtract(Matrix matrixB) throws InvalidDimensionException;
	
	/**
	 * Multiplies two matrices together (this x matrixB) 
	 * @param matrixB
	 * @return
	 * @throws InvalidDimensionException
	 */
	public abstract Matrix multiply(Matrix matrixB) throws InvalidDimensionException;
	
	/**
	 * Multiplies a matrix by a scalar
	 * @param scalar
	 * @return
	 */
	public abstract Matrix scalarMultiply(double scalar);
	
	/**
	 * Divides the matrix by a scalar
	 * @param scalar
	 * @return
	 */
	public abstract Matrix scalarDivide(double scalar);
	
	/**
	 * Performs Gaussian Elimination on the matrix. This is a destructive
	 * function that will alter THIS matrix. You must copy this matrix if you
	 * wish to preserve the original matrix
	 */
	public abstract void gaussianElimination();
	
	/**
	 * Transposes the original matrix and returns the result in a new matrix
	 * @return
	 */
	public abstract Matrix transpose();
	
	/**
	 * Finds the inverse of this matrix and returns the result in a new matrix
	 * @return
	 */
	public abstract Matrix inverse() throws InvalidDimensionException;
	
}
