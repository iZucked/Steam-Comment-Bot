/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class NegationOperator implements ArithmeticExpression {
	final IExpression<@NonNull Double> argument;

	public NegationOperator(final IExpression<@NonNull Double> argument) {
		super();
		this.argument = argument;
	}

	@Override
	public @NonNull Double evaluate() {
		return -argument.evaluate();
	}

	@Override
	public String toString() {
		return "-" + argument;
	}

	@Override
	public boolean canEvaluate() {
		// TODO Auto-generated method stub
		return true;
	}
}
