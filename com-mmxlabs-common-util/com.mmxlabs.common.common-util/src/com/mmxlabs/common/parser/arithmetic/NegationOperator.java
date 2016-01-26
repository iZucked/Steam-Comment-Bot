/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import com.mmxlabs.common.parser.IExpression;

public class NegationOperator extends ArithmeticExpression {
	final IExpression<Double> argument;

	public NegationOperator(IExpression<Double> argument) {
		super();
		this.argument = argument;
	}

	@Override
	public Double evaluate() {
		return -argument.evaluate();
	}

	@Override
	public String toString() {
		return "-" + argument;
	}
}
