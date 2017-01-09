/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class ArithmeticOperator extends ArithmeticExpression {
	private IExpression<Double> rhs;
	private IExpression<Double> lhs;
	private char op;

	public ArithmeticOperator(final char op, final IExpression<Double> lhs, final IExpression<Double> rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public @NonNull Double evaluate() {
		final Double lhsValue = lhs.evaluate();
		final Double rhsValue = rhs.evaluate();
		switch (op) {
		case '*':
			return lhsValue * rhsValue;
		case '/':
			return lhsValue / rhsValue;
		case '+':
			return lhsValue + rhsValue;
		case '-':
			return lhsValue - rhsValue;
		}
		throw new RuntimeException("Unknown operator " + op);
	}

	@Override
	public String toString() {
		return "(" + lhs + op + rhs + ")";
	}
}
