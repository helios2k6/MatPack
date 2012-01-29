package com.nlogneg.matpack.sparseHashMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.common.IntegerTwoTuple;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.exceptions.MatrixOutOfBoundsException;
import com.nlogneg.matpack.operations.ParallelOperationType;
import com.nlogneg.matpack.threadcenter.ThreadCenterOld;

public class SparseHashMatrix extends Matrix {
	
	public static final int WORK_ITEM_THRESHOLD = 20;
	
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
		int numRows = getRows();
		int numCols = getCols();
		
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

	public Matrix add(Matrix matrixB) throws InvalidDimensionException {
		if(!checkAdditionAndSubtractionDimenions(matrixB)){
			throw new InvalidDimensionException();
		}

		Matrix result = new SparseHashMatrix(getRows(), getCols());

		List<Future<?>> futures = ThreadCenterOld.setUpSimpleThreadedOperation(this, matrixB, result, ParallelOperationType.ADD, 0);

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

	public Matrix subtract(Matrix matrixB) throws InvalidDimensionException {
		if(!checkAdditionAndSubtractionDimenions(matrixB)){
			throw new InvalidDimensionException();
		}

		Matrix result = new SparseHashMatrix(getRows(), getCols());

		List<Future<?>> futures =  ThreadCenterOld.setUpSimpleThreadedOperation(this, matrixB, result, ParallelOperationType.SUBTRACT, 0);

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
	
	public Matrix multiply(Matrix matrixB) throws InvalidDimensionException {
		if(!checkMultiplicationDimensions(matrixB)){
			throw new InvalidDimensionException();
		}
		
		Set<IntegerTwoTuple> coords = getTuples();
		Map<Integer, Set<IntegerTwoTuple>> segregationByRow = new HashMap<Integer, Set<IntegerTwoTuple>>();
		
		for(IntegerTwoTuple it : coords){
			int currentRow = it.getRow();
			
			Set<IntegerTwoTuple> row = segregationByRow.get(currentRow);
			if(row == null){
				row = new HashSet<IntegerTwoTuple>();
			}
			
			row.add(it);
			
			segregationByRow.put(currentRow, row);
		}
		
		Matrix result = new SparseHashMatrix(getRows(), matrixB.getCols());
		
		Set<Integer> keychain = segregationByRow.keySet();
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		for(Integer i : keychain){
			SparseMatrixParallelOperation parallelOp = new SparseMatrixParallelOperation(segregationByRow.get(i), this, 
					matrixB, result, ParallelOperationType.MULTIPLY, 0.0, i);
			
			futures.add(ThreadCenterOld.getInstance().submitTask(parallelOp));
		}
		
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

	public Matrix scalarMultiply(double scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix scalarDivide(double scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	public void gaussianElimination() {
		// TODO Auto-generated method stub
		
	}

	public Matrix transpose() {
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix inverse() throws InvalidDimensionException {
		// TODO Auto-generated method stub
		return null;
	}

}
