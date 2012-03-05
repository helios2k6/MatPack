package com.nlogneg.matpack.sparseHashMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public class SparseHashMatrix extends Matrix {

	private Map<IntegerTwoTuple, Double> matrix;

	public SparseHashMatrix(int rows, int cols) {
		super(rows, cols);
		matrix = new HashMap<IntegerTwoTuple, Double>();
	}

	public Set<IntegerTwoTuple> getTuples(){
		return matrix.keySet();
	}

	public Matrix copyMatrix() {
		return null;
	}

	public double getElement(int row, int col) {
		int numRows = getNumRows();
		int numCols = getNumCols();

		if(row < numRows && col < numCols){
			IntegerTwoTuple coords = IntegerTwoTuple.getTuple(row, col);

			Double d =  matrix.get(coords);

			if(d == null){
				return 0.0;
			}

			return d;
		}else{
			throw new MatrixOutOfBoundsException();
		}

	}

	public void setElement(int row, int col, double value) {
		if(PRECISION < Math.abs(value)){
			IntegerTwoTuple coords = IntegerTwoTuple.getTuple(row, col);

			matrix.put(coords, value);
		}

	}
}
