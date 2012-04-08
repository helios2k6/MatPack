package com.nlogneg.matpack.operations;

import com.nlogneg.matpack.Matrix;

public class StandardThreadedOperation implements Runnable{

	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix result;

	private int rowStart;
	private int rowEnd;

	private MatrixOperationEnum operation;

	/**
	 * 
	 * @param matrixA
	 * @param matrixB
	 * @param result
	 * @param rowStart The row this operation will start operating on
	 * @param rowEnd The row AFTER the last row this operation will operate on  
	 * @param operation
	 */
	public StandardThreadedOperation(Matrix matrixA,
			Matrix matrixB,
			Matrix result,
			int rowStart,
			int rowEnd, 
			MatrixOperationEnum operation){

		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.result = result;

		this.rowStart = rowStart;
		this.rowEnd = rowEnd;

		this.operation = operation;

	}

	private void add(){
		for(int i = rowStart; i < rowEnd; i++){
			for(int j = 0; j < matrixA.getNumCols(); j++){
				double doubleResult = matrixA.getElement(i, j) + matrixB.getElement(i, j);
				result.setElement(i, j, doubleResult);
			}
		}
	}

	private void subtract(){
		for(int i = rowStart; i < rowEnd; i++){
			for(int j = 0; j < matrixA.getNumCols(); j++){
				double doubleResult = matrixA.getElement(i, j) - matrixB.getElement(i, j);
				result.setElement(i, j, doubleResult);
			}
		}
	}

	private void multiply(){
		for(int i = rowStart; i < rowEnd; i++){
			for(int j = 0; j < result.getNumCols(); j++){
				double runningSum = 0;

				for(int k = 0; k < matrixB.getNumRows(); k++){
					double matrixADouble = matrixA.getElement(i, k);
					double matrixBDouble = matrixB.getElementUsingColMajor(k, j);

					runningSum += matrixADouble * matrixBDouble;
				}

				result.setElement(i, j, runningSum);
			}
		}
	}

	@Override
	public void run() {
		try {
			TicketMaster.getInstance().GetTicket();
			switch(operation){
			case ADD:
				add();
				break;
			case SUBTRACT:
				subtract();
				break;
			case MULTIPLY:
				multiply();
				break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			TicketMaster.getInstance().ReplaceTicket();
		}
	}

}
