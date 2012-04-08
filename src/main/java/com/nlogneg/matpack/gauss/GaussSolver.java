package com.nlogneg.matpack.gauss;

import com.nlogneg.matpack.Matrix;

public interface GaussSolver {
	public void rowReduceToEchelonForm(Matrix a);
	public void rowReduce(Matrix a);
}
