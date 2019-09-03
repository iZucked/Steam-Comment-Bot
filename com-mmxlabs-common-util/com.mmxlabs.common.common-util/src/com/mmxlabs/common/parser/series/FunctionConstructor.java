/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.parser.IExpression;

public class FunctionConstructor implements IExpression<ISeries> {
	private final Class<? extends ISeries> clazz;
	private final List<IExpression<ISeries>> arguments;

	public FunctionConstructor(final Class<? extends ISeries> clazz, final IExpression<ISeries> e1, final IExpression<ISeries> e2) {
		this.clazz = clazz;
		this.arguments = CollectionsUtil.makeArrayList(e1, e2);
	}

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
		final List<ISeries> result = new ArrayList<>(exprs.size());
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