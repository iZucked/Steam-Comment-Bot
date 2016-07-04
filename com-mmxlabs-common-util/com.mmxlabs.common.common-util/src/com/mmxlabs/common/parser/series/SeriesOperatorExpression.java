/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class SeriesOperatorExpression implements IExpression<ISeries> {
	private final char op;
	private @NonNull final IExpression<ISeries> lhs;
	private @NonNull final IExpression<ISeries> rhs;

	public SeriesOperatorExpression(final char op, final @NonNull IExpression<ISeries> lhs, final @NonNull IExpression<ISeries> rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public ISeries evaluate() {
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
		public int[] getChangePoints() {
			return SeriesUtil.mergeChangePoints(lhs.getChangePoints(), rhs.getChangePoints());
		}

		@Override
		public Number evaluate(final int point) {
			final Number n1 = lhs.evaluate(point);
			final Number n2 = rhs.evaluate(point);
			return op.evaluate(n1, n2);
		}
	}

	private interface IOp {
		public Number evaluate(final Number a, final Number b);

		public static final IOp ADD = new IOp() {
			@Override
			public Number evaluate(final Number a, final Number b) {
				return a.doubleValue() + b.doubleValue();
			}
		};

		public static final IOp SUB = new IOp() {
			@Override
			public Number evaluate(final Number a, final Number b) {
				return a.doubleValue() - b.doubleValue();
			}
		};

		public static final IOp DIV = new IOp() {
			@Override
			public Number evaluate(final Number a, final Number b) {
				return a.doubleValue() / b.doubleValue();
			}
		};

		public static final IOp MUL = new IOp() {
			@Override
			public Number evaluate(final Number a, final Number b) {
				return a.doubleValue() * b.doubleValue();
			}
		};

		public static final IOp PERCENT = new IOp() {
			@Override
			public Number evaluate(final Number a, final Number b) {
				return a.doubleValue() * b.doubleValue() / 100.0;
			}
		};
	}
}
