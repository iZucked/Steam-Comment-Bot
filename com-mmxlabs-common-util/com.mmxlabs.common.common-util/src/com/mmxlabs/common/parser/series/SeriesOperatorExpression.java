/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import com.mmxlabs.common.parser.IExpression;

public class SeriesOperatorExpression implements IExpression<ISeries> {
	private char op;
	private IExpression<ISeries> lhs;
	private IExpression<ISeries> rhs;

	public SeriesOperatorExpression(final char op, final IExpression<ISeries> lhs, final IExpression<ISeries> rhs) {
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
		private IOp op;
		private ISeries lhs;
		private ISeries rhs;

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
		public Number evaluate(int point) {
			final Number n1 = lhs.evaluate(point);
			final Number n2 = rhs.evaluate(point);
			return op.evaluate(n1, n2);
		}
	}

	private interface IOp {
		public Number evaluate(final Number a, final Number b);

		public static final IOp ADD = new IOp() {
			@Override
			public Number evaluate(Number a, Number b) {
				return a.doubleValue() + b.doubleValue();
			}
		};

		public static final IOp SUB = new IOp() {
			@Override
			public Number evaluate(Number a, Number b) {
				return a.doubleValue() - b.doubleValue();
			}
		};

		public static final IOp DIV = new IOp() {
			@Override
			public Number evaluate(Number a, Number b) {
				return a.doubleValue() / b.doubleValue();
			}
		};

		public static final IOp MUL = new IOp() {
			@Override
			public Number evaluate(Number a, Number b) {
				return a.doubleValue() * b.doubleValue();
			}
		};

		public static final IOp PERCENT = new IOp() {
			@Override
			public Number evaluate(Number a, Number b) {
				return a.doubleValue() * b.doubleValue() / 100.0;
			}
		};
	}
}
