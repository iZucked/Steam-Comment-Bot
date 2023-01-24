/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class SeriesOperatorExpression implements IExpression<ISeries> {
	private final char op;
	private final @NonNull IExpression<ISeries> lhs;
	private final @NonNull IExpression<ISeries> rhs;

	public SeriesOperatorExpression(final char op, final @NonNull IExpression<ISeries> lhs, final @NonNull IExpression<ISeries> rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public @NonNull ISeries evaluate() {
		final IOp opImpl;
		switch (op) {
		case '*':
			opImpl = IOp.MUL;
			break;
		case '/':
			opImpl = IOp.DIV;
			break;
		case '+':
			opImpl = IOp.ADD;
			break;
		case '-':
			opImpl = IOp.SUB;
			break;
		case '%':
			opImpl = IOp.PERCENT;
			break;
		default:
			throw new RuntimeException("Unknown operator " + op);
		}

		return new OpSeries(opImpl, lhs.evaluate(), rhs.evaluate());
	}

	private class OpSeries implements ISeries {
		private final IOp op;
		private final ISeries lhs;
		private final ISeries rhs;

		public OpSeries(final IOp op, final ISeries lhs, final ISeries rhs) {
			this.op = op;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		public boolean isParameterised() {
			return lhs.isParameterised() || rhs.isParameterised();
		}

		@Override
		public Set<String> getParameters() {

			Set<String> p = new HashSet<>();
			p.addAll(lhs.getParameters());
			p.addAll(rhs.getParameters());
			return p;
		}

		@Override
		public int[] getChangePoints() {
			return SeriesUtil.mergeChangePoints(lhs.getChangePoints(), rhs.getChangePoints());
		}

		@Override
		public Number evaluate(final int timePoint, final Map<String, String> params) {
			final Number n1 = lhs.evaluate(timePoint, params);
			final Number n2 = rhs.evaluate(timePoint, params);
			return op.evaluate(n1, n2);
		}

	}

	private interface IOp {
		public Number evaluate(final Number a, final Number b);

		public static final IOp ADD = (a, b) -> a.doubleValue() + b.doubleValue();
		public static final IOp SUB = (a, b) -> a.doubleValue() - b.doubleValue();
		public static final IOp DIV = (a, b) -> b.doubleValue() == 0.0 ? 0.0 : a.doubleValue() / b.doubleValue();
		public static final IOp MUL = (a, b) -> a.doubleValue() * b.doubleValue();
		public static final IOp PERCENT = (a, b) -> a.doubleValue() * b.doubleValue() / 100.0;
	}

	public char getOperation() {
		return op;
	}

	public @NonNull IExpression<ISeries> getLHS() {
		return lhs;
	}

	public @NonNull IExpression<ISeries> getRHS() {
		return rhs;
	}

	@Override
	public boolean canEvaluate() {
		return lhs.canEvaluate() && rhs.canEvaluate();
	}
}
