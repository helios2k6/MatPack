package com.nlogneg.matpack.basicMatrix;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

/**
 * A 2D array-backed Matrix. Takes up O(N x M) space for the matrix size.
 * 
 * The maximum size of any SimpleMatrix is INT_MAX x INT_MAX.
 * 
 * Elements are stored in ROW-MAJOR Order.
 * @author helios2k6
 *
 */
public class BasicMatrix extends Matrix{

	private double matrix[][];

	public BasicMatrix(int rows, int cols) {
		super(rows, cols);
		matrix = new double[rows][cols];
	}

	@Override
	public double getElement(int row, int col) throws MatrixOutOfBoundsException{
		return matrix[row][col];
	}

	@Override
	public void setElement(int row, int col, double value) throws MatrixOutOfBoundsException{
		matrix[row][col] = value;
	}

	public Matrix copyMatrix(){
		int rows, cols;
		rows = getNumRows();
		cols = getNumCols();
		Matrix copy = new BasicMatrix(rows, cols);
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				copy.setElement(i, j, getElement(i, j));
			}
		}

		return copy;
	}

}
