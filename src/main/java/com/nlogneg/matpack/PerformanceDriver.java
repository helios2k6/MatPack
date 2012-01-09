package com.nlogneg.matpack;

import java.util.Random;

import org.joda.time.DateTime;

import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.simplematrix.SimpleTwoDimensionalMatrix;
import com.nlogneg.matpack.threadcenter.ThreadCenter;

public class PerformanceDriver {
	public static final int MATRIX_SIZE = 5000;
	private Random random;

	private Matrix matrixA;
	private Matrix matrixB;
	
	public static void main(String[] args){
		System.out.println("Starting performance test with " + MATRIX_SIZE + " x " + MATRIX_SIZE);
		PerformanceDriver driver = new PerformanceDriver();
		//driver.generateRandomMatrices();
		//driver.performanceTest();
		
		driver.performRowAccessToColAccess();
		
		ThreadCenter.getInstance().shutdown();
	}
	
	public PerformanceDriver(){
		init();
	}
	
	public void init(){
		random = new Random();
		matrixA = new SimpleTwoDimensionalMatrix(MATRIX_SIZE, MATRIX_SIZE);
		matrixB = new SimpleTwoDimensionalMatrix(MATRIX_SIZE, MATRIX_SIZE);
	}
	
	public void generateRandomMatrices(){
		for(int i = 0; i < MATRIX_SIZE; i++){
			for(int j = 0; j < MATRIX_SIZE; j++){
				matrixA.setElement(i,  j, random.nextDouble());
				matrixB.setElement(i,  j, random.nextDouble());
			}
		}
	}

	public void performanceTest(){
		try {
			DateTime before = new DateTime();
			matrixA.multiply(matrixB);
			DateTime after = new DateTime();
			
			long beforeAsLong = before.getMillis();
			long afterAsLong = after.getMillis();
			
			System.out.println("Took " + (afterAsLong - beforeAsLong) + " milliseconds to multiply " 
					+ MATRIX_SIZE + " x " + MATRIX_SIZE);

		} catch (InvalidDimensionException e) {
		}
	}
	
	public void performRowAccessToColAccess(){
		System.out.println("Starting access time checks");
		Matrix matrix = new SimpleTwoDimensionalMatrix(MATRIX_SIZE, MATRIX_SIZE);
		
		for(int i = 0; i < MATRIX_SIZE; i++){
			for(int j = 0; j < MATRIX_SIZE; j++){
				matrix.setElement(i, j, random.nextDouble());
			}
		}
		
		DateTime beforeRowMajorAccess = new DateTime();
		for(int i = 0; i < MATRIX_SIZE; i++){
			for(int j = 0; j < MATRIX_SIZE; j++){
				double d = matrix.getElement(i, j);
				d = d + 1;
			}
		}
		DateTime afterRowMajorAccess = new DateTime();
		
		DateTime beforeColMajorAccess = new DateTime();
		for(int i = 0; i < MATRIX_SIZE; i++){
			for(int j = 0; j < MATRIX_SIZE; j++){
				double d = matrix.getElement(j, i);
				d = d + 1;
			}
		}
		DateTime afterColMajorAccess = new DateTime();
		
		long beforeRow = beforeRowMajorAccess.getMillis();
		long afterRow = afterRowMajorAccess.getMillis();
		
		long beforeCol = beforeColMajorAccess.getMillis();
		long afterCol = afterColMajorAccess.getMillis();
		
		System.out.println("Row Major access time took: " + (afterRow - beforeRow) + " milliseconds for " + MATRIX_SIZE + " x " + MATRIX_SIZE);
		System.out.println("Column Major access time took: " + (afterCol - beforeCol) + " milliseconds for " + MATRIX_SIZE + " x " + MATRIX_SIZE);
	}
}
