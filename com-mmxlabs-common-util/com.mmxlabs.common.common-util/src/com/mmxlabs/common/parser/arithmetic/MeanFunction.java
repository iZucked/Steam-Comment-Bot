/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class MeanFunction implements ArithmeticExpression {
	private List<IExpression<@NonNull Double>> arguments;

	public MeanFunction(final List<IExpression<@NonNull Double>> arguments) {
		this.arguments = arguments;
	}

	@Override
	public @NonNull Double evaluate() {
		double sum = 0;
		for (final IExpression<Double> arg : arguments) {
			sum += arg.evaluate();
		}
		return sum / arguments.size();
	}

	@Override
	public String toString() {
		return "avg(" + arguments + ")";
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}
}
