package com.nlogneg.matpack.common;

import java.util.HashMap;


public class IntegerTuple {
	private final int length;
	private final int[] members;

	private final int hash;

	private static HashMap<Integer, IntegerTuple> knownTuples = new HashMap<Integer, IntegerTuple>();
	
	public static final IntegerTuple getTuple(int...elements){
		IntegerTuple it = new IntegerTuple(elements.length, elements);
		
		IntegerTuple fetch = knownTuples.get(it.hashCode());
		
		if(fetch == null){
			knownTuples.put(it.hashCode(), it);
			return it;
		}
		
		return fetch;
	}
	
	private IntegerTuple(int length, int...elements){
		this.length = length;
		members = new int[length];
		
		int runningHash = 0;
		
		for(int i = 0; i < length; i++){
			int e = elements[i];
			members[i] = e;
			
			runningHash += (e << 3) ^ runningHash; 
		}
		
		hash = runningHash;
		
	}
	
	public int getLength(){
		return length;
	}

	public int getElement(int index){
		return members[index];
	}

	public boolean equals(Object o){
		if(o instanceof IntegerTuple){
			IntegerTuple t = (IntegerTuple)o;
			if(length == t.getLength()){
				for(int i = 0; i < length; i++){
					if(members[i] != t.getElement(i)){
						return false;
					}
				}
				return true;
			}
		}

		return false;
	}

	public int hashCode(){
		return hash;
	}
}
