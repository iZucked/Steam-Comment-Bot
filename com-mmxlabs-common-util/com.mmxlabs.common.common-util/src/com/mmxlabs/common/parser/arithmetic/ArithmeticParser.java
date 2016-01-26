/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IFunctionFactory;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;
import com.mmxlabs.common.parser.ITermFactory;

public class ArithmeticParser extends ExpressionParser<Double> {
	private Map<String, Double> variables = new HashMap<String, Double>();

	public ArithmeticParser() {
		setPrefixOperatorFactory(new IPrefixOperatorFactory<Double>() {

			@Override
			public boolean isPrefixOperator(char operator) {
				return operator == '-';
			}

			@Override
			public IExpression<Double> createPrefixOperator(char operator, IExpression<Double> argument) {
				return new NegationOperator(argument);
			}
		});

		setInfixOperatorFactory(new IInfixOperatorFactory<Double>() {
			@Override
			public boolean isOperatorHigherPriority(char a, char b) {
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
			public boolean isInfixOperator(char operator) {
				return operator == '*' || operator == '/' || operator == '+' || operator == '-';
			}

			@Override
			public IExpression<Double> createInfixOperator(char operator, IExpression<Double> lhs, IExpression<Double> rhs) {
				return new ArithmeticOperator(operator, lhs, rhs);
			}
		});

		setFunctionFactory(new IFunctionFactory<Double>() {

			@Override
			public IExpression<Double> createFunction(String name, List<IExpression<Double>> arguments) {
				if (name.equals("avg")) {
					return new MeanFunction(arguments);
				} else {
					throw new RuntimeException("unknown function: " + name);
				}
			}
		});

		setTermFactory(new ITermFactory<Double>() {
			@Override
			public IExpression<Double> createTerm(String term) {
				return new ArithmeticTerm(term, variables);
			}
		});
	}

	public void addVariable(final String name, final double value) {
		variables.put(name, value);
	}

	public static void main(String[] args) {
		final ArithmeticParser parser = new ArithmeticParser();
		for (int i = 1; i < args.length; i += 2) {
			parser.addVariable(args[i], Double.parseDouble(args[i + 1]));
		}
		final IExpression<Double> expression = parser.parse(args[0]);
		System.err.println(expression + " = " + expression.evaluate());
	}
}
