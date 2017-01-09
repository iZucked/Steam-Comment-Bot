/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

public class ArithmeticTerm extends ArithmeticExpression {
	private final @NonNull String term;
	private final @NonNull Map<@NonNull String, @NonNull Double> variables;

	public ArithmeticTerm(final @NonNull String term, final @NonNull Map<@NonNull String, @NonNull Double> variables) {
		this.term = term;
		this.variables = variables;
	}

	@Override
	public @NonNull Double evaluate() {
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
