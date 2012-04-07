package com.nlogneg.matpack.sparseHashMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public class SparseHashMatrix extends Matrix {

	private Map<IntegerTwoTuple, Double> elementMap;

	public SparseHashMatrix(int rows, int cols) {
		super(rows, cols);
		elementMap = new HashMap<IntegerTwoTuple, Double>();
	}

	public Set<IntegerTwoTuple> getTuples(){
		return elementMap.keySet();
	}

	public Matrix copyMatrix() {
		Matrix copy = new SparseHashMatrix(getNumRows(), getNumCols());
		Set<IntegerTwoTuple> tuples = elementMap.keySet();
		
		for(IntegerTwoTuple t : tuples){
			copy.setElement(t.getRow(), t.getCol(), elementMap.get(t));
		}
		
		return copy;
	}

	public double getElement(int row, int col) {
		int numRows = getNumRows();
		int numCols = getNumCols();

		if(row < numRows && col < numCols){
			IntegerTwoTuple coords = IntegerTwoTuple.getTuple(row, col);

			Double d =  elementMap.get(coords);

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

			elementMap.put(coords, value);
		}

	}
}
