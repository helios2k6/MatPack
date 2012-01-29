package com.nlogneg.matpack.operations;

public class RunnableWithCallback implements Runnable{
	
	private Runnable runnable;
	
	public RunnableWithCallback(Runnable runnable){
		this.runnable = runnable;
	}
	
	public void run() {
		runnable.run();
		ThreadCenter.getInstance().returnPermit();
	}
}
