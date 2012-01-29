package com.nlogneg.matpack.sparseHashMatrix;

import java.util.Set;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.operations.ParallelOperationType;

public class SparseMatrixParallelOperation implements Runnable{

	private Set<IntegerTwoTuple> coords;

	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix result;

	private ParallelOperationType operation;

	private double scalar;
	private int row;

	public SparseMatrixParallelOperation(Set<IntegerTwoTuple> coords, 
			Matrix matrixA, Matrix matrixB, Matrix result, ParallelOperationType operation, double scalar, int row){

		this.coords = coords;
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.result = result;

		this.operation = operation;

		this.scalar = scalar;

		this.row = row;
	}

	private void multiply(){
		int cols = result.getCols();
		for(int i = 0; i < cols; i++){
			int runningSum = 0;
			for(IntegerTwoTuple it : coords){
				runningSum += matrixA.getElement(row, it.getCol()) * matrixB.getElement(it.getCol(), i);
			}
			
			result.setElement(row, i, runningSum);
		}
	}

	private void multiplyScalar(){

	}

	private void divideScalar(){

	}

	private void transpose(){

	}

	@Override
	public void run() {
		switch(operation){
		case MULTIPLY:
			multiply();
			break;
		case MULTIPLY_SCALAR:
			break;
		case DIVIDE_SCALAR:
			break;
		case TRANSPOSE:
			break;
		}
	}

}
