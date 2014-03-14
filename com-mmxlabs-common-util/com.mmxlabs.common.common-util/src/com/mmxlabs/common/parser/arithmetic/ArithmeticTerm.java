/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.Map;

public class ArithmeticTerm extends ArithmeticExpression {
	private final String term;
	private final Map<String, Double> variables;

	public ArithmeticTerm(final String term, final Map<String, Double> variables) {
		this.term = term;
		this.variables = variables;
	}

	@Override
	public Double evaluate() {
		try {
			return Double.parseDouble(term);
		} catch (final NumberFormatException exception) {
			return variables.get(term);
		}
	}

	@Override
	public String toString() {
		return term;
	}
}
