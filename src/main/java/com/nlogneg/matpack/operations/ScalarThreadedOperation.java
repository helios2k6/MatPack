package com.nlogneg.matpack.operations;

import com.nlogneg.matpack.Matrix;

public class ScalarThreadedOperation implements Runnable{

	private Matrix matrixA;
	private Matrix result;
	private double scalar;

	private int rowStart;
	private int rowEnd;
	
	public ScalarThreadedOperation(
			Matrix matrixA, 
			Matrix result,
			double scalar, int rowStart, int rowEnd,
			MatrixOperationEnum operation) {
		this.matrixA = matrixA;
		this.result = result;
		this.scalar = scalar;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.operation = operation;
	}

	private MatrixOperationEnum operation;


	private void divideScalar(){
		for(int row = rowStart; row < rowEnd; row++){
			for(int col = 0; col < matrixA.getNumCols(); col++){
				result.setElement(row, col, matrixA.getElement(row, col) / scalar);
			}
		}
	}

	private void multiplyScalar(){
		for(int row = rowStart; row < rowEnd; row++){
			for(int col = 0; col < matrixA.getNumCols(); col++){
				result.setElement(row, col, matrixA.getElement(row, col) * scalar);
			}
		}
	}

	@Override
	public void run() {
		switch(operation){
		case DIVIDE_SCALAR:
			divideScalar();
			break;
		case MULTIPLY_SCALAR:
			multiplyScalar();
			break;
		}
	}

}
