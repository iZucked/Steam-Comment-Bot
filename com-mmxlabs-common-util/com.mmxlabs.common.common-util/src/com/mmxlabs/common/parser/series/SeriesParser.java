/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<String, ISeries> evaluatedSeries = new HashMap<String, ISeries>();
	private final Map<String, String> unevaluatedSeries = new HashMap<String, String>();

	private class FunctionConstructor implements IExpression<ISeries> {
		private Class<? extends ISeries> clazz;
		private List<IExpression<ISeries>> arguments;

		public FunctionConstructor(final Class<? extends ISeries> clazz, final List<IExpression<ISeries>> arguments) {
			this.clazz = clazz;
			this.arguments = arguments;
		}

		public ISeries construct() {
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
		public ISeries evaluate() {
			return construct();
		}
	}

	public SeriesParser() {
		setInfixOperatorFactory(new IInfixOperatorFactory<ISeries>() {
			@Override
			public boolean isOperatorHigherPriority(char a, char b) {
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
			public boolean isInfixOperator(char operator) {
				return operator == '*' || operator == '/' || operator == '+' || operator == '-' || operator == '%';
			}

			@Override
			public IExpression<ISeries> createInfixOperator(char operator, IExpression<ISeries> lhs, IExpression<ISeries> rhs) {
				return new SeriesOperatorExpression(operator, lhs, rhs);
			}
		});

		setTermFactory(new ITermFactory<ISeries>() {
			@Override
			public IExpression<ISeries> createTerm(final String term) {
				try {
					int i = Integer.parseInt(term);
					return new ConstantSeriesExpression(i);
				} catch (NumberFormatException nfe) {
					try {
						double d = Double.parseDouble(term);
						return new ConstantSeriesExpression(d);
					} catch (NumberFormatException nfe2) {
						return new NamedSeriesExpression(getSeries(term));
					}
				}
			}
		});

		setFunctionFactory(new IFunctionFactory<ISeries>() {
			@Override
			public IExpression<ISeries> createFunction(String name, final List<IExpression<ISeries>> arguments) {
				if (name.equals("MAX")) {
					return new FunctionConstructor(Max.class, arguments);
				} else if (name.equals("MIN")) {
					return new FunctionConstructor(Min.class, arguments);
				} else if (name.equals("AVG")) {
					return new FunctionConstructor(Mean.class, arguments);
				} else if (name.equals("SHIFT")) {
					return new IExpression<ISeries>() {
						@Override
						public ISeries evaluate() {
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
			public boolean isPrefixOperator(char operator) {
				return false;
			}

			@Override
			public IExpression<ISeries> createPrefixOperator(char operator, IExpression<ISeries> argument) {
				throw new RuntimeException("Unknown prefix op " + operator);
			}
		});
	}

	public ISeries getSeries(final String name) {
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

	public void addConstant(final String name, final Number value) {
		evaluatedSeries.put(name, new ConstantSeriesExpression(value).evaluate());
	}

	public void addSeriesData(final String name, final int[] points, final Number[] values) {
		evaluatedSeries.put(name, new ISeries() {
			@Override
			public int[] getChangePoints() {
				return points;
			}

			@Override
			public Number evaluate(int point) {
				return values[SeriesUtil.floor(points, point)];
			}
		});
	}

	public void addSeriesExpression(final String name, final String expression) {
		unevaluatedSeries.put(name, expression);
	}

	public static void main(String args[]) {
		final SeriesParser parser = new SeriesParser();
		parser.addConstant("X", 1);
		parser.addConstant("Y", 10);
		parser.addSeriesData("Z", new int[] { 0, 10, 20 }, new Double[] { -5d, 4d, 9.4 });
		System.err.println(SeriesUtil.toString(parser.parse(args[0]).evaluate()));
	}
}
