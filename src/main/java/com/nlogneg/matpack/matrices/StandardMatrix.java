package com.nlogneg.matpack.matrices;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public class StandardMatrix extends Matrix{

	double[][] rowMajor;
	double[][] colMajor;

	public StandardMatrix(int rows, int cols){
		super(rows, cols);
		rowMajor = new double[rows][cols];
		colMajor = new double[cols][rows];
	}

	@Override
	public Matrix copyMatrix() {
		Matrix copy = new StandardMatrix(getNumRows(), getNumCols());

		for(int i = 0; i < getNumRows(); i++){
			for(int j = 0; j < getNumCols(); j++){
				copy.setElement(i, j, rowMajor[i][j]);
			}
		}

		return copy;
	}

	@Override
	public double getElement(int row, int col) throws MatrixOutOfBoundsException {
		return rowMajor[row][col];
	}
	
	@Override
	public double getElementUsingColMajor(int row, int col) throws MatrixOutOfBoundsException{
		return colMajor[col][row];
	}

	@Override
	public void setElement(int row, int col, double value) {
		rowMajor[row][col] = colMajor[col][row] = value;
	}
}
