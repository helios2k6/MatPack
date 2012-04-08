package com.nlogneg.matpack.gauss;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;

public class SingleThreadedSolver implements GaussSolver {

	private SingleThreadedSolver() {

	}

	private static class SingleThreadedSolverHolder {
		private static final SingleThreadedSolver INSTANCE = new SingleThreadedSolver();
	}

	public static SingleThreadedSolver getInstance() {
		return SingleThreadedSolverHolder.INSTANCE;
	}

	private int selectPivot(Matrix a, int col, int rowStart){
		int selectedRow = rowStart;
		double lastValue = a.getElement(rowStart, col);

		for(int i = rowStart + 1; i < a.getNumRows(); i++){
			double currentValue = a.getElementUsingColMajor(i, col);
			if(Math.abs(lastValue - currentValue) > Matrix.PRECISION){
				selectedRow = i;
				lastValue = currentValue;
			}
		}

		return selectedRow;
	}

	private void swapRows(Matrix a, int rowA, int rowB){
		for(int i = 0; i < a.getNumCols(); i++){
			double temp = a.getElement(rowA, i);
			a.setElement(rowA, i, a.getElement(rowB, i));
			a.setElement(rowB, i, temp);
		}
	}

	private void multiplyRowByScalar(Matrix a, int row, double scalar){
		for(int i = 0; i < a.getNumCols(); i++){
			a.setElement(row, i, a.getElement(row, i) * scalar);
		}
	}

	private void divideRowByScalar(Matrix a, int row, double scalar){
		for(int i = 0; i < a.getNumCols(); i++){
			a.setElement(row, i, a.getElement(row, i) / scalar);
		}
	}

	/**
	 * This function will subtract an array of elements from a row
	 * @param a
	 * @param rowA
	 */
	private void subtractRow(Matrix a, int rowA, double[] elements){
		if(elements.length != a.getNumCols()){
			throw new MatrixOutOfBoundsException();
		}
		
		for(int i = 0; i < a.getNumCols(); i++){
			a.setElement(rowA, i, a.getElement(rowA, i) - elements[i]);
		}
	}
	
	private int detectPivotCol(Matrix a, int row){
		for(int i = 0; i < a.getNumCols(); i++){
			double result = a.getElement(row, i);
			if(result != 0){
				return i;
			}
		}
		return -1;
	}

	private void neutralizeRowsAbove(Matrix a, int row, int pivotCol){
		double currentElement = a.getElement(row, pivotCol);
		for(int i = row - 1; i >= 0; i--){
			double elementAbove = a.getElementUsingColMajor(i, pivotCol);
			
			double multiple = elementAbove/currentElement;
			
			double[] rowCopy = new double[a.getNumCols()];
			//Copy row and multiply it by a multiple
			for(int j = 0; j < a.getNumCols(); j++){
				rowCopy[j] = a.getElement(row, j) * multiple;
			}
			
			subtractRow(a, i, rowCopy);
		}
	}
	
	private void backSubstitute(Matrix a) throws Exception{
		for(int i = a.getNumRows() - 1; i >= 0; i--){
			int pivotCol = detectPivotCol(a, i);
			
			if(pivotCol < 0){
				throw new Exception("Unable to back substitute");
			}
			
			//Neutralize rows above
			neutralizeRowsAbove(a, i, pivotCol);
			
			//Neutralize this row last
			divideRowByScalar(a, i, a.getElement(i, pivotCol));
		}
	}

	@Override
	public void rowReduceToEchelonForm(Matrix a) {
		try {
			rowReduce(a);
			backSubstitute(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rowReduce(Matrix a) {
		for(int row = 0; row < a.getNumRows() && row < a.getNumCols(); row++){
			int pivotRow = selectPivot(a, row, row);
			swapRows(a, row, pivotRow);
			for(int belowPivot = row + 1; belowPivot < a.getNumRows(); belowPivot++){
				for(int col = row + 1; col < a.getNumCols(); col++){
					double result = a.getElement(belowPivot, col) - a.getElement(row, col) * (a.getElement(belowPivot, row) / a.getElement(row, row));
					a.setElement(belowPivot, col, result);
				}
				a.setElement(belowPivot, row, 0);
			}
		}
	}

}
