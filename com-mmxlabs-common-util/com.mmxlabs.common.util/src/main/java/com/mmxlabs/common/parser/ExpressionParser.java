/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * General purpose arithmetic expression parser.
 * 
 * @author hinton
 * 
 */
public class ExpressionParser<T> implements IExpressionParser<T> {
	private IPrefixOperatorFactory<T> prefixFactory;
	private IInfixOperatorFactory<T> infixFactory;
	private IFunctionFactory<T> functionFactory;
	private ITermFactory<T> termFactory;

	public IExpression<T> parse(final String expression) {
		final StreamTokenizer tok = new StreamTokenizer(new StringReader(expression));

		tok.resetSyntax();
		tok.eolIsSignificant(false);
		tok.lowerCaseMode(false);
		tok.slashSlashComments(false);
		tok.slashStarComments(false);

		tok.quoteChar('"');
		tok.quoteChar('\'');

		tok.whitespaceChars(' ', ' ');
		tok.whitespaceChars('\t', '\t');
		tok.wordChars('a', 'z');
		tok.wordChars('A', 'Z');
		// parse numbers as words
		tok.wordChars('.', '.');
		tok.wordChars('0', '9');

		try {
			return parse(tok);
		} catch (IOException e) {
			return null;
		}
	}

	private class Operator {
		final public boolean isUnary;
		final public char name;

		public Operator(boolean isUnary, char name) {
			super();
			this.isUnary = isUnary;
			this.name = name;
		}
	}

	private IExpression<T> parse(final StreamTokenizer tok) throws IOException {
		final Stack<Operator> operatorStack = new Stack<Operator>();
		final Stack<IExpression<T>> fragmentStack = new Stack<IExpression<T>>();

		boolean justPushedExpression = false;
		boolean justSeenWord = false;
		String word = null;

		loop: while (true) {
			tok.nextToken();
			if (justSeenWord && tok.ttype != '(') {
				// have to push variable reference onto stack
				fragmentStack.push(termFactory.createTerm(word));
				justPushedExpression = true;
			}
			if (tok.ttype == StreamTokenizer.TT_EOF)
				break loop;

			switch (tok.ttype) {
			case '(':
				if (justSeenWord) {
					final String functionName = word;
					final List<IExpression<T>> arguments = new ArrayList<IExpression<T>>();
					do {
						final IExpression<T> fragment = parse(tok);
						arguments.add(fragment);
					} while (tok.ttype == ',');
					// create function call
					fragmentStack.push(functionFactory.createFunction(functionName, arguments));
					justPushedExpression = true;
				} else {
					fragmentStack.push(parse(tok));
					justPushedExpression = true;
				}
				break;
			case ')':
			case ',':
				break loop;
			case StreamTokenizer.TT_WORD:
			case '"':
			case '\'':
				word = tok.sval;
				break;
			default:
				final char opChar = (char) tok.ttype;
				if (!justPushedExpression) {
					if (prefixFactory.isPrefixOperator(opChar)) {
						operatorStack.push(new Operator(true, opChar));
						justPushedExpression = false;
						break;
					}
				}

				if (infixFactory.isInfixOperator(opChar)) {
					while (!operatorStack.isEmpty() && operatorStack.peek().isUnary) {
						final Operator o = operatorStack.pop();
						final IExpression<T> arg = fragmentStack.pop();
						fragmentStack.push(prefixFactory.createPrefixOperator(o.name, arg));
					}

					if (!operatorStack.isEmpty() && infixFactory.isOperatorHigherPriority(operatorStack.peek().name, opChar)) {
						final Operator o = operatorStack.pop();
						final IExpression<T> rhs = fragmentStack.pop();
						final IExpression<T> lhs = fragmentStack.pop();
						fragmentStack.push(infixFactory.createInfixOperator(o.name, lhs, rhs));
					}

					operatorStack.push(new Operator(false, opChar));
					justPushedExpression = false;
				}
				break;

			}

			justSeenWord = tok.ttype == StreamTokenizer.TT_WORD || tok.ttype == '"' || tok.ttype == '\'';
		}

		// handle leftover operators
		while (operatorStack.isEmpty() == false) {
			final Operator o = operatorStack.pop();

			if (o.isUnary) {
				fragmentStack.push(prefixFactory.createPrefixOperator(o.name, fragmentStack.pop()));
			} else {
				final IExpression<T> rhs = fragmentStack.pop();
				final IExpression<T> lhs = fragmentStack.pop();
				fragmentStack.push(infixFactory.createInfixOperator(o.name, lhs, rhs));
			}
		}

		return fragmentStack.pop();
	}

	public static void main(String args[]) {

	}

	@Override
	public void setPrefixOperatorFactory(IPrefixOperatorFactory<T> factory) {
		this.prefixFactory = factory;
	}

	@Override
	public void setInfixOperatorFactory(IInfixOperatorFactory<T> factory) {
		this.infixFactory = factory;
	}

	@Override
	public void setFunctionFactory(IFunctionFactory<T> factory) {
		this.functionFactory = factory;
	}

	@Override
	public void setTermFactory(ITermFactory<T> factory) {
		this.termFactory = factory;
	}
}
