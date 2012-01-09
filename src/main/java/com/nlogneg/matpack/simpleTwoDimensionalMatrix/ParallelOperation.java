package com.nlogneg.matpack.simpleTwoDimensionalMatrix;

import com.nlogneg.matpack.Matrix;

public class ParallelOperation implements Runnable{

	private ParallelOperationType operation;
	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix result;

	private int rowStart;
	private int rowEnd;
	
	private double scalar;

	public ParallelOperation(ParallelOperationType operation, Matrix matrixA,
			Matrix matrixB, Matrix result, int rowStart, int rowEnd, double scalar){

		this.operation = operation;
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.result = result;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.scalar = scalar;
	}

	private void add(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = result.getCols();
			for(int j= 0; j < numCols; j++){
				double sum = matrixA.getElement(i, j) + matrixB.getElement(i, j);
				result.setElement(i, j, sum);
			}
		}
	}

	private void subtract(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = result.getCols();
			for(int j= 0; j < numCols; j++){
				double sum = matrixA.getElement(i, j) - matrixB.getElement(i, j);
				result.setElement(i, j, sum);
			}
		}
	}
	
	private void multiply(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = matrixB.getCols();
			for(int j = 0; j < numCols; j++){
				double runningSum = 0;
				int numMatrixBRows = matrixB.getRows();
				for(int z = 0; z < numMatrixBRows; z++){
					runningSum += matrixA.getElement(i, j)* matrixB.getElement(z, j);
				}
				result.setElement(i,j, runningSum);
			}
		}
	}

	private void multiplyScalar(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = matrixA.getCols();
			for(int j = 0; j < numCols; j++){
				result.setElement(i, j, matrixA.getElement(i, j) * scalar);
			}
		}
	}

	private void divideScalar(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = matrixA.getCols();
			for(int j = 0; j < numCols; j++){
				result.setElement(i, j, matrixA.getElement(i, j) / scalar);
			}
		}
	}

	private void transpose(){
		for(int i = rowStart; i < rowEnd; i++){
			int numCols = matrixA.getCols();
			for(int j = 0; j < numCols; j++){
				result.setElement(j, i, matrixA.getElement(i, j));
			}
		}
	}

	@Override
	public void run() {
		switch(operation){
		case ADD:
			add();
			break;
		case SUBTRACT:
			subtract();
			break;
		case MULTIPLY:
			multiply();
			break;
		case MULTIPLY_SCALAR:
			multiplyScalar();
			break;
		case DIVIDE_SCALAR:
			divideScalar();
			break;
		case TRANSPOSE:
			transpose();
			break;
		case BACK_SUB:
			
			break;
		}
	}

}
