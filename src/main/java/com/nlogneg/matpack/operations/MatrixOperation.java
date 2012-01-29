package com.nlogneg.matpack.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.simpleTwoDimensionalMatrix.SimpleTwoDimensionalMatrix;
import com.nlogneg.matpack.sparseHashMatrix.SparseHashMatrix;

public class MatrixOperation {
	public static boolean checkAdditionAndSubtractionDimenions(Matrix a, Matrix b){
		if(a.getRows() == b.getRows() && a.getCols() == b.getCols()){
			return true;
		}
		return false;
	}

	public static boolean checkMultiplicationDimensions(Matrix a, Matrix b){
		if(a.getCols() == b.getRows()){
			return true;
		}
		return false;
	}

	public static boolean checkInverseDimensions(Matrix a){
		if(a.getRows() == a.getCols()){
			return true;
		}
		return false;
	}

	private static List<RunnableWithCallback> splitMatrixUp(Matrix matrixA,
			Matrix matrixB, Matrix result, ParallelOperationType operation, double scalar){

		ThreadCenter tc = ThreadCenter.getInstance();
		int sublistSize, remainderList, rows, processors, currentRow;

		List<RunnableWithCallback> runnables = new ArrayList<RunnableWithCallback>();

		rows = matrixA.getRows();
		processors = tc.getNumProcessors();
		currentRow = 0;

		sublistSize = rows/processors;
		remainderList = rows%processors;

		for(int i = 0; i < processors-1; i++){
			SimpleParallelOperation op = new SimpleParallelOperation(operation, matrixA, matrixB, 
					result, currentRow, currentRow+sublistSize, scalar);

			currentRow += sublistSize;

			runnables.add(new RunnableWithCallback(op));
		}

		SimpleParallelOperation op = new SimpleParallelOperation(operation, matrixA, matrixB,
				result, currentRow, currentRow+sublistSize+remainderList, scalar);

		runnables.add(new RunnableWithCallback(op));

		return runnables;
	}

	public static Matrix add(Matrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		if(checkAdditionAndSubtractionDimenions(matrixA, matrixB)){
			Matrix result = new SimpleTwoDimensionalMatrix(matrixA.getRows(), matrixA.getCols());

			List<RunnableWithCallback> runnables = splitMatrixUp(matrixA, matrixB, result, ParallelOperationType.ADD, 0.0);

			ThreadCenter tc = ThreadCenter.getInstance();

			try {
				List<Thread> threads = tc.submit(runnables);

				for(Thread t : threads){
					t.join(); //Wait for all threads to finish
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return result;
		}else{
			throw new InvalidDimensionException();
		}
	}

	public static Matrix add(SparseHashMatrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		Set<IntegerTwoTuple> tuplesA = matrixA.getTuples();
		Set<IntegerTwoTuple> tuplesB = matrixB.getTuples();
		
		Matrix result = new SparseHashMatrix(matrixA.getCols(), matrixA.getRows());
		
		
		
		return null;
	}

	public static Matrix add(SparseHashMatrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix add(Matrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix subtract(Matrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		if(checkAdditionAndSubtractionDimenions(matrixA, matrixB)){
			Matrix result = new SimpleTwoDimensionalMatrix(matrixA.getRows(), matrixA.getCols());

			List<RunnableWithCallback> runnables = splitMatrixUp(matrixA, matrixB, result, ParallelOperationType.SUBTRACT, 0.0);

			ThreadCenter tc = ThreadCenter.getInstance();

			try {
				List<Thread> threads = tc.submit(runnables);

				for(Thread t : threads){
					t.join(); //Wait for all threads to finish
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return result;
		}else{
			throw new InvalidDimensionException();
		}
	}

	public static Matrix subtract(SparseHashMatrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix subtract(SparseHashMatrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix subtract(Matrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix multiply(Matrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		if(checkMultiplicationDimensions(matrixA, matrixB)){
			Matrix result = new SimpleTwoDimensionalMatrix(matrixA.getRows(), matrixA.getCols());

			List<RunnableWithCallback> runnables = splitMatrixUp(matrixA, matrixB, result, ParallelOperationType.MULTIPLY, 0.0);

			ThreadCenter tc = ThreadCenter.getInstance();

			try {
				List<Thread> threads = tc.submit(runnables);

				for(Thread t : threads){
					t.join(); //Wait for all threads to finish
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return result;
		}else{
			throw new InvalidDimensionException();
		}
	}

	public static Matrix multiply(SparseHashMatrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix multiply(SparseHashMatrix matrixA, Matrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix multiply(Matrix matrixA, SparseHashMatrix matrixB) throws InvalidDimensionException{
		return null;
	}

	public static Matrix multiplyScalar(Matrix matrixA, double scalar){
		Matrix result = new SimpleTwoDimensionalMatrix(matrixA.getRows(), matrixA.getCols());

		List<RunnableWithCallback> runnables = splitMatrixUp(matrixA, null, result, ParallelOperationType.MULTIPLY_SCALAR, scalar);

		ThreadCenter tc = ThreadCenter.getInstance();

		try {
			List<Thread> threads = tc.submit(runnables);

			for(Thread t : threads){
				t.join(); //Wait for all threads to finish
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Matrix multiplyScalar(SparseHashMatrix matrixA, double scalar){
		return null;
	}

	public static Matrix divideScalar(Matrix matrixA, double scalar){
		Matrix result = new SimpleTwoDimensionalMatrix(matrixA.getRows(), matrixA.getCols());

		List<RunnableWithCallback> runnables = splitMatrixUp(matrixA, null, result, ParallelOperationType.DIVIDE_SCALAR, scalar);

		ThreadCenter tc = ThreadCenter.getInstance();

		try {
			List<Thread> threads = tc.submit(runnables);

			for(Thread t : threads){
				t.join(); //Wait for all threads to finish
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Matrix divideScalar(SparseHashMatrix matrixA, double scalar){
		return null;
	}

	public static Matrix transpose(Matrix matrixA){
		return null;
	}

	public static Matrix transpose(SparseHashMatrix matrixA){
		return null;
	}

	public static Matrix gaussianElimination(Matrix matrixA){
		return null;
	}

	public static Matrix gaussianElimination(SparseHashMatrix matrixA){
		return null;
	}

	public static Matrix inverse(Matrix matrixA){
		return null;
	}

	public static Matrix inverse(SparseHashMatrix matrixA){
		return null;
	}

}
