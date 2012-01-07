package com.nlogneg.matpack.simplematrix;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.Operation;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;

/**
 * A 2D array-backed Matrix. Takes up O(N x M) space for the matrix size.
 * 
 * The maximum size of any SimpleMatrix is INT_MAX x INT_MAX.
 * 
 * Elements are stored in ROW-MAJOR Order.
 * @author helios2k6
 *
 */
public class SimpleMatrix extends Matrix{

	private double matrix[][];

	public SimpleMatrix(int rows, int cols) {
		super(rows, cols);
		matrix = new double[rows][cols];
	}

	@Override
	public Matrix add(Matrix matrixB) throws InvalidDimensionException {
		if(getRows() != matrixB.getRows() || getCols() != matrixB.getCols()){
			throw new InvalidDimensionException();
		}

		Matrix result = new SimpleMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, Operation.ADD, 0);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}


	@Override
	public Matrix subtract(Matrix matrixB) throws InvalidDimensionException {
		if(getRows() != matrixB.getRows() || getCols() != matrixB.getCols()){
			throw new InvalidDimensionException();
		}

		Matrix result = new SimpleMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, Operation.SUBTRACT, 0);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}


	@Override
	public Matrix multiply(Matrix matrixB) throws InvalidDimensionException {
		if(getCols() != matrixB.getRows()){
			throw new InvalidDimensionException();
		}

		Matrix result = new SimpleMatrix(getRows(), matrixB.getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, Operation.MULTIPLY, 0);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public double getElement(int row, int col) {
		return matrix[row][col];
	}

	@Override
	public void setElement(int row, int col, double value) {
		matrix[row][col] = value;
	}

	@Override
	public Matrix scalarMultiply(double scalar) {
		Matrix result = new SimpleMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(null, result, Operation.MULTIPLY_SCALAR, scalar);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public Matrix scalarDivide(double scalar) {
		Matrix result = new SimpleMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(null, result, Operation.DIVIDE_SCALAR, scalar);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public Matrix gaussianElimination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix transpose() {
		Matrix result = new SimpleMatrix(getCols(), getRows());

		List<Future<?>> futures = setUpThreadedOperation(null, result, Operation.TRANSPOSE, 0);

		for(Future<?> f : futures){
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public Matrix inverse() {
		// TODO Auto-generated method stub
		return null;
	}

}
