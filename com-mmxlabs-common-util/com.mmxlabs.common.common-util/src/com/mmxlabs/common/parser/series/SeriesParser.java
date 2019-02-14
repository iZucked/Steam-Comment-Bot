/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private final @NonNull Map<@NonNull String, @NonNull ISeries> evaluatedSeries = new HashMap<>();
	private final @NonNull Map<@NonNull String, @NonNull String> unevaluatedSeries = new HashMap<>();
	private final @NonNull Set<@NonNull String> expressionCurves = new HashSet<>();

	private @Nullable ShiftFunctionMapper shiftMapper;
	private @Nullable CalendarMonthMapper calendarMonthMapper;

	public CalendarMonthMapper getCalendarMonthMapper() {
		return calendarMonthMapper;
	}

	public void setCalendarMonthMapper(CalendarMonthMapper calendarMonthMapper) {
		this.calendarMonthMapper = calendarMonthMapper;
	}

	public ShiftFunctionMapper getShiftMapper() {
		return shiftMapper;
	}

	public void setShiftMapper(ShiftFunctionMapper shiftMapper) {
		this.shiftMapper = shiftMapper;
	}

	public @NonNull ISeries getSeries(final @NonNull String name) {
		if (evaluatedSeries.containsKey(name.toLowerCase())) {
			return evaluatedSeries.get(name.toLowerCase());
		} else if (unevaluatedSeries.containsKey(name.toLowerCase())) {
			final ISeries ser = parse(unevaluatedSeries.get(name.toLowerCase())).evaluate();
			evaluatedSeries.put(name.toLowerCase(), ser);
			return ser;
		} else {
			throw new UnknownSeriesException("No series with name " + name + " defined");
		}
	}

	public IExpression<ISeries> parse(final String expression) {

		final ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
		Parser parser = new Parser(new Lexer(new StringReader(expression), symbolFactory));
		try {
			parser.setSeriesParser(this);
			parser.setShiftFunctionMapper(shiftMapper);
			parser.setCalendarMonthMapper(calendarMonthMapper);

			Symbol parse = parser.parse();
			if (parse == null || parse.value == null) {
				if (!parser.errors.isEmpty()) {
					Symbol s = parser.errors.get(0);
					throw new RuntimeException("Syntax error after: " + expression.substring(0, s.right - 1));
				}
			}

			return (IExpression<ISeries>) parse.value;
		} catch (final UnknownSeriesException e) {
			throw e;
		} catch (final ClassCastException e) {
			List<Symbol> errors = parser.errors;
			throw new RuntimeException("Syntax error ", e);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			List<Symbol> errors = parser.errors;
			throw new RuntimeException("Error parsing expression", e);
		}
	}

	public void addConstant(@NonNull final String name, @NonNull final Number value) {
		evaluatedSeries.put(name.toLowerCase(), new ConstantSeriesExpression(value).evaluate());
	}

	public void addSeriesData(final @NonNull String name, final @NonNull ISeries series) {
		evaluatedSeries.put(name.toLowerCase(), series);
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
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

	public void addSeriesData(@NonNull final String name, final int @NonNull [] points, @NonNull final Number[] values) {
		if (name == null) {
			return;
		}
		evaluatedSeries.put(name.toLowerCase(), new ISeries() {
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
		// Invalidate any pre-evaluated expression curves as we may have changed the underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
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
}
