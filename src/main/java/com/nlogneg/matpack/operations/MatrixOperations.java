package com.nlogneg.matpack.operations;

import java.util.List;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;

public class MatrixOperations {
	
	private static List<MultithreadedAddSubtractOperation> generateAddOperations(Matrix a, Matrix b, Matrix result){
		return null;
	}
	
	private static List<MultithreadedAddSubtractOperation> generateSubtractOperations(Matrix a, Matrix b, Matrix result){
		return null;
	}
	
	private static List<MultithreadedMultiplyOperation> generateMultiplyOperations(Matrix a, Matrix b, Matrix result){
		return null;
	}
	
	private static boolean checkAdditionAndSubtractionDimenions(Matrix a, Matrix b){
		return (a.getNumRows() == b.getNumRows()) && (a.getNumCols() == b.getNumCols());
	}

	private static boolean checkMultiplicationDimensions(Matrix a, Matrix b){
		return a.getNumCols() == b.getNumRows();
	}

	private static boolean checkInverseDimensions(Matrix a){
		return a.getNumRows() == a.getNumCols();
	}

	public static Matrix add(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkAdditionAndSubtractionDimenions(a, b)){
			throw new InvalidDimensionException();
		}
		
		return null;
	}

	public static Matrix subtract(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkAdditionAndSubtractionDimenions(a, b)){
			throw new InvalidDimensionException();
		}
		return null;
	}

	public static Matrix multiply(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkMultiplicationDimensions(a, b)){
			throw new InvalidDimensionException();
		}
		return null;
	}

	public static Matrix multiplyScalar(Matrix a, double scalar){
		return null;		
	}

	public static Matrix divideByScalar(Matrix a, double scalar){
		return null;
	}

	public static Matrix performGaussianElimination(Matrix a){
		return null;
	}

	public static Matrix transpose(Matrix a){
		return null;
	}
	
	public static Matrix inverse(Matrix a) throws InvalidDimensionException{
		if(!checkInverseDimensions(a)){
			throw new InvalidDimensionException();
		}
		return null;
	}
	

}