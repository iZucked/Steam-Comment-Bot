/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.List;

import com.mmxlabs.common.parser.IExpression;

public class MeanFunction extends ArithmeticExpression {
	private List<IExpression<Double>> arguments;

	public MeanFunction(final List<IExpression<Double>> arguments) {
		this.arguments = arguments;
	}

	@Override
	public Double evaluate() {
		double sum = 0;
		for (final IExpression<Double> arg : arguments)
			sum += arg.evaluate();
		return sum / arguments.size();
	}

	@Override
	public String toString() {
		return "avg(" + arguments + ")";
	}
}
