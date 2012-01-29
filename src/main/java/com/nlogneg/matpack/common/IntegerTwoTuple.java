package com.nlogneg.matpack.common;

import java.util.HashMap;


public class IntegerTwoTuple {
	private final int row;
	private final int col;

	private final int hash;

	private static HashMap<Integer, IntegerTwoTuple> knownTuples = new HashMap<Integer, IntegerTwoTuple>();

	public static final IntegerTwoTuple getTuple(int row, int col){
		IntegerTwoTuple it = new IntegerTwoTuple(row, col);
		
		IntegerTwoTuple fetch = knownTuples.get(it.hashCode());

		if(fetch == null){
			knownTuples.put(it.hashCode(), it);
			return it;
		}

		return fetch;
	}
	
	public static final int calculateHashCode(int row, int col){
		return (int)(Math.pow(2, row + 1) * Math.pow(3, col + 1));
	}

	private IntegerTwoTuple(int row, int col){
		this.row = row;
		this.col = col;

		hash = calculateHashCode(row, col);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean equals(Object o){
		if(o instanceof IntegerTwoTuple){
			IntegerTwoTuple t = (IntegerTwoTuple)o;
			if(row == t.getRow() && col == t.getCol()){
				return true;
			}
		}
		return false;
	}

	public int hashCode(){
		return hash;
	}
}
