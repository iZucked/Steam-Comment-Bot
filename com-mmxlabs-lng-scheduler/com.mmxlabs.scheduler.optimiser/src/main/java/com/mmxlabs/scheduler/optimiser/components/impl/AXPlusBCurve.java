package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * Implementation of {@link ICurve} providing ax+b formulation around a reference {@link ICurve} object. This implementation assumes integer arithmetic
 * 
 * @author Simon Goodall
 * 
 */
public class AXPlusBCurve implements ICurve {

	private final int a;
	private final int b;
	private final ICurve ref;

	public AXPlusBCurve(final int a, final int b, final ICurve ref) {
		this.a = a;
		this.b = b;
		this.ref = ref;
	}

	@Override
	public double getValueAtPoint(final double point) {

		return b + (int) Calculator.multiply((int) ref.getValueAtPoint(point), a);
	}
}
