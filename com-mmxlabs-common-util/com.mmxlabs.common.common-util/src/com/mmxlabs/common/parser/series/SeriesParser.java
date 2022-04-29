/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.impl.Lexer;
import com.mmxlabs.common.parser.impl.Parser;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class SeriesParser {
	private final @NonNull Map<@NonNull String, @NonNull ISeriesContainer> evaluatedSeries = new HashMap<>();
	private final @NonNull Map<@NonNull String, @NonNull String> unevaluatedSeries = new HashMap<>();
	private final @NonNull Set<@NonNull String> expressionCurves = new HashSet<>();

	private final @NonNull SeriesParserData seriesParserData;

	public SeriesParser(@NonNull SeriesParserData seriesParserData) {
		this.seriesParserData = seriesParserData;
	}

	public @NonNull ISeriesContainer getSeries(@NonNull final String name) {
		final String lowercaseName = name.toLowerCase();
		if (evaluatedSeries.containsKey(lowercaseName)) {
			return evaluatedSeries.get(lowercaseName);
		} else if (unevaluatedSeries.containsKey(lowercaseName)) {
			final IExpression<ISeries> parsed = parse(unevaluatedSeries.get(lowercaseName));
			final ISeriesContainer ser;
			if (parsed.canEvaluate()) {
				ser = new DefaultSeriesContainer(parsed.evaluate());
			} else {
				ser = new DefaultExpressionContainer(parsed);
			}
			evaluatedSeries.put(lowercaseName, ser);
			return ser;
		} else {
			throw new UnknownSeriesException("No series with name " + name + "defined");
		}
		
	}

	public IExpression<ISeries> parse(final String expression) {

		final ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
		Parser parser = new Parser(new Lexer(new StringReader(expression), symbolFactory));
		try {
			parser.setSeriesParser(this);
			parser.setSeriesParserData(seriesParserData);

			Symbol parse = parser.parse();
			if (parse == null || parse.value == null) {
				if (!parser.errors.isEmpty()) {
					Symbol s = parser.errors.get(0);
					throw new RuntimeException("Syntax error after: " + expression.substring(0, s.right - 1));
				} else {
					throw new RuntimeException("Syntax error: " + expression);
				}
			}

			return (IExpression<ISeries>) parse.value;
		} catch (final ClassCastException e) {
			throw new RuntimeException("Syntax error ", e);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException("Error parsing expression", e);
		}
	}

	public void addConstant(@NonNull final String name, @NonNull final Number value) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(new ConstantSeriesExpression(value).evaluate());
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesData(final @NonNull String name, final @NonNull ISeries series) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(series);
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesData(final @NonNull String name, @NonNull final ISeriesContainer seriesContainer) {
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void removeSeriesData(@Nullable final String name) {
		if (name == null) {
			return;
		}
		// Remove any references to the curve
		evaluatedSeries.remove(name.toLowerCase());
		unevaluatedSeries.remove(name.toLowerCase());
		expressionCurves.remove(name.toLowerCase());
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	private void addSeriesContainerToEvaluatedSeries(@NonNull final String name, @NonNull final ISeriesContainer seriesContainer) {
		evaluatedSeries.put(name.toLowerCase(), seriesContainer);
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	public void addSeriesData(@NonNull final String name, final int @NonNull [] points, @NonNull final Number[] values) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(new ISeries() {
			@Override
			public int[] getChangePoints() {
				return points;
			}

			@Override
			public Number evaluate(final int point) {
				int pos = SeriesUtil.floor(points, point);
				if (pos == -1) {
					return 0;
				}
				return values.length == 0 ? 0 : values[pos];
			}
		});
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesExpression(final @NonNull String name, final @NonNull String expression) {
		// Register as an expression curve.
		if (!expressionCurves.add(name.toLowerCase())) {
			// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
			for (final String expr : expressionCurves) {
				evaluatedSeries.remove(expr);
			}
		}
		unevaluatedSeries.put(name.toLowerCase(), expression);
	}

	public SeriesParserData getSeriesParserData() {
		return seriesParserData;
	}
}
