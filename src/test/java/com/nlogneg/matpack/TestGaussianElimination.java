package com.nlogneg.matpack;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nlogneg.matpack.simplematrix.SimpleMatrix;

public class TestGaussianElimination {
	private Matrix matrixA;
	private Matrix expectedResult;
	
	@Before
	public void init(){
		matrixA = new SimpleMatrix(3, 3);
		expectedResult = new SimpleMatrix(3, 3);
		/* 
		 * 8   1   6
		 * 3   5   7
		 * 4   9   2
		 */
		
		matrixA.setElement(0, 0, 8);
		matrixA.setElement(0, 1, 1);
		matrixA.setElement(0, 2, 6);
		
		matrixA.setElement(1, 0, 3);
		matrixA.setElement(1, 1, 5);
		matrixA.setElement(1, 2, 7);
		
		matrixA.setElement(2, 0, 4);
		matrixA.setElement(2, 1, 9);
		matrixA.setElement(2, 1, 2);
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(i != j){
					expectedResult.setElement(i, j, 0);
				}else{
					expectedResult.setElement(i, j, 1);
				}
			}
		}
		
	}
	
	@Test
	public void testGaussianElimination(){
		matrixA.gaussianElimination();
		
		assertTrue(matrixA.equals(expectedResult));
	}
	
	
	
}
