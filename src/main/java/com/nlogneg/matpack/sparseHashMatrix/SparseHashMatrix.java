package com.nlogneg.matpack.sparseHashMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTuple;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public class SparseHashMatrix extends Matrix{
	
	Map<IntegerTuple, Double> matrix;
	
	public SparseHashMatrix(int rows, int cols) {
		super(rows, cols);
		matrix = new HashMap<IntegerTuple, Double>();
	}

	public Set<IntegerTuple> getTuples(){
		return matrix.keySet();
	}
	
	@Override
	public Matrix copyMatrix() {
		return null;
	}

	@Override
	public double getElement(int row, int col) {
		int numRows = getRows();
		int numCols = getCols();
		
		if(row < numRows && col < numCols){
			IntegerTuple coords = IntegerTuple.getTuple(row, col);
			
			Double d =  matrix.get(coords);
			
			if(d == null){
				return 0.0;
			}
			
			return d;
		}else{
			throw new MatrixOutOfBoundsException();
		}
		
	}
	@Override
	public void setElement(int row, int col, double value) {
		IntegerTuple coords = IntegerTuple.getTuple(row, col);
		
		matrix.put(coords, value);
	}

	@Override
	public Matrix add(Matrix matrixB) throws InvalidDimensionException {
		
		
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
