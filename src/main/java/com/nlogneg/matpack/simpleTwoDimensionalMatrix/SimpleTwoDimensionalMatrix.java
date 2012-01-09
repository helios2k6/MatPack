package com.nlogneg.matpack.simpleTwoDimensionalMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.threadcenter.ThreadCenter;

/**
 * A 2D array-backed Matrix. Takes up O(N x M) space for the matrix size.
 * 
 * The maximum size of any SimpleMatrix is INT_MAX x INT_MAX.
 * 
 * Elements are stored in ROW-MAJOR Order.
 * @author helios2k6
 *
 */
public class SimpleTwoDimensionalMatrix extends Matrix{

	private double matrix[][];

	public SimpleTwoDimensionalMatrix(int rows, int cols) {
		super(rows, cols);
		matrix = new double[rows][cols];
	}

	private List<Future<?>> setUpThreadedOperation(Matrix matrixB, Matrix result, ParallelOperationType operation, double scalar){
		ThreadCenter tc = ThreadCenter.getInstance();
		
		int sublistSize, remainderList, rows, processors, currentRow;
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		rows = getRows();
		processors = tc.getNumProcessors();
		currentRow = 0;
		
		sublistSize = rows/tc.getNumProcessors();
		remainderList = rows%tc.getNumProcessors();
		
		for(int i = 0; i < processors-1; i++){
			ParallelOperation op = new ParallelOperation(operation, this, matrixB, 
					result, currentRow, currentRow+sublistSize, scalar);
			
			currentRow += sublistSize;
			
			futures.add(tc.submitTask(op));
		}
		
		ParallelOperation op = new ParallelOperation(operation, this, matrixB,
				result, currentRow, currentRow+sublistSize+remainderList, scalar);
		
		futures.add(tc.submitTask(op));
		
		return futures;
	}
	
	@Override
	public Matrix add(Matrix matrixB) throws InvalidDimensionException {
		if(getRows() != matrixB.getRows() || getCols() != matrixB.getCols()){
			throw new InvalidDimensionException();
		}

		Matrix result = new SimpleTwoDimensionalMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, ParallelOperationType.ADD, 0);

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

		Matrix result = new SimpleTwoDimensionalMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, ParallelOperationType.SUBTRACT, 0);

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

		Matrix result = new SimpleTwoDimensionalMatrix(getRows(), matrixB.getCols());

		List<Future<?>> futures = setUpThreadedOperation(matrixB, result, ParallelOperationType.MULTIPLY, 0);

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
		Matrix result = new SimpleTwoDimensionalMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(null, result, ParallelOperationType.MULTIPLY_SCALAR, scalar);

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
		Matrix result = new SimpleTwoDimensionalMatrix(getRows(), getCols());

		List<Future<?>> futures = setUpThreadedOperation(null, result, ParallelOperationType.DIVIDE_SCALAR, scalar);

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

	private void swapRows(int rowStart, int rowEnd){
		int cols = getCols();
		for(int i = 0; i < cols; i++){
			double startValue = getElement(rowStart, i);
			double endValue = getElement(rowEnd, i);

			setElement(rowStart, i, endValue);
			setElement(rowEnd, i, startValue);
		}
	}

//	private void subtractRows(int rowSubtractValue, int rowSubtractFrom){
//		int cols = getCols();
//		for(int i = 0; i < cols; i++){
//			double subtractionValue = getElement(rowSubtractValue, i);
//			double subtractionFromValue = getElement(rowSubtractFrom, i);
//
//			setElement(rowSubtractFrom, i, subtractionFromValue - subtractionValue);
//		}
//	}

	private void subtractRows(double[] rowCopy, int rowSubtractFrom){
		int cols = getCols();
		for(int i = 0; i < cols; i++){
			setElement(rowSubtractFrom, i, getElement(rowSubtractFrom, i) - rowCopy[i]);
		}
	}

//	private void multiplyRowByScalar(int row, double scalar){
//		int cols = getCols();
//		for(int i = 0; i < cols; i++){
//			double value = getElement(row, i);
//			setElement(row, i, value * scalar);
//		}
//	}

	private void divideRowByScalar(int row, double scalar){
		int cols = getCols();
		for(int i = 0; i < cols; i++){
			double value = getElement(row, i);
			setElement(row, i, value / scalar);
		}
	}

	private int getRowWithLargestPivotValue(int rowStart, int pivotCol){
		int largestRow;
		int rows = getRows();

		double largestValue;

		largestValue = 0;
		largestRow = -1;

		for(int i = rowStart; i < rows; i++){
			double currentValue = Math.abs(getElement(i, pivotCol));
			if(currentValue > largestValue){
				largestValue = currentValue;
				largestRow = i;
			}
		}

		return largestRow;
	}

	private double[] multiplyRowByScalarAndCopy(int row, double scalar){
		int cols = getCols();
		double[] rowCopy = new double[cols];
		for(int i = 0; i < cols; i++){
			rowCopy[i] = getElement(row, i) * scalar;
		}
		return rowCopy;
	}

//	private double[] divideRowByScalarAndCopy(int row, double scalar){
//		int cols = getCols();
//		double[] rowCopy = new double[cols];
//		for(int i = 0; i < cols; i++){
//			rowCopy[i] = getElement(row, i) / scalar;
//		}
//		return rowCopy;
//	}

	private void neutralizeRow(int row){
		int colIndexOfElement = getColIndexOfNextNonZeroElement(row);
		if(colIndexOfElement > -1){
			double element = getElement(row, colIndexOfElement);
			if(element != 0){
				divideRowByScalar(row, element);
			}
		}
	}

	private void forwardElimination(){
		int rows;

		rows = getRows();
		for(int i = 0; i < rows; i++){
			int colIndex = getColIndexOfNextNonZeroElement(i);
			int chosenPivotRow = getRowWithLargestPivotValue(i, colIndex);

			if(chosenPivotRow > -1){
				if(i != chosenPivotRow){
					//Partial pivoting
					swapRows(i, chosenPivotRow);
				}

				neutralizeRow(i);

				for(int z = i+1; z < rows; z++){
					double eliminationCoefficent = getElement(z, colIndex);
					if(eliminationCoefficent != 0){
						double[] rowCopy = multiplyRowByScalarAndCopy(i, eliminationCoefficent);
						subtractRows(rowCopy, z);
					}
				}
			}
		}
	}

	/**
	 * This method will search the current row and return the index of next element
	 * that is not zero
	 * @param row
	 * @return index of col that has next element that is not zero (from the left). Returns -1
	 * when the whole row is zero
	 */
	private int getColIndexOfNextNonZeroElement(int row){
		int cols = getCols();
		for(int i = 0; i < cols; i++){
			if(getElement(row, i) != 0){
				return i;
			}
		}
		return -1;
	}

	private void backSubtitute(){
		int rows;

		rows = getRows();

		for(int i = rows-1; i > 0; i--){
			int currentColIndex = getColIndexOfNextNonZeroElement(i); 
			if(currentColIndex > -1){
				for(int j = i-1; j >= 0; j--){
					double elementAbove = getElement(j, currentColIndex);
					double[] copy = multiplyRowByScalarAndCopy(i, elementAbove);

					subtractRows(copy, j);
				}
			}
		}

	}

	@Override
	public void gaussianElimination() {
		forwardElimination();
		backSubtitute();
	}

	@Override
	public Matrix transpose() {
		Matrix result = new SimpleTwoDimensionalMatrix(getCols(), getRows());

		List<Future<?>> futures = setUpThreadedOperation(null, result, ParallelOperationType.TRANSPOSE, 0);

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
	public Matrix inverse() throws InvalidDimensionException{
		int rows, cols;
		
		rows = getRows();
		cols = getCols();
		
		if(rows != cols){
			throw new InvalidDimensionException();
		}
		
		//Create Augmented Matrix
		Matrix augmentedMatrix = new SimpleTwoDimensionalMatrix(getRows(), getCols()*2);
		
		for(int i = 0; i < rows; i++){
			for(int j = cols; j < cols * 2; j++){
				if(i == j){
					augmentedMatrix.setElement(i, j, 1);
				}else{
					augmentedMatrix.setElement(i, j, 0);
				}
			}
		}
		
		//Gauss-Jacob Elimination
		augmentedMatrix.gaussianElimination();
		
		//Get augmented matrix again
		Matrix result = new SimpleTwoDimensionalMatrix(rows, cols);
		for(int i = 0; i < rows; i++){
			for (int j = cols; j < cols * 2; j++){
				result.setElement(i, j-cols, augmentedMatrix.getElement(i, j));
			}
		}
		
		return result;
	}

	public Matrix copyMatrix(){
		int rows, cols;
		rows = getRows();
		cols = getCols();
		Matrix copy = new SimpleTwoDimensionalMatrix(rows, cols);
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				copy.setElement(i, j, getElement(i, j));
			}
		}

		return copy;
	}

}
