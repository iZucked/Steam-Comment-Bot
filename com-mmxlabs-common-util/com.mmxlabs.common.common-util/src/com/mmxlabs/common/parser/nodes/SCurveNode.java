/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class SCurveNode extends AbstractMarkedUpNode {

	private final MarkedUpNode base;
	private final double firstThreshold;
	private final double secondThreshold;
	private double a1;
	private double b1;
	private double a2;
	private double b2;
	private double a3;
	private double b3;

	public SCurveNode(final MarkedUpNode base, final double firstThreshold, final double secondThreshold, final double a1, double b1, double a2, double b2, double a3, double b3) {
		this.base = base;
		this.firstThreshold = firstThreshold;
		this.secondThreshold = secondThreshold;
		this.a1 = a1;
		this.b1 = b1;
		this.a2 = a2;
		this.b2 = b2;
		this.a3 = a3;
		this.b3 = b3;
	}

	public double getA1() {
		return a1;
	}

	public double getB1() {
		return b1;
	}

	public double getA2() {
		return a2;
	}

	public double getB2() {
		return b2;
	}

	public double getA3() {
		return a3;
	}

	public double getB3() {
		return b3;
	}

	public MarkedUpNode getBase() {
		return base;
	}

	public double getFirstThreshold() {
		return firstThreshold;
	}

	public double getSecondThreshold() {
		return secondThreshold;
	}
}
