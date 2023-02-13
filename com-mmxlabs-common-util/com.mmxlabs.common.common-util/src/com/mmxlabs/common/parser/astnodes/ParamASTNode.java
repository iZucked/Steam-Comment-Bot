/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.astnodes;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;

public final class ParamASTNode implements ASTNode {
	private static final int[] NONE = new int[0];

	private final Number defaultValue;
	private final String name;

	public String getName() {
		return name;
	}

	private final Set<String> parameters;

	public ParamASTNode(String name) {
		this(name, null);

	}

	public ParamASTNode(String name, @Nullable Number defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.parameters = Collections.singleton(name);

	}

	@Override
	public @NonNull IExpression<@NonNull ISeries> asExpression(@NonNull SeriesParser seriesParser) {

		return new IExpression<@NonNull ISeries>() {

			@Override
			public @NonNull ISeries evaluate() {
				return new ISeries() {

					@Override
					public boolean isParameterised() {
						return true;
					}

					@Override
					public Set<String> getParameters() {
						return parameters;
					}

					@Override
					public int[] getChangePoints() {
						return NONE;
					}

					@Override
					public Number evaluate(final int timePoint, final Map<String, String> params) {
						final String v = params.get(name);
						if (v == null) {
							return defaultValue;
						}
						return Double.parseDouble(v);
					}
				};
			}

			@Override
			public boolean canEvaluate() {
				return true;
			}
		};
	}

	@Override
	public @NonNull Iterable<@NonNull ASTNode> getChildren() {
		return Collections.emptySet();
	}

	@Override
	public void replace(@NonNull ASTNode original, @NonNull ASTNode replacement) {
		throw new IllegalArgumentException();
	}

	@Override
	public @NonNull String asString() {
		if (defaultValue != null) {
			return String.format("@%s:%s", name, defaultValue);
		} else {
			return String.format("@%s", name);
		}
	}
}
