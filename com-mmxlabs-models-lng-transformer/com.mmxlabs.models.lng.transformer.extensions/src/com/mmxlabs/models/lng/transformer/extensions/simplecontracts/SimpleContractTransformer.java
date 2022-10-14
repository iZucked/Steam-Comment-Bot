/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getExpressionPriceParameters(), CommercialPackage.eINSTANCE.getDateShiftExpressionPriceParameters(),
			CommercialPackage.eINSTANCE.getSalesContract(), CommercialPackage.eINSTANCE.getPurchaseContract());

	@Inject
	private Injector injector;

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser indices;

	@Inject
	private DateAndCurveHelper dateHelper;

	private SimpleContract instantiate(final LNGPriceCalculatorParameters priceInfo) {
		if (priceInfo instanceof final ExpressionPriceParameters expressionPriceInfo) {
			return createPriceExpressionContract(expressionPriceInfo.getPriceExpression(), dateHelper::generateParameterisedExpressionCurve);

		} else if (priceInfo instanceof final DateShiftExpressionPriceParameters expressionPriceInfo) {
			if (expressionPriceInfo.getValue() == 0) {
				// No shift set, return simple variant.
				return createPriceExpressionContract(expressionPriceInfo.getPriceExpression(), dateHelper::generateParameterisedExpressionCurve);
			} else {
				Function<ISeries, IParameterisedCurve> curveFactory;
				if (expressionPriceInfo.isSpecificDay()) {
					curveFactory = parsed -> dateHelper.generateShiftedCurve(parsed, date -> date.minusMonths(1L).withDayOfMonth(expressionPriceInfo.getValue()).withHour(0));
				} else {
					curveFactory = parsed -> dateHelper.generateShiftedCurve(parsed, date -> date.minusDays(expressionPriceInfo.getValue()));
				}
				return createPriceExpressionContract(expressionPriceInfo.getPriceExpression(), curveFactory);
			}
		}

		return null;
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable final PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}

	/**
	 * Create an internal representation of a PriceExpressionContract from the price
	 * expression string.
	 * 
	 * @param priceExpression A string containing a valid price expression
	 *                        interpretable by a {@link SeriesParser}
	 * @return An internal representation of a price expression contract for use by
	 *         the optimiser
	 */
	protected PriceExpressionContract createPriceExpressionContract(final String priceExpression) {
		return createPriceExpressionContract(priceExpression, dateHelper::generateParameterisedExpressionCurve);
	}

	private PriceExpressionContract createPriceExpressionContract(final String priceExpression, final Function<ISeries, IParameterisedCurve> curveFactory) {

		final var p = dateHelper.createCurveAndIntervals(indices, priceExpression, curveFactory);
		final PriceExpressionContract contract = new PriceExpressionContract(p.getFirst(), p.getSecond());
		injector.injectMembers(contract);
		return contract;
	}
}
