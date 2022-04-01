/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;

@NonNullByDefault
public class ArithmeticParser extends ExpressionParser<Double> {
	private final Map<String, Double> variables = new HashMap<>();

	public ArithmeticParser() {
		setPrefixOperatorFactory(new IPrefixOperatorFactory<Double>() {

			@Override
			public boolean isPrefixOperator(final char operator) {
				return operator == '-';
			}

			@Override
			public IExpression<Double> createPrefixOperator(final char operator, final IExpression<Double> argument) {
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
				default:
				}
				return false;
			}

			@Override
			public boolean isInfixOperator(final char operator) {
				return operator == '*' || operator == '/' || operator == '+' || operator == '-';
			}

			@Override
			public IExpression<Double> createInfixOperator(final char operator, final IExpression<Double> lhs, final IExpression<Double> rhs) {
				return new ArithmeticOperator(operator, lhs, rhs);
			}
		});

		setFunctionFactory((name, arguments) -> {
			if (name.equals("avg")) {
				return new MeanFunction(arguments);
			} else {
				throw new RuntimeException("unknown function: " + name);
			}
		});

		setTermFactory(term -> new ArithmeticTerm(term, variables));
	}

	public void addVariable(final String name, final double value) {
		variables.put(name, value);
	}
}
