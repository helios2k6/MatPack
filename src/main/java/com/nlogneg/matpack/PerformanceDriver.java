package com.nlogneg.matpack;

import java.util.Random;

import org.joda.time.DateTime;

import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.simpleTwoDimensionalMatrix.SimpleTwoDimensionalMatrix;

public class PerformanceDriver {
	public static final int MATRIX_SIZE = 1414;
	private static final Random random = new Random();

	public static void main(String[] args) throws InvalidDimensionException{
		testMultiplication();

		performMatrixAccessTest();
	}

	private static void performMatrixAccessTest() {
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

	private static void testMultiplication() throws InvalidDimensionException {
		Matrix matrixA = new SimpleTwoDimensionalMatrix(MATRIX_SIZE, MATRIX_SIZE);
		Matrix matrixB = new SimpleTwoDimensionalMatrix(MATRIX_SIZE, MATRIX_SIZE);
		
		for(int i = 0; i < MATRIX_SIZE; i++){
			for(int j = 0; j < MATRIX_SIZE; j++){
				matrixA.setElement(i,  j, random.nextDouble());
				matrixB.setElement(i,  j, random.nextDouble());
			}
		}
		System.out.println("Starting multiplication test with " + MATRIX_SIZE + " x " + MATRIX_SIZE);
		DateTime before = new DateTime();
		matrixA.multiply(matrixB);
		DateTime after = new DateTime();

		long beforeAsLong = before.getMillis();
		long afterAsLong = after.getMillis();

		System.out.println("Took " + (afterAsLong - beforeAsLong) + " milliseconds to multiply " 
				+ MATRIX_SIZE + " x " + MATRIX_SIZE);
	}
}
