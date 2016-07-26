/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IFunctionFactory;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;
import com.mmxlabs.common.parser.ITermFactory;
import com.mmxlabs.common.parser.series.functions.And;
import com.mmxlabs.common.parser.series.functions.Equal;
import com.mmxlabs.common.parser.series.functions.If;
import com.mmxlabs.common.parser.series.functions.InOrder;
import com.mmxlabs.common.parser.series.functions.Max;
import com.mmxlabs.common.parser.series.functions.Mean;
import com.mmxlabs.common.parser.series.functions.Min;
import com.mmxlabs.common.parser.series.functions.Or;
import com.mmxlabs.common.parser.series.functions.ShiftedSeries;

public class SeriesParser extends ExpressionParser<ISeries> {
	private final @NonNull Map<@NonNull String, @NonNull ISeries> evaluatedSeries = new HashMap<>();
	private final @NonNull Map<@NonNull String, @NonNull String> unevaluatedSeries = new HashMap<>();
	private final @NonNull Set<@NonNull String> expressionCurves = new HashSet<>();

	private class FunctionConstructor implements IExpression<ISeries> {
		private final Class<? extends ISeries> clazz;
		private final List<IExpression<ISeries>> arguments;

		public FunctionConstructor(final Class<? extends ISeries> clazz, final List<IExpression<ISeries>> arguments) {
			this.clazz = clazz;
			this.arguments = arguments;
		}

		public @NonNull ISeries construct() {
			try {
				return clazz.getConstructor(List.class).newInstance(evaluate(arguments));
			} catch (final Throwable th) {
				throw new RuntimeException(th);
			}
		}

		private List<ISeries> evaluate(final List<IExpression<ISeries>> exprs) {
			final List<ISeries> result = new ArrayList<ISeries>(exprs.size());
			for (final IExpression<ISeries> exp : exprs) {
				result.add(exp.evaluate());
			}
			return result;
		}

		@Override
		public @NonNull ISeries evaluate() {
			return construct();
		}
	}

	public SeriesParser() {
		setInfixOperatorFactory(new IInfixOperatorFactory<ISeries>() {
			@Override
			public boolean isOperatorHigherPriority(final char a, final char b) {
				if (a == b)
					return false;
				switch (a) {
				case '%':
					return true;
				case '*':
					return b != '%';
				case '/':
					return b == '+' || b == '-';
				case '+':
					return b == '-';
				case '-':
					return false;
				}
				return false;
			}

			@Override
			public boolean isInfixOperator(final char operator) {
				return operator == '*' || operator == '/' || operator == '+' || operator == '-' || operator == '%';
			}

			@Override
			public @NonNull IExpression<ISeries> createInfixOperator(final char operator, final @NonNull IExpression<ISeries> lhs, final @NonNull IExpression<ISeries> rhs) {
				return new SeriesOperatorExpression(operator, lhs, rhs);
			}
		});

		setTermFactory(new ITermFactory<ISeries>() {
			@Override
			public @NonNull IExpression<ISeries> createTerm(final @NonNull String term) {
				try {
					final long i = Long.parseLong(term);
					return new ConstantSeriesExpression(i);
				} catch (final NumberFormatException nfe) {
					try {
						final double d = Double.parseDouble(term);
						return new ConstantSeriesExpression(d);
					} catch (final NumberFormatException nfe2) {
						return new NamedSeriesExpression(getSeries(term));
					}
				}
			}
		});

		setFunctionFactory(new IFunctionFactory<ISeries>() {
			@Override
			public IExpression<ISeries> createFunction(final String name, final List<IExpression<ISeries>> arguments) {
				if (name.equals("MAX")) {
					return new FunctionConstructor(Max.class, arguments);
				} else if (name.equals("MIN")) {
					return new FunctionConstructor(Min.class, arguments);
				} else if (name.equals("AVG")) {
					return new FunctionConstructor(Mean.class, arguments);
				} else if (name.equals("SHIFT")) {
					return new IExpression<ISeries>() {
						@Override
						public @NonNull ISeries evaluate() {
							return new ShiftedSeries(arguments.get(0).evaluate(), arguments.get(1).evaluate());
						}
					};
				} else if (name.equals("AND")) {
					return new FunctionConstructor(And.class, arguments);
				} else if (name.equals("OR")) {
					return new FunctionConstructor(Or.class, arguments);
				} else if (name.equals("IF")) {
					return new FunctionConstructor(If.class, arguments);
				} else if (name.equals("SEQ")) {
					return new FunctionConstructor(InOrder.class, arguments);
				} else if (name.equals("EQ")) {
					return new FunctionConstructor(Equal.class, arguments);
				} else {
					throw new RuntimeException("Unknown series function " + name);
				}
			}
		});

		setPrefixOperatorFactory(new IPrefixOperatorFactory<ISeries>() {
			@Override
			public boolean isPrefixOperator(final char operator) {
				return false;
			}

			@Override
			public IExpression<ISeries> createPrefixOperator(final char operator, final IExpression<ISeries> argument) {
				throw new RuntimeException("Unknown prefix op " + operator);
			}
		});
	}

	public @NonNull ISeries getSeries(final @NonNull String name) {
		if (evaluatedSeries.containsKey(name)) {
			return evaluatedSeries.get(name);
		} else if (unevaluatedSeries.containsKey(name)) {
			final ISeries ser = parse(unevaluatedSeries.get(name)).evaluate();
			evaluatedSeries.put(name, ser);
			return ser;
		} else {
			throw new RuntimeException("No series with name " + name + " defined");
		}
	}

	public void addConstant(@NonNull final String name, @NonNull final Number value) {
		evaluatedSeries.put(name, new ConstantSeriesExpression(value).evaluate());
	}

	public void addSeriesData(@NonNull final String name, @NonNull ISeries series) {
		evaluatedSeries.put(name, series);
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	public void removeSeriesData(@Nullable final String name) {
		if (name == null) {
			return;
		}
		// Remove any references to the curve
		evaluatedSeries.remove(name);
		unevaluatedSeries.remove(name);
		expressionCurves.remove(name);
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	public void addSeriesData(@NonNull final String name, final int @NonNull [] points, @NonNull final Number[] values) {
		evaluatedSeries.put(name, new ISeries() {
			@Override
			public int[] getChangePoints() {
				return points;
			}

			@Override
			public Number evaluate(final int point) {
				return values.length == 0 ? 0 : values[SeriesUtil.floor(points, point)];
			}
		});
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	public void addSeriesExpression(final @NonNull String name, final @NonNull String expression) {
		// Register as an expression curve.
		if (!expressionCurves.add(name)) {
			// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
			for (final String expr : expressionCurves) {
				evaluatedSeries.remove(expr);
			}
		}
		unevaluatedSeries.put(name, expression);
	}

	public static void main(final String args[]) {
		final SeriesParser parser = new SeriesParser();
		parser.addConstant("X", 1);
		parser.addConstant("Y", 10);
		parser.addSeriesData("Z", new int[] { 0, 10, 20 }, new @NonNull Number[] { -5d, 4d, 9.4 });
		System.err.println(SeriesUtil.toString(parser.parse(args[0]).evaluate()));
	}
}
