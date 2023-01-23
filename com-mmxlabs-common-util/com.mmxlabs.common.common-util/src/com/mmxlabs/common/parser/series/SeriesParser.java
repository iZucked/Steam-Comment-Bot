/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.BunkersSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CharterSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.ConversionASTNode;
import com.mmxlabs.common.parser.astnodes.CurrencySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.PricingBasisSeriesASTNode;
import com.mmxlabs.common.parser.impl.Lexer;
import com.mmxlabs.common.parser.impl.Parser;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

@NonNullByDefault
public class SeriesParser {
	private final Map<String, ISeriesContainer> evaluatedSeries = new HashMap<>();
	private final Map<String, Pair<String, SeriesType>> unevaluatedSeries = new HashMap<>();
	private final Set<String> expressionCurves = new HashSet<>();

	private final SeriesParserData seriesParserData;

	public SeriesParser(final SeriesParserData seriesParserData) {
		this.seriesParserData = seriesParserData;
	}

	public ISeriesContainer getSeries(final String name) {
		final String lowercaseName = name.toLowerCase();
		if (evaluatedSeries.containsKey(lowercaseName)) {
			return evaluatedSeries.get(lowercaseName);
		} else if (unevaluatedSeries.containsKey(lowercaseName)) {
			final Pair<String, SeriesType> p = unevaluatedSeries.get(lowercaseName);
			final IExpression<ISeries> parsed = asIExpression(p.getFirst());
			final ISeriesContainer ser;
			if (parsed.canEvaluate()) {
				ser = new DefaultSeriesContainer(name, p.getSecond(), parsed.evaluate());
			} else {
				ser = new DefaultExpressionContainer(name, p.getSecond(), parsed);
			}
			evaluatedSeries.put(lowercaseName, ser);
			return ser;
		} else {
			throw new UnknownSeriesException("No series with name " + name + " defined");
		}
	}

	public IExpression<ISeries> asIExpression(final String expression) {
		return parse(expression).asExpression(this);
	}

	public ISeries asSeries(final String expression) {
		return parse(expression).asExpression(this).evaluate();
	}

	public ASTNode parse(final String expression) {
		final ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
		final Parser parser = new Parser(new Lexer(new StringReader(expression), symbolFactory));
		try {
			parser.setSeriesParser(this);
			parser.setSeriesParserData(seriesParserData);

			final Symbol parse = parser.parse();
			if (parse == null || parse.value == null) {
				if (!parser.errors.isEmpty()) {
					final Symbol s = parser.errors.get(0);
					throw new RuntimeException("Syntax error after: " + expression.substring(0, s.right - 1));
				} else {
					throw new RuntimeException("Syntax error: " + expression);
				}
			}

			return (ASTNode) parse.value;
		} catch (final ClassCastException e) {
			throw new RuntimeException("Syntax error ", e);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException("Error parsing expression", e);
		}
	}

	public void addConstant(final String name, final SeriesType seriesType, final Number value) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(name, seriesType, new ConstantSeriesExpression(value).evaluate());
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesData(final String name, final SeriesType seriesType, final ISeries series) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(name, seriesType, series);
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesData(final String name, final ISeriesContainer seriesContainer) {
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
		// Invalidate any pre-evaluated expression curves as we may have changed the
		// underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	private void addSeriesContainerToEvaluatedSeries(final String name, final ISeriesContainer seriesContainer) {
		evaluatedSeries.put(name.toLowerCase(), seriesContainer);
		// Invalidate any pre-evaluated expression curves as we may have changed the
		// underlying data
		for (final String expr : expressionCurves) {
			evaluatedSeries.remove(expr);
		}
	}

	public void addSeriesData(final @NonNull String name, final @NonNull SeriesType seriesType, final int[] points, final @NonNull Number[] values) {
		final ISeriesContainer seriesContainer = new DefaultSeriesContainer(name, seriesType, new ISeries() {

			@Override
			public boolean isParameterised() {
				return false;
			}

			@Override
			public Set<String> getParameters() {
				return Collections.emptySet();
			}

			@Override
			public int[] getChangePoints() {
				return points;
			}

			@Override
			public Number evaluate(final int timePoint, final Map<String, String> params) {
				int pos = SeriesUtil.floor(points, timePoint);
				if (pos == -1) {
					return 0;
				}
				return values.length == 0 ? 0 : values[pos];
			}
		});
		addSeriesContainerToEvaluatedSeries(name, seriesContainer);
	}

	public void addSeriesExpression(final String name, final SeriesType seriesType, final String expression) {
		// Register as an expression curve.
		if (!expressionCurves.add(name.toLowerCase())) {
			// Invalidate any pre-evaluated expression curves as we may have changed the
			// underlying data
			for (final String expr : expressionCurves) {
				evaluatedSeries.remove(expr);
			}
		}
		unevaluatedSeries.put(name.toLowerCase(), Pair.of(expression, seriesType));
	}

	public SeriesParserData getSeriesParserData() {
		return seriesParserData;
	}

	/** Used by the parser to determine which node type the name belongs to */
	public NamedSeriesASTNode getNamedSeriesASTNode(final String name) {
		final ISeriesContainer series = getSeries(name);
		return switch (series.getType()) {
		case COMMODITY -> new CommoditySeriesASTNode(name);
		case CURRENCY -> new CurrencySeriesASTNode(name);
		case CHARTER -> new CharterSeriesASTNode(name);
		case BUNKERS -> new BunkersSeriesASTNode(name);
		case CONVERSION -> new ConversionASTNode(name);
		case PRICING_BASIS -> new PricingBasisSeriesASTNode(name);
		default -> throw new IllegalArgumentException();
		};
	}
}
