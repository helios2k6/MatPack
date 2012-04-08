package com.nlogneg.matpack.operations;

import com.nlogneg.matpack.Matrix;

public class TransposeThreadedOperation implements Runnable{

	private Matrix matrixA;
	private Matrix result;

	private int rowStart;
	private int rowEnd;

	public TransposeThreadedOperation(Matrix matrixA, Matrix result, int rowStart, int rowEnd) {
		this.matrixA = matrixA;
		this.result = result;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
	}

	private void transpose(){
		for(int i = rowStart; i < rowEnd; i++){
			for(int j = 0; j < matrixA.getNumCols(); j++){
				result.setElement(j, i, matrixA.getElement(i, j));
			}
		}
	}

	@Override
	public void run() {
		try {
			TicketMaster.getInstance().GetTicket();
			transpose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			TicketMaster.getInstance().ReplaceTicket();
		}
	}
}
