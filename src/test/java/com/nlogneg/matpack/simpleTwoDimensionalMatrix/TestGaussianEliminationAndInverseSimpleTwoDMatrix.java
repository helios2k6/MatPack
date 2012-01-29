package com.nlogneg.matpack.simpleTwoDimensionalMatrix;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.simpleTwoDimensionalMatrix.SimpleTwoDimensionalMatrix;

public class TestGaussianEliminationAndInverseSimpleTwoDMatrix {
	private Matrix matrixA;
	private Matrix augmentedMatrix;
	private Matrix expectedResult;
	private Matrix expectedAugmentedMatrixResult;
	
	@Before
	public void init(){
		matrixA = new SimpleTwoDimensionalMatrix(3, 3);
		expectedResult = new SimpleTwoDimensionalMatrix(3, 3);
		augmentedMatrix = new SimpleTwoDimensionalMatrix(3, 6);
		expectedAugmentedMatrixResult = new SimpleTwoDimensionalMatrix(3, 6);
		
		/* 
		 * 8   1   6
		 * 3   5   7
		 * 4   9   2
		 * 
		 * inverse
		 * 0.147222  -0.144444   0.063889
		 * -0.061111   0.022222   0.105556
		 * -0.019444   0.188889  -0.102778
		 */
		
		matrixA.setElement(0, 0, 8);
		matrixA.setElement(0, 1, 1);
		matrixA.setElement(0, 2, 6);
		
		matrixA.setElement(1, 0, 3);
		matrixA.setElement(1, 1, 5);
		matrixA.setElement(1, 2, 7);
		
		matrixA.setElement(2, 0, 4);
		matrixA.setElement(2, 1, 9);
		matrixA.setElement(2, 2, 2);
		
		expectedAugmentedMatrixResult.setElement(0, 3, 0.147222);
		expectedAugmentedMatrixResult.setElement(0, 4, -0.144444);
		expectedAugmentedMatrixResult.setElement(0, 5, 0.063889);
		
		expectedAugmentedMatrixResult.setElement(1, 3, -0.061111);
		expectedAugmentedMatrixResult.setElement(1, 4, 0.022222);
		expectedAugmentedMatrixResult.setElement(1, 5, 0.105556);
		
		expectedAugmentedMatrixResult.setElement(2, 3, -0.019444);
		expectedAugmentedMatrixResult.setElement(2, 4, 0.188889);
		expectedAugmentedMatrixResult.setElement(2, 5, -0.102778);
		
		for(int i = 0; i < matrixA.getRows(); i++){
			for(int j = 0; j < matrixA.getCols(); j++){
				augmentedMatrix.setElement(i, j, matrixA.getElement(i,  j));
				if(i == j){
					expectedAugmentedMatrixResult.setElement(i, j, 1);
				}else{
					expectedAugmentedMatrixResult.setElement(i, j, 0);
				}
			}
		}
		
		for(int i = 0; i < augmentedMatrix.getRows(); i++){
			for(int j = 3; j < augmentedMatrix.getCols(); j++){
				if((i+3) == j){
					augmentedMatrix.setElement(i, j, 1);
				}else{
					augmentedMatrix.setElement(i, j, 0);
				}
			}
		}
		
		for(int i = 0; i < expectedResult.getRows(); i++){
			for(int j = 0; j < expectedResult.getCols(); j++){
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
//		matrixA.gaussianElimination();
		
		assertTrue(matrixA.equals(expectedResult));
		
//		augmentedMatrix.gaussianElimination();
		
		Matrix.printMatrix(augmentedMatrix);
		
		assertTrue(augmentedMatrix.equals(expectedAugmentedMatrixResult));
	}
	
	
	
}
