package com.nlogneg.matpack.gauss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;
import com.nlogneg.matpack.operations.MatrixOperations;
import com.nlogneg.matpack.operations.TicketMaster;

public class SingleThreadedSolver implements GaussSolver {

	private List<Runnable> generateOperations(
			Matrix a,
			int numElements,
			int numProcessors){

		int predominateSublistSize = numElements / numProcessors;

		//Don't have enough elements for each processor, just going to default to 
		//single threaded
		if(predominateSublistSize < 1){
			numProcessors = 1;
			predominateSublistSize = numElements;
		}

		//This is where we load balance properly
		//Calculate how many excess elements the last thread would have to process
		int numExcessElements = (a.getNumRows() - (numProcessors - 1) * predominateSublistSize) - predominateSublistSize - 1;

		List<Runnable> operations = new ArrayList<Runnable>();

		int lastEndingIndex = 0;

		for(int i = 0; i < numProcessors - 1; i++){
			int sizeOfCurrentList = predominateSublistSize;

			//Add extra element to this current list 
			if(numExcessElements > 0){
				sizeOfCurrentList++;
				numExcessElements--;
			}

			InternalBacksubber op = new InternalBacksubber(
					a, 
					lastEndingIndex, 
					lastEndingIndex + sizeOfCurrentList);

			lastEndingIndex += sizeOfCurrentList;

			operations.add(op);
		}

		//Correct last operation
		InternalBacksubber op = new InternalBacksubber(
				a, 
				(numProcessors - 1) * predominateSublistSize, 
				a.getNumRows());

		operations.add(op);

		return operations;
	}

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
		long before = Calendar.getInstance().getTimeInMillis();
		List<Thread> threads = new ArrayList<Thread>();
		
		List<Runnable> ops = generateOperations(a, a.getNumRows(), MatrixOperations.NUMBER_OF_PROCESSORS);

		for(Runnable r : ops){
			Thread t = new Thread(r);
			threads.add(t);
			t.start();
		}
		
		for(Thread t : threads){
			t.join();
		}

		long after = Calendar.getInstance().getTimeInMillis();

		System.out.println("Spent " + (after - before) + "ms in BackSub");
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
		long before = Calendar.getInstance().getTimeInMillis();
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
		long after = Calendar.getInstance().getTimeInMillis();

		System.out.println("Spent " + (after - before) + "ms in Row Reduction");
	}

	private class InternalBacksubber implements Runnable{

		private Matrix matrix;
		private int rowStart;
		private int rowEnd;

		public InternalBacksubber(Matrix matrix, int rowStart, int rowEnd) {
			this.matrix = matrix;
			this.rowStart = rowStart;
			this.rowEnd = rowEnd;
		}

		private void backSubOp(){
			for(int i = rowStart; i < rowEnd; i++){
				int pivotCol = detectPivotCol(matrix, i);
				neutralizeRowsAbove(matrix, i, pivotCol);
				divideRowByScalar(matrix, i, matrix.getElement(i, pivotCol));
			}
		}

		@Override
		public void run() {
			try {
				TicketMaster.getInstance().GetTicket();
				backSubOp();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally{
				TicketMaster.getInstance().ReplaceTicket();
			}
		}

	}

}
