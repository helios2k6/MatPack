package com.nlogneg.matpack.threadcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadCenter {
	private ExecutorService threadpool;
	
	private int numProcessors;
	
	private ThreadCenter() {
		numProcessors = Runtime.getRuntime().availableProcessors();
		threadpool = Executors.newFixedThreadPool(numProcessors);
	}

	private static class ThreadCenterHolder {
		private static final ThreadCenter INSTANCE = new ThreadCenter();
	}

	public static ThreadCenter getInstance() {
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
