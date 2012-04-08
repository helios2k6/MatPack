package com.nlogneg.matpack.tests;

import java.util.Calendar;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.matrices.StandardMatrix;
import com.nlogneg.matpack.operations.MatrixOperations;

public class MatrixOperationTests {
	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix matrixC;
	private Matrix matrixD;

	private Matrix invariantAdd;
	private Matrix invariantSubtract;
	private Matrix invariantMultiply1;
	private Matrix invariantMultiply2;
	private Matrix invariantMultiply3;

	private Matrix invariantMultiplyScalar;

	private Matrix invariantDivideScalar;

	private Matrix invariantTranspose;

	private double SCALAR_MULTIPLE = 3.0;
	private double SCALAR_DIVIDE = 4.0;

	private int speedTestRowSize = 500;
	private int speedTestColSize = 500;

	@Before
	public void init(){
		matrixA = new StandardMatrix(2, 2);
		matrixB = new StandardMatrix(2, 2);
		matrixC = new StandardMatrix(2, 1);
		matrixD = new StandardMatrix(2, 3);

		matrixA.setElement(0, 0, 1);
		matrixA.setElement(0, 1, 2);
		matrixA.setElement(1, 0, 2);
		matrixA.setElement(1, 1, 2);

		matrixB.setElement(0, 0, 1);
		matrixB.setElement(0, 1, 2);
		matrixB.setElement(1, 0, 2);
		matrixB.setElement(1, 1, 2);

		matrixC.setElement(0, 0, 4);
		matrixC.setElement(1, 0, 1);

		matrixD.setElement(0, 0, 6);
		matrixD.setElement(0, 1, 2);
		matrixD.setElement(0, 2, 1);
		matrixD.setElement(1, 0, 9);
		matrixD.setElement(1, 1, 4);
		matrixD.setElement(1, 2, 3);

		invariantAdd = new StandardMatrix(2, 2); //Test A + B
		invariantAdd.setElement(0, 0, 2);
		invariantAdd.setElement(0, 1, 4);
		invariantAdd.setElement(1, 0, 4);
		invariantAdd.setElement(1, 1, 4);

		invariantSubtract = new StandardMatrix(2, 2); //Test A - B

		invariantMultiply1 = new StandardMatrix(2, 2); //Test A * B
		invariantMultiply1.setElement(0, 0, 5);
		invariantMultiply1.setElement(0, 1, 6);
		invariantMultiply1.setElement(1, 0, 6);
		invariantMultiply1.setElement(1, 1, 8);

		invariantMultiply2 = new StandardMatrix(2, 1); //Test A * C
		invariantMultiply2.setElement(0, 0, 6);
		invariantMultiply2.setElement(1, 0, 10);

		invariantMultiply3 = new StandardMatrix(2, 3); //Test A * D
		invariantMultiply3.setElement(0, 0, 24);
		invariantMultiply3.setElement(0, 1, 10);
		invariantMultiply3.setElement(0, 2, 7);
		invariantMultiply3.setElement(1, 0, 30);
		invariantMultiply3.setElement(1, 1, 12);
		invariantMultiply3.setElement(1, 2, 8);

		invariantMultiplyScalar = new StandardMatrix(2, 2);
		invariantMultiplyScalar.setElement(0, 0, matrixA.getElement(0, 0)*SCALAR_MULTIPLE);
		invariantMultiplyScalar.setElement(0, 1, matrixA.getElement(0, 1)*SCALAR_MULTIPLE);
		invariantMultiplyScalar.setElement(1, 0, matrixA.getElement(1, 0)*SCALAR_MULTIPLE);
		invariantMultiplyScalar.setElement(1, 1, matrixA.getElement(1, 1)*SCALAR_MULTIPLE);

		invariantDivideScalar = new StandardMatrix(2, 2);
		invariantDivideScalar.setElement(0, 0, matrixA.getElement(0, 0)/SCALAR_DIVIDE);
		invariantDivideScalar.setElement(0, 1, matrixA.getElement(0, 1)/SCALAR_DIVIDE);
		invariantDivideScalar.setElement(1, 0, matrixA.getElement(1, 0)/SCALAR_DIVIDE);
		invariantDivideScalar.setElement(1, 1, matrixA.getElement(1, 1)/SCALAR_DIVIDE);

		invariantTranspose = new StandardMatrix(1, 2); //Transpose of C
		invariantTranspose.setElement(0, 0, 4);
		invariantTranspose.setElement(0, 1, 1);
	}

	@Test
	public void testAdd() throws InvalidDimensionException{
		Matrix result = MatrixOperations.add(matrixA, matrixB);
		Assert.assertEquals(result, invariantAdd);
	}

	@Test
	public void testSub() throws InvalidDimensionException{
		Matrix result = MatrixOperations.subtract(matrixA, matrixB);
		Assert.assertEquals(result, invariantSubtract);
	}

	@Test
	public void testMultiply() throws InvalidDimensionException{
		Matrix result1 = MatrixOperations.multiply(matrixA, matrixB);
		Matrix result2 = MatrixOperations.multiply(matrixA, matrixC);
		Matrix result3 = MatrixOperations.multiply(matrixA, matrixD);

		Assert.assertEquals(result1, invariantMultiply1);
		Assert.assertEquals(result2, invariantMultiply2);
		Assert.assertEquals(result3, invariantMultiply3);
	}

	@Test
	public void speedTestDense() throws InvalidDimensionException{
		Random random = new Random(5050);

		//Raw speed test - Dense
		Matrix arrayMatrix = new StandardMatrix(speedTestRowSize, speedTestColSize);

		for(int i = 0; i < speedTestRowSize; i++){
			for(int j = 0; j < speedTestColSize; j++){
				double ranD = random.nextDouble();
				arrayMatrix.setElement(i, j, ranD);
			}
		}

		System.out.println("Matrix Size (dense): " + arrayMatrix.getNumRows() + "x" + arrayMatrix.getNumCols());

		//Calculate addition test
		Calendar beforeArrayTime = Calendar.getInstance();
		MatrixOperations.add(arrayMatrix, arrayMatrix);
		Calendar afterArrayTime = Calendar.getInstance();

		System.out.println("Array Matrix Addition Speed (Dense): " + 
				(afterArrayTime.getTimeInMillis() - beforeArrayTime.getTimeInMillis()) + " milliseconds");

		//Calculate multiplication test
		Calendar beforeArrayTimeMulti = Calendar.getInstance();
		MatrixOperations.multiply(arrayMatrix, arrayMatrix);
		Calendar afterArrayTimeMulti = Calendar.getInstance();

		System.out.println("Array Matrix Multiplication Speed (Dense): " + 
				(afterArrayTimeMulti.getTimeInMillis() - beforeArrayTimeMulti.getTimeInMillis()) + " milliseconds");

		//Calculate Scalar stuff
		//Multiplication
		Calendar beforeArrayTimeMultiScalar = Calendar.getInstance();
		MatrixOperations.multiplyScalar(arrayMatrix, SCALAR_MULTIPLE);
		Calendar afterArrayTimeMultiScalar = Calendar.getInstance();

		System.out.println("Array Matrix Scalar Multiplication Speed (Dense): " + 
				(afterArrayTimeMultiScalar.getTimeInMillis() - beforeArrayTimeMultiScalar.getTimeInMillis()) + " milliseconds");

		//Division
		Calendar beforeArrayTimeDivideScalar = Calendar.getInstance();
		MatrixOperations.divideByScalar(arrayMatrix, SCALAR_DIVIDE);
		Calendar afterArrayTimeDivideScalar = Calendar.getInstance();

		System.out.println("Array Matrix Scalar Division Speed (Dense): " + 
				(afterArrayTimeDivideScalar.getTimeInMillis() - beforeArrayTimeDivideScalar.getTimeInMillis()) + " milliseconds");

		System.out.println();
	}

	@Test
	public void speedTestSparse() throws InvalidDimensionException{
		Random random = new Random(5050);

		//Raw speed test - Dense
		Matrix arrayMatrix = new StandardMatrix(speedTestRowSize, speedTestColSize);

		for(int i = 0; i < speedTestRowSize; i++){
			for(int j = 0; j < speedTestColSize; j++){
				double ranD = random.nextDouble();
				arrayMatrix.setElement(i, j, ranD);
			}
		}

		System.out.println("Matrix Size (sparse): " + arrayMatrix.getNumRows() + "x" + arrayMatrix.getNumCols());

		//Calculate addition test
		Calendar beforeArrayTime = Calendar.getInstance();
		MatrixOperations.add(arrayMatrix, arrayMatrix);
		Calendar afterArrayTime = Calendar.getInstance();

		System.out.println("Array Matrix Addition Speed (Sparse): " + (afterArrayTime.getTimeInMillis() - beforeArrayTime.getTimeInMillis()) + " milliseconds");

		//Calculate multiplication test
		Calendar beforeArrayTimeMulti = Calendar.getInstance();
		MatrixOperations.multiply(arrayMatrix, arrayMatrix);
		Calendar afterArrayTimeMulti = Calendar.getInstance();

		System.out.println("Array Matrix Multiplication Speed (Sparse): " + 
				(afterArrayTimeMulti.getTimeInMillis() - beforeArrayTimeMulti.getTimeInMillis()) + " milliseconds");

		//Calculate Scalar stuff
		//Multiplication
		Calendar beforeArrayTimeMultiScalar = Calendar.getInstance();
		MatrixOperations.multiplyScalar(arrayMatrix, SCALAR_MULTIPLE);
		Calendar afterArrayTimeMultiScalar = Calendar.getInstance();

		System.out.println("Array Matrix Scalar Multiplication Speed (Sparse): " + 
				(afterArrayTimeMultiScalar.getTimeInMillis() - beforeArrayTimeMultiScalar.getTimeInMillis()) + " milliseconds");

		//Division
		Calendar beforeArrayTimeDivideScalar = Calendar.getInstance();
		MatrixOperations.divideByScalar(arrayMatrix, SCALAR_DIVIDE);
		Calendar afterArrayTimeDivideScalar = Calendar.getInstance();

		System.out.println("Array Matrix Scalar Division Speed (Sparse): " + 
				(afterArrayTimeDivideScalar.getTimeInMillis() - beforeArrayTimeDivideScalar.getTimeInMillis()) + " milliseconds");

		System.out.println();
	}

	@Test
	public void testScalarOperations(){
		Matrix resultM = MatrixOperations.multiplyScalar(matrixA, SCALAR_MULTIPLE);
		Matrix resultD = MatrixOperations.divideByScalar(matrixA, SCALAR_DIVIDE);

		Assert.assertEquals(resultM, invariantMultiplyScalar);
		Assert.assertEquals(resultD, invariantDivideScalar);
	}

	@Test
	public void testTranspose(){
		Matrix resultT = MatrixOperations.transpose(matrixC);
		Assert.assertEquals(resultT, invariantTranspose);
	}

	@Test
	public void testRef(){
		System.out.println("Result Original [REF]: " + matrixD);
		Matrix result = MatrixOperations.refMatrix(matrixD);
		System.out.println(result);
	}

	@Test
	public void testRref() throws Exception{
		System.out.println("Result Original [RREF]: " + matrixD);
		Matrix result = MatrixOperations.rrefMatrix(matrixD);
		System.out.println(result);
	}

	@Test
	public void testRrefSpeed() throws Exception{
		Random random = new Random(5050);

		//Raw speed test
		Matrix arrayMatrix = new StandardMatrix(speedTestRowSize, speedTestColSize);

		System.out.println("Testing RREF on " + arrayMatrix.getNumRows() + "x" + arrayMatrix.getNumCols() + " Matrix");
		for(int i = 0; i < speedTestRowSize; i++){
			for(int j = 0; j < speedTestColSize; j++){
				double ranD = random.nextDouble();
				arrayMatrix.setElement(i, j, ranD);
			}
		}
		
		Calendar before = Calendar.getInstance();
		MatrixOperations.rrefMatrix(arrayMatrix);
		Calendar after = Calendar.getInstance();

		System.out.println("RREF Speed: " + (after.getTimeInMillis() - before.getTimeInMillis()) + " milliseconds");
	}
}
