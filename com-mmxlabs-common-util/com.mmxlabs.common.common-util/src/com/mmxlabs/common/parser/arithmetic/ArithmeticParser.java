/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IFunctionFactory;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;
import com.mmxlabs.common.parser.ITermFactory;

public class ArithmeticParser extends ExpressionParser<Double> {
	private final @NonNull Map<@NonNull String, @NonNull Double> variables = new HashMap<>();

	public ArithmeticParser() {
		setPrefixOperatorFactory(new IPrefixOperatorFactory<@NonNull Double>() {

			@Override
			public boolean isPrefixOperator(final char operator) {
				return operator == '-';
			}

			@Override
			public IExpression<Double> createPrefixOperator(final char operator, @NonNull final IExpression<@NonNull Double> argument) {
				return new NegationOperator(argument);
			}
		});

		setInfixOperatorFactory(new IInfixOperatorFactory<Double>() {
			@Override
			public boolean isOperatorHigherPriority(final char a, final char b) {
				if (a == b)
					return false;
				switch (a) {
				case '*':
					return true;
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
				return operator == '*' || operator == '/' || operator == '+' || operator == '-';
			}

			@Override
			public @NonNull IExpression<Double> createInfixOperator(final char operator, final IExpression<Double> lhs, final IExpression<Double> rhs) {
				return new ArithmeticOperator(operator, lhs, rhs);
			}
		});

		setFunctionFactory(new IFunctionFactory<@NonNull Double>() {

			@Override
			public IExpression<Double> createFunction(@NonNull final String name, final List<IExpression<@NonNull Double>> arguments) {
				if (name.equals("avg")) {
					return new MeanFunction(arguments);
				} else {
					throw new RuntimeException("unknown function: " + name);
				}
			}
		});

		setTermFactory(new ITermFactory<@NonNull Double>() {
			@Override
			public @NonNull IExpression<@NonNull Double> createTerm(@NonNull final String term) {
				return new ArithmeticTerm(term, variables);
			}
		});
	}

	public void addVariable(final @NonNull String name, final double value) {
		variables.put(name, value);
	}

	public static void main(final String[] args) {
		final ArithmeticParser parser = new ArithmeticParser();
		for (int i = 1; i < args.length; i += 2) {
			final String name = args[i];
			assert name != null;
			parser.addVariable(name, Double.parseDouble(args[i + 1]));
		}
		final IExpression<Double> expression = parser.parse(args[0]);
		System.err.println(expression + " = " + expression.evaluate());
	}
}
