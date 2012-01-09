package com.nlogneg.matpack;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.simpleTwoDimensionalMatrix.SimpleTwoDimensionalMatrix;
import com.nlogneg.matpack.threadcenter.ThreadCenter;

public class TestBasicOperations {
	
	private static final Random random = new Random();
	
	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix result;
	private Matrix invariantResultAdd;
	private Matrix invariantResultSubtract;
	private Matrix invariantResultMultiplication;
	
	private Matrix matrixC;
	private Matrix matrixD;
	private Matrix invariantResultMultiplicationNonSquare;
	
	private Matrix matrixE;
	private Matrix matrixF;
	private Matrix invariantResultMultiEF;
	
	private Matrix matrixG;
	private Matrix invariantScalarMulti;
	private double scalarMulti;
	
	private Matrix matrixH;
	private Matrix invariantScalarDivide;
	private double scalarDivide;
	
	private Matrix matrixI;
	private Matrix invariantResultTrans;
	

	@Before
	public void init(){
		matrixA = new SimpleTwoDimensionalMatrix(5, 5);
		matrixB = new SimpleTwoDimensionalMatrix(5, 5);
		invariantResultAdd = new SimpleTwoDimensionalMatrix(5, 5);
		invariantResultSubtract = new SimpleTwoDimensionalMatrix(5, 5);
		invariantResultMultiplication = new SimpleTwoDimensionalMatrix(5,5);
		
		matrixC = new SimpleTwoDimensionalMatrix(3, 2);
		matrixD = new SimpleTwoDimensionalMatrix(2, 1);
		invariantResultMultiplicationNonSquare = new SimpleTwoDimensionalMatrix(3, 1);
		
		matrixE = new SimpleTwoDimensionalMatrix(1, 3);
		matrixF = new SimpleTwoDimensionalMatrix(3, 1);
		invariantResultMultiEF = new SimpleTwoDimensionalMatrix(1, 1);
		
		matrixG = new SimpleTwoDimensionalMatrix(4, 4);
		invariantScalarMulti = new SimpleTwoDimensionalMatrix(4, 4);
		scalarMulti = 13.0;
		
		matrixH = new SimpleTwoDimensionalMatrix(3, 3);
		invariantScalarDivide = new SimpleTwoDimensionalMatrix(3, 3);
		scalarDivide = 4.0;
		
		matrixI = new SimpleTwoDimensionalMatrix(4, 7);
		invariantResultTrans = new SimpleTwoDimensionalMatrix(7, 4);
		
		for(int i = 0; i < matrixG.getRows(); i++){
			for(int j = 0; j < matrixG.getCols(); j++){
				matrixG.setElement(i, j, 5);
				invariantScalarMulti.setElement(i, j, 5 * 13);
			}
		}
		
		for(int i = 0; i < matrixH.getRows(); i++){
			for(int j = 0; j < matrixH.getCols(); j++){
				matrixH.setElement(i, j, 6.0);
				invariantScalarDivide.setElement(i, j, 6.0/4.0);
			}
		}
		
		for(int i = 0; i < matrixI.getRows(); i++){
			for(int j = 0; j < matrixI.getCols(); j++){
				double randDouble = random.nextDouble();
				matrixI.setElement(i, j, randDouble);
				invariantResultTrans.setElement(j, i, randDouble);
			}
		}
		
		for(int i = 0; i < matrixA.getRows(); i++){
			for(int j = 0; j < matrixA.getCols(); j++){
				matrixA.setElement(i, j, 5.0);
				matrixB.setElement(i, j, 3.0);
				invariantResultAdd.setElement(i, j, 8.0);
				invariantResultSubtract.setElement(i, j, 2.0);
				invariantResultMultiplication.setElement(i, j, 75.0);
				
			}
		}
		
		for(int i = 0; i < matrixC.getRows(); i++){
			for(int j = 0; j < matrixC.getCols(); j++){
				matrixC.setElement(i, j,  3.0);
			}
		}
		
		for(int i = 0; i < matrixD.getRows(); i++){
			for(int j = 0; j < matrixD.getCols(); j++){
				matrixD.setElement(i, j, 7.0);
			}
		}
		
		for(int i = 0; i < invariantResultMultiplicationNonSquare.getRows(); i++){
			for(int j = 0; j < invariantResultMultiplicationNonSquare.getCols(); j++){
				invariantResultMultiplicationNonSquare.setElement(i, j, 42.0);
			}
		}
		
		for(int i = 0; i < matrixE.getRows(); i++){
			for(int j = 0; j < matrixE.getCols(); j++){
				matrixE.setElement(i, j, 6.0);
			}
		}
		
		for(int i = 0; i < matrixF.getRows(); i++){
			for(int j = 0; j < matrixF.getCols(); j++){
				matrixF.setElement(i, j, 9.0);
			}
		}
		
		invariantResultMultiEF.setElement(0, 0, 162.0);
	}

	@Test
	public void testAddition(){
		try {
			result = matrixA.add(matrixB);
		} catch (InvalidDimensionException e) {
			fail();
		}

		assertTrue(result.equals(invariantResultAdd));
	}

	@Test
	public void testSubtraction(){
		try {
			result = matrixA.subtract(matrixB);
		} catch (InvalidDimensionException e) {
			fail();
		}

		assertTrue(result.equals(invariantResultSubtract));
	}

	@Test
	public void testMultiplication(){
		try {
			result = matrixA.multiply(matrixB);
		} catch (InvalidDimensionException e) {
			fail();
		}
		
		assertTrue(result.equals(invariantResultMultiplication));
	}

	@Test
	public void testMultiplicationNonSquare(){
		try {
			result = matrixC.multiply(matrixD);
		} catch (InvalidDimensionException e) {
			fail();
		}
		
		assertTrue(result.equals(invariantResultMultiplicationNonSquare));
		
		try {
			result = matrixE.multiply(matrixF);
		} catch (InvalidDimensionException e) {
			fail();
		}
		
		assertTrue(result.equals(invariantResultMultiEF));
		
	}
	
	@Test
	public void testScalars(){
		result = matrixG.scalarMultiply(scalarMulti);
		assertTrue(result.equals(invariantScalarMulti));
		
		result = matrixH.scalarDivide(scalarDivide);
		assertTrue(result.equals(invariantScalarDivide));
	}
	
	@Test
	public void testTranspose(){
		result = matrixI.transpose();
		
		assertTrue(result.equals(invariantResultTrans));
	}
	
	@AfterClass
	public static void shutdown(){
		ThreadCenter.getInstance().shutdown();
	}
}
