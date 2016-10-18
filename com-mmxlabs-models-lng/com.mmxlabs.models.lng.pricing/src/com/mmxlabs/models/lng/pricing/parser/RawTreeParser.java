/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parser;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.ExpressionParser;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.IFunctionFactory;
import com.mmxlabs.common.parser.IInfixOperatorFactory;
import com.mmxlabs.common.parser.IPrefixOperatorFactory;
import com.mmxlabs.common.parser.ITermFactory;

/**
 * Parser for price expressions returning a raw parse tree. NOTE: this class duplicates code in ISeriesParser and its ancestors so it will NOT automatically remain in synch.
 * 
 * @author Simon McGregor
 */
public class RawTreeParser extends ExpressionParser<Node> {
	public RawTreeParser() {
		setInfixOperatorFactory(new IInfixOperatorFactory<Node>() {

			@Override
			public IExpression<Node> createInfixOperator(final char operator, final IExpression<Node> lhs, final IExpression<Node> rhs) {
				final Node[] children = { lhs.evaluate(), rhs.evaluate() };
				return new NodeExpression("" + operator, children);
			}

			@Override
			public boolean isOperatorHigherPriority(final char a, final char b) {
				if (a == b)
					return false;
				switch (a) {
				case '*':
					return true;
				case '%':
					return b == '/' || b == '+' || b == '-';
				case '/':
					return b == '+' || b == '-';
				case '-':
					return b == '+';
				case '+':
					return false;
				}
				return false;
			}

			@Override
			public boolean isInfixOperator(final char operator) {
				return operator == '*' || operator == '/' || operator == '+' || operator == '-' || operator == '%';
			}

		});

		setTermFactory(new ITermFactory<Node>() {

			@Override
			public IExpression<Node> createTerm(final String term) {
				return new NodeExpression(term, new Node[0]);
			}
		});

		setFunctionFactory(new IFunctionFactory<Node>() {
			@Override
			public IExpression<Node> createFunction(final String name, final List<IExpression<Node>> arguments) {
				final @NonNull Node[] children = new Node[arguments.size()];

				for (int i = 0; i < arguments.size(); i++) {
					children[i] = arguments.get(i).evaluate();
				}

				return new NodeExpression(name, children);
			}
		});

		setPrefixOperatorFactory(new IPrefixOperatorFactory<Node>() {
			@Override
			public boolean isPrefixOperator(final char operator) {
				if (operator == '-') {
					return true;
				}
				return false;
			}

			@Override
			public IExpression<Node> createPrefixOperator(final char operator, final IExpression<Node> argument) {
				if (operator == '-') {
					final @NonNull Node[] children = new Node[1];

					children[0] = argument.evaluate();

					return new NodeExpression("-", children);
				}
				throw new RuntimeException("Unknown prefix op " + operator);
			}
		});
	}

}