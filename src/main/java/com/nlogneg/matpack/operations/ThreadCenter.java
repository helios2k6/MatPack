package com.nlogneg.matpack.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;


public class ThreadCenter{
	private final int numProcessors;
	
	private Semaphore semaphore;
	
	private ThreadCenter() {
		numProcessors = Runtime.getRuntime().availableProcessors();
		semaphore = new Semaphore(numProcessors + 2, true);
	}

	private static class ThreadCenterHolder {
		private static final ThreadCenter INSTANCE = new ThreadCenter();
	}

	public static ThreadCenter getInstance() {
		return ThreadCenterHolder.INSTANCE;
	}
	
	public List<Thread> submit(Collection<RunnableWithCallback> runnables) throws InterruptedException{
		List<Thread> threads = new ArrayList<Thread>();
		for(RunnableWithCallback r : runnables){
			threads.add(submit(r));
		}
		
		return threads;
	}
	
	public Thread submit(RunnableWithCallback r) throws InterruptedException{
		semaphore.acquire();
		Thread t = new Thread(r);
		t.start();
		
		return t;
	}
	
	public void returnPermit(){
		semaphore.release();
	}
	
	public int getNumProcessors(){
		return numProcessors;
	}

}
