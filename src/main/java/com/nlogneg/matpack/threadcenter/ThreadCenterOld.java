package com.nlogneg.matpack.threadcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.operations.SimpleParallelOperation;
import com.nlogneg.matpack.operations.ParallelOperationType;

public class ThreadCenterOld {
	private ExecutorService threadpool;
	
	private int numProcessors;
	
	public static final List<Future<?>> setUpSimpleThreadedOperation(Matrix matrixA, Matrix matrixB, Matrix result, ParallelOperationType operation, double scalar){
		ThreadCenterOld tc = ThreadCenterOld.getInstance();
		
		int sublistSize, remainderList, rows, processors, currentRow;
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		rows = matrixA.getRows();
		processors = tc.getNumProcessors();
		currentRow = 0;
		
		sublistSize = rows/tc.getNumProcessors();
		remainderList = rows%tc.getNumProcessors();
		
		for(int i = 0; i < processors-1; i++){
			SimpleParallelOperation op = new SimpleParallelOperation(operation, matrixA, matrixB, 
					result, currentRow, currentRow+sublistSize, scalar);
			
			currentRow += sublistSize;
			
			futures.add(tc.submitTask(op));
		}
		
		SimpleParallelOperation op = new SimpleParallelOperation(operation, matrixA, matrixB,
				result, currentRow, currentRow+sublistSize+remainderList, scalar);
		
		futures.add(tc.submitTask(op));
		
		return futures;
	}
	
	private ThreadCenterOld() {
		numProcessors = Runtime.getRuntime().availableProcessors();
//		threadpool = Executors.newFixedThreadPool(numProcessors+1);
	}

	private static class ThreadCenterHolder {
		private static final ThreadCenterOld INSTANCE = new ThreadCenterOld();
	}

	public static ThreadCenterOld getInstance() {
		return ThreadCenterHolder.INSTANCE;
	}
	
	public Future<?> submitTask(Runnable runnable){
		return threadpool.submit(runnable);
	}
	
	public int getNumProcessors(){
		return numProcessors;
	}
	
	public void shutdown(){
		threadpool.shutdown();
	}
	
}
