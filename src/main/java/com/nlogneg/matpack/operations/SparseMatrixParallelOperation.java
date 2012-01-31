package com.nlogneg.matpack.operations;

import java.util.HashSet;
import java.util.Set;

import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.sparseHashMatrix.SparseHashMatrix;


public class SparseMatrixParallelOperation implements Runnable{

	private SparseHashMatrix matrixA;
	private SparseHashMatrix matrixB;
	private SparseHashMatrix result;

	private ParallelOperationType operation;

	private Set<IntegerTwoTuple> tuplesA;
	private Set<IntegerTwoTuple> tuplesB;

	private double scalar;

	public SparseMatrixParallelOperation(SparseHashMatrix matrixA, SparseHashMatrix matrixB, SparseHashMatrix result, 
			ParallelOperationType operation, Set<IntegerTwoTuple> tuplesA, Set<IntegerTwoTuple> tuplesB, double scalar){
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.result = result;

		this.operation = operation;
		this.scalar = scalar;

		this.tuplesA = tuplesA;
		this.tuplesB = tuplesB;
	}

	private void add(){
		Set<IntegerTwoTuple> addedTuples = new HashSet<IntegerTwoTuple>();

		for(IntegerTwoTuple i : tuplesA){
			if(!addedTuples.contains(i)){
				int row, col;

				row = i.getRow();
				col = i.getCol();

				result.setElement(row, col, matrixA.getElement(row, col));

				addedTuples.add(i);
			}
		}

		for(IntegerTwoTuple i : tuplesB){
			if(!addedTuples.contains(i)){
				int row, col;
				double previousElement;

				row = i.getRow();
				col = i.getCol();

				previousElement = result.getElement(row, col);

				result.setElement(row, col, previousElement + matrixB.getElement(row, col));

				addedTuples.add(i);
			}
		}
	}

	private void subtract(){
		Set<IntegerTwoTuple> addedTuples = new HashSet<IntegerTwoTuple>();

		for(IntegerTwoTuple i : tuplesA){
			if(!addedTuples.contains(i)){
				int row, col;

				row = i.getRow();
				col = i.getCol();

				result.setElement(row, col, matrixA.getElement(row, col));

				addedTuples.add(i);
			}
		}

		for(IntegerTwoTuple i : tuplesB){
			if(!addedTuples.contains(i)){
				int row, col;
				double previousElement;

				row = i.getRow();
				col = i.getCol();

				previousElement = result.getElement(row, col);

				result.setElement(row, col, previousElement - matrixB.getElement(row, col));

				addedTuples.add(i);
			}
		}
	}
	
	/*
	 * Quick Note: This assumes that the current thread has all of the elements in the current
	 * row. 
	 */
	private void multiply(){
		
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
		}
	}

}
