/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * A curve which implements $y = Ae^kx + C$
 * 
 * @author hinton
 * 
 */
public final class ExponentialCurve implements ICurve {
	private double A = 1;
	private double k = -1;
	private double C = 0;

	public ExponentialCurve() {

	}

	public ExponentialCurve(final double A, final double k, final double C) {
		setA(A);
		setK(k);
		setC(C);
	}

	public double getA() {
		return A;
	}

	public void setA(final double a) {
		A = a;
	}

	public double getK() {
		return k;
	}

	public void setK(final double k) {
		this.k = k;
	}

	public double getC() {
		return C;
	}

	public void setC(final double c) {
		C = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.common.curves.ICurve#getValueAtPoint(double)
	 */
	@Override
	public int getValueAtPoint(final int point) {
		return (int) (C + (A * Math.exp(k * point)));
	}
}
