package com.nlogneg.matpack.sparseHashMatrix;

import java.util.HashMap;
import java.util.Map;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;

public class SparseHashMatrix extends Matrix{
	
	private Map<Integer, Map<Integer, MatrixElement>> matrix;
	
	public SparseHashMatrix(int rows, int cols) {
		super(rows, cols);
		
		matrix = new HashMap<Integer, Map<Integer, MatrixElement>>();
	}

	@Override
	public Matrix copyMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getElement(int row, int col) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setElement(int row, int col, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Matrix add(Matrix matrixB) throws InvalidDimensionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix subtract(Matrix matrixB) throws InvalidDimensionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix multiply(Matrix matrixB) throws InvalidDimensionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix scalarMultiply(double scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix scalarDivide(double scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gaussianElimination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Matrix transpose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix inverse() throws InvalidDimensionException {
		// TODO Auto-generated method stub
		return null;
	}

}
