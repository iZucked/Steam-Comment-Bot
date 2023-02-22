/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class ArithmeticOperator implements ArithmeticExpression {
	private final IExpression<Double> rhs;
	private final IExpression<Double> lhs;
	private final char op;

	public ArithmeticOperator(final char op, final IExpression<Double> lhs, final IExpression<Double> rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public @NonNull Double evaluate() {
		final Double lhsValue = lhs.evaluate();
		final Double rhsValue = rhs.evaluate();
		return switch (op) {
		case '*' -> lhsValue * rhsValue;
		case '/' -> lhsValue / rhsValue;
		case '+' -> lhsValue + rhsValue;
		case '-' -> lhsValue - rhsValue;
		default -> throw new RuntimeException("Unknown operator " + op);
		};
	}

	@Override
	public String toString() {
		return "(" + lhs + op + rhs + ")";
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}
}
