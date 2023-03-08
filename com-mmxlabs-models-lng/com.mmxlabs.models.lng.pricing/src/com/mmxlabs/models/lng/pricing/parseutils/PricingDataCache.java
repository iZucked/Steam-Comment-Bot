/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.BunkersSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CharterSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CurrencySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.PricingBasisSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingBasis;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

@NonNullByDefault
public final class PricingDataCache {
	private final PricingModel pricingModel;

	private final Map<String, CommodityCurve> commodityMap = new HashMap<>();
	private final Map<String, PricingBasis> pricingBases = new HashMap<>(); // Key is lowercase
	private final Map<String, CharterCurve> charterMap = new HashMap<>(); // Key is lowercase
	private final Map<String, BunkerFuelCurve> baseFuelMap = new HashMap<>(); // Key is lowercase
	private final Map<String, CurrencyCurve> currencyMap = new HashMap<>(); // Key is lowercase
	private final Map<String, UnitConversion> conversionMap = new HashMap<>(); // Key is lowercase
	private final Map<String, UnitConversion> reverseConversionMap = new HashMap<>(); // Key is lowercase

	private final Map<String, Collection<AbstractYearMonthCurve>> expressionToLinkedCurves = new HashMap<>();
	private final Map<String, ASTNode> expressionToNode = new HashMap<>();
	private final Map<String, IExpression<ISeries>> expressionToIExpression = new HashMap<>();

	private final Map<AbstractYearMonthCurve, @Nullable YearMonth> firstDateCache = new HashMap<>();
	private final Map<PriceIndexType, SeriesParser> parserCache = new EnumMap<>(PriceIndexType.class);

	private PricingDataCache(final PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	public static PricingDataCache createLookupData(final PricingModel pricingModel) {
		final PricingDataCache lookupData = new PricingDataCache(pricingModel);

		pricingModel.getCommodityCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getPricingBases().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.pricingBases.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCurrencyCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.currencyMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCharterCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.charterMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getBunkerFuelCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.baseFuelMap.put(idx.getName().toLowerCase(), idx));

		// A to B
		pricingModel.getConversionFactors().forEach(f -> {
			final String conversionFactorName = PriceIndexUtils.createConversionFactorName(f);
			if (conversionFactorName != null) {
				lookupData.conversionMap.put(conversionFactorName.toLowerCase(), f);
			}
		});
		// B to A
		pricingModel.getConversionFactors().forEach(f -> {
			final String conversionFactorName = PriceIndexUtils.createReverseConversionFactorName(f);
			if (conversionFactorName != null) {
				lookupData.reverseConversionMap.put(conversionFactorName.toLowerCase(), f);
			}
		});

		// First date per index cache
		final Function<AbstractYearMonthCurve, @Nullable YearMonth> finder = curve -> {
			if (!curve.isSetExpression()) {
				final Optional<YearMonthPoint> min = curve.getPoints()
						.stream() //
						.min((p1, p2) -> p1.getDate().compareTo(p2.getDate()));
				// No data check
				if (min.isPresent()) {
					return min.get().getDate();
				}
			}
			return null;
		};

		pricingModel.getCommodityCurves().forEach(c -> lookupData.firstDateCache.put(c, finder.apply(c)));
		pricingModel.getCharterCurves().forEach(c -> lookupData.firstDateCache.put(c, finder.apply(c)));
		pricingModel.getBunkerFuelCurves().forEach(c -> lookupData.firstDateCache.put(c, finder.apply(c)));
		pricingModel.getCurrencyCurves().forEach(c -> lookupData.firstDateCache.put(c, finder.apply(c)));
		pricingModel.getPricingBases().forEach(c -> lookupData.firstDateCache.put(c, finder.apply(c)));

		lookupData.parserCache.put(PriceIndexType.COMMODITY, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY));
		lookupData.parserCache.put(PriceIndexType.CHARTER, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.CHARTER));
		lookupData.parserCache.put(PriceIndexType.BUNKERS, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.BUNKERS));
		lookupData.parserCache.put(PriceIndexType.CURRENCY, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.CURRENCY));
		lookupData.parserCache.put(PriceIndexType.PRICING_BASIS, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.PRICING_BASIS));

		return lookupData;
	}

	public @Nullable AbstractYearMonthCurve getCurve(final PriceIndexType priceIndexType, final String name) {

		return switch (priceIndexType) {
		case BUNKERS -> baseFuelMap.get(name.toLowerCase());
		case CHARTER -> charterMap.get(name.toLowerCase());
		case COMMODITY -> commodityMap.get(name.toLowerCase());
		case CURRENCY -> currencyMap.get(name.toLowerCase());
		case PRICING_BASIS -> pricingBases.get(name.toLowerCase());
		default -> null;
		};
	}

	// Parse the expression and return the AST Node tree
	public ASTNode getASTNodeFor(final String priceExpression, final PriceIndexType priceIndexType) {
		return expressionToNode.computeIfAbsent(priceExpression, expr -> {
			final SeriesParser parser = PriceIndexUtils.getParserFor(pricingModel, priceIndexType);
			return parser.parse(expr);
		});
	}

	// Parse the expression and return the AST Node tree
	public IExpression<ISeries> getIExpressionFor(final String priceExpression, final PriceIndexType priceIndexType) {
		return expressionToIExpression.computeIfAbsent(priceExpression, expr -> {
			final SeriesParser parser = PriceIndexUtils.getParserFor(pricingModel, priceIndexType);
			return parser.asIExpression(expr);
		});
	}

	public SeriesParser getSeriesParser(final PriceIndexType type) {
		return parserCache.get(type);
	}

	public @Nullable YearMonth getEarliestDate(final AbstractYearMonthCurve curve) {
		return firstDateCache.get(curve);
	}

	public Collection<AbstractYearMonthCurve> getLinkedCurves(final @Nullable String priceExpression, final PriceIndexType type) {
		if (priceExpression == null || priceExpression.isBlank()) {
			return Collections.emptySet();
		}

		return expressionToLinkedCurves.computeIfAbsent(priceExpression, k -> {
			try {
				final ASTNode parsedExpression = parserCache.get(type).parse(priceExpression);
				return collectIndicies(parsedExpression, this);
			} catch (final Exception e) {
				return Collections.emptySet();
			}
		});
	}

	/**
	 * Computes the dependent curves and the first date needed based on the pricing date. This wil take into account function specifics e.g. the SHIFT function or DATEDAVG function.
	 * 
	 * @param priceExpression
	 * @param type
	 * @param pricingDate
	 * @return
	 */
	public Map<AbstractYearMonthCurve, LocalDate> getLinkedCurvesAndFirstDateNeeded(final String priceExpression, final PriceIndexType type, final LocalDate pricingDate) {

		// Parse the expression
		final ASTNode markedUpNode = getASTNodeFor(priceExpression, type);
		return getIndexToFirstDateNode(markedUpNode, pricingDate);
	}

	private Map<AbstractYearMonthCurve, LocalDate> getIndexToFirstDateNode(final ASTNode node, final LocalDate date) {

		if (node instanceof final ShiftFunctionASTNode shiftNode) {
			final LocalDate pricingDate = shiftNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(shiftNode.getToShift(), pricingDate);
		} else if (node instanceof final MonthFunctionASTNode monthNode) {
			final LocalDate pricingDate = monthNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(monthNode.getSeries(), pricingDate);
		} else if (node instanceof final DatedAvgFunctionASTNode averageNode) {
			final LocalDate startDate = averageNode.mapTimeToStartDate(date.atStartOfDay()).toLocalDate();
			return getIndexToFirstDateNode(averageNode.getSeries(), startDate);
		} else if (node instanceof final CommoditySeriesASTNode commodityNode) {
			return make(commodityMap.get(commodityNode.getName().toLowerCase()), date);
		} else if (node instanceof final CurrencySeriesASTNode currencyNode) {
			return make(currencyMap.get(currencyNode.getName().toLowerCase()), date);
		} else if (node instanceof final CharterSeriesASTNode charterNode) {
			return make(charterMap.get(charterNode.getName().toLowerCase()), date);
		} else if (node instanceof final BunkersSeriesASTNode bunkersNode) {
			return make(baseFuelMap.get(bunkersNode.getName().toLowerCase()), date);
		} else if (node instanceof final PricingBasisSeriesASTNode basisNode) {
			return make(pricingBases.get(basisNode.getName().toLowerCase()), date);
		} else {
			return mergeIterables(date, node.getChildren());
		}
	}

	private Map<AbstractYearMonthCurve, LocalDate> make(final AbstractYearMonthCurve c, final LocalDate d) {
		final Map<AbstractYearMonthCurve, LocalDate> m = new HashMap<>();
		m.put(c, d);
		return m;
	}

	private static final BinaryOperator<LocalDate> mergeFunc = (a, b) -> a.isBefore(b) ? a : b;

	private Map<AbstractYearMonthCurve, LocalDate> mergeIterables(final LocalDate date, final Iterable<ASTNode> iterables) {

		final Map<AbstractYearMonthCurve, LocalDate> m = new HashMap<>();
		for (final var c : iterables) {
			for (final var e : getIndexToFirstDateNode(c, date).entrySet()) {
				m.merge(e.getKey(), e.getValue(), mergeFunc);
			}
		}
		return m;
	}

	public static Set<@NonNull AbstractYearMonthCurve> collectIndicies(final ASTNode parentNode, final PricingDataCache lookupData) {
		final Set<@NonNull AbstractYearMonthCurve> s = new HashSet<>();

		if (parentNode instanceof final CommoditySeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.commodityMap.get(n.getName().toLowerCase())));
		} else if (parentNode instanceof final CharterSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.charterMap.get(n.getName().toLowerCase())));
		} else if (parentNode instanceof final BunkersSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.baseFuelMap.get(n.getName().toLowerCase())));
		} else if (parentNode instanceof final CurrencySeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.currencyMap.get(n.getName().toLowerCase())));
		} else if (parentNode instanceof final PricingBasisSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.pricingBases.get(n.getName().toLowerCase())));
		}

		for (final ASTNode child : parentNode.getChildren()) {
			s.addAll(collectIndicies(child, lookupData));
		}
		return s;
	}
}