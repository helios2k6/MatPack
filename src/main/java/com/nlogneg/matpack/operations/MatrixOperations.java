package com.nlogneg.matpack.operations;

import java.util.ArrayList;
import java.util.List;

import com.nlogneg.matpack.Matrix;
import com.nlogneg.matpack.exceptions.InvalidDimensionException;
import com.nlogneg.matpack.matrices.StandardMatrix;

public class MatrixOperations {

	public static final int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();

	private static List<Runnable> generateOperations(
			Matrix a, 
			Matrix b, 
			Matrix result, 
			MatrixOperationEnum operation, 
			int numElements,
			int numProcessors){

		int predominateSublistSize = numElements / numProcessors;
		
		//Don't have enough elements for each processor, just going to default to 
		//single threaded
		if(predominateSublistSize < 1){
			numProcessors = 1;
			predominateSublistSize = numElements;
		}
		
		//This is where we load balance properly
		//Calculate how many excess elements the last thread would have to process
		int numExcessElements = (a.getNumRows() - (numProcessors - 1) * predominateSublistSize) - predominateSublistSize - 1;
		
		List<Runnable> operations = new ArrayList<Runnable>();
		
		int lastEndingIndex = 0;

		for(int i = 0; i < numProcessors - 1; i++){
			int sizeOfCurrentList = predominateSublistSize;
			
			//Add extra element to this current list 
			if(numExcessElements > 0){
				sizeOfCurrentList++;
				numExcessElements--;
			}
			
			StandardThreadedOperation op = new StandardThreadedOperation(
					a, 
					b, 
					result, 
					lastEndingIndex, 
					lastEndingIndex + sizeOfCurrentList, 
					operation);

			lastEndingIndex += sizeOfCurrentList;
			
			operations.add(op);
		}

		//Correct last operation
		StandardThreadedOperation op = new StandardThreadedOperation(
				a, 
				b, 
				result, 
				(numProcessors - 1) * predominateSublistSize, 
				a.getNumRows(),
				operation);

		operations.add(op);

		return operations;
	}
	
	private static List<Runnable> generateScalarOperations(
			Matrix a, 
			Matrix result, 
			double scalar,
			MatrixOperationEnum operation, 
			int numElements,
			int numProcessors){

		int predominateSublistSize = numElements / numProcessors;
		
		//Don't have enough elements for each processor, just going to default to 
		//single threaded
		if(predominateSublistSize < 1){
			numProcessors = 1;
			predominateSublistSize = numElements;
		}
		
		//This is where we load balance properly
		//Calculate how many excess elements the last thread would have to process
		int numExcessElements = (a.getNumRows() - (numProcessors - 1) * predominateSublistSize) - predominateSublistSize - 1;
		
		List<Runnable> operations = new ArrayList<Runnable>();
		
		int lastEndingIndex = 0;

		for(int i = 0; i < numProcessors - 1; i++){
			int sizeOfCurrentList = predominateSublistSize;
			
			//Add extra element to this current list 
			if(numExcessElements > 0){
				sizeOfCurrentList++;
				numExcessElements--;
			}
			
			ScalarThreadedOperation op = new ScalarThreadedOperation(
					a, 
					result, 
					scalar,
					lastEndingIndex, 
					lastEndingIndex + sizeOfCurrentList, 
					operation);

			lastEndingIndex += sizeOfCurrentList;
			
			operations.add(op);
		}

		//Correct last operation
		ScalarThreadedOperation op = new ScalarThreadedOperation(
				a, 
				result, 
				scalar,
				(numProcessors - 1) * predominateSublistSize, 
				a.getNumRows(),
				operation);

		operations.add(op);

		return operations;
	}

	private static List<Runnable> generateAddOperations(Matrix a, Matrix b, Matrix result){
		return generateOperations(a, b, result, MatrixOperationEnum.ADD, a.getNumRows(), NUMBER_OF_PROCESSORS);
	}

	private static List<Runnable> generateSubtractOperations(Matrix a, Matrix b, Matrix result){
		return generateOperations(a, b, result, MatrixOperationEnum.SUBTRACT, a.getNumRows(), NUMBER_OF_PROCESSORS);
	}

	private static List<Runnable> generateMultiplicationOperations(Matrix a, Matrix b, Matrix result){
		return generateOperations(a, b, result, MatrixOperationEnum.MULTIPLY, result.getNumRows(), NUMBER_OF_PROCESSORS);
	}
	
	private static List<Runnable> generateScalarMultiplicationOperations(Matrix a, Matrix result, double scalar){
		return generateScalarOperations(a, result, scalar, MatrixOperationEnum.MULTIPLY_SCALAR, a.getNumRows(), NUMBER_OF_PROCESSORS);
	}
	
	private static List<Runnable> generateScalarDivisionOperations(Matrix a, Matrix result, double scalar){
		return generateScalarOperations(a, result, scalar, MatrixOperationEnum.DIVIDE_SCALAR, a.getNumRows(), NUMBER_OF_PROCESSORS);
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

	private static void executeThreadedOps(List<Runnable> operations){
		List<Thread> threads = new ArrayList<Thread>();
		//Start all threads
		for(Runnable op : operations){
			Thread t = new Thread(op);
			t.start();
			threads.add(t);
		}

		//Join on threads 
		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Matrix add(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkAdditionAndSubtractionDimenions(a, b)){
			throw new InvalidDimensionException();
		}

		Matrix result = new StandardMatrix(a.getNumRows(), a.getNumCols());
		List<Runnable> addOps = generateAddOperations(a, b, result);

		executeThreadedOps(addOps);

		return result;
	}

	public static Matrix subtract(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkAdditionAndSubtractionDimenions(a, b)){
			throw new InvalidDimensionException();
		}

		Matrix result = new StandardMatrix(a.getNumRows(), a.getNumCols());
		List<Runnable> addOps = generateSubtractOperations(a, b, result);

		executeThreadedOps(addOps);

		return result;
	}

	public static Matrix multiply(Matrix a, Matrix b) throws InvalidDimensionException{
		if(!checkMultiplicationDimensions(a, b)){
			throw new InvalidDimensionException();
		}

		Matrix result = new StandardMatrix(a.getNumRows(), b.getNumCols());
		List<Runnable> addOps = generateMultiplicationOperations(a, b, result);

		executeThreadedOps(addOps);

		return result;
	}

	public static Matrix multiplyScalar(Matrix a, double scalar){
		Matrix result = new StandardMatrix(a.getNumRows(), a.getNumCols());
		
		List<Runnable> ops = generateScalarMultiplicationOperations(a, result, scalar);
		
		executeThreadedOps(ops);
		
		return result;
	}

	public static Matrix divideByScalar(Matrix a, double scalar){
		Matrix result = new StandardMatrix(a.getNumRows(), a.getNumCols());
		
		List<Runnable> ops = generateScalarDivisionOperations(a, result, scalar);
		
		executeThreadedOps(ops);
		
		return result;
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