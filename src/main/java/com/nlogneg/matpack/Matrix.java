package com.nlogneg.matpack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.simplematrix.ParallelOperation;
import com.nlogneg.matpack.threadcenter.ThreadCenter;

public abstract class Matrix {
	private final int rows, cols;
	
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
				if(!(getElement(i, j) == matrixB.getElement(i, j))){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public List<Future<?>> setUpThreadedOperation(Matrix matrixB, Matrix result, ParallelOperationType operation, double scalar){
		ThreadCenter tc = ThreadCenter.getInstance();
		
		int sublistSize, remainderList, rows, processors, currentRow;
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		rows = getRows();
		processors = tc.getNumProcessors();
		currentRow = 0;
		
		sublistSize = rows/tc.getNumProcessors();
		remainderList = rows%tc.getNumProcessors();
		
		for(int i = 0; i < processors-1; i++){
			ParallelOperation op = new ParallelOperation(operation, this, matrixB, 
					result, currentRow, currentRow+sublistSize, scalar);
			
			currentRow += sublistSize;
			
			futures.add(tc.submitTask(op));
		}
		
		ParallelOperation op = new ParallelOperation(operation, this, matrixB,
				result, currentRow, currentRow+sublistSize+remainderList, scalar);
		
		futures.add(tc.submitTask(op));
		
		return futures;
	}
	
	public abstract double getElement(int row, int col);
	public abstract void setElement(int row, int col, double value);
	
	/**
	 * Add two matrices together (this + matrixB). Throws InvalidDimensionException if
	 * the matrices cannot be added together
	 * @param matrixB Matrix to add to this matrix
	 * @return a newly constructed matrix with the sum of the two matrices
	 */
	public abstract Matrix add(Matrix matrixB) throws InvalidDimensionException;
	
	public abstract Matrix subtract(Matrix matrixB) throws InvalidDimensionException;
	
	public abstract Matrix multiply(Matrix matrixB) throws InvalidDimensionException;
	
	public abstract Matrix scalarMultiply(double scalar);
	
	public abstract Matrix scalarDivide(double scalar);
	
	public abstract Matrix gaussianElimination();
	
	public abstract Matrix transpose();
	
	public abstract Matrix inverse();
	
}
