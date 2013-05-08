/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.FixedPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * 
 * 
 * @author hinton
 * @since 2.0
 * 
 */

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getFixedPriceParameters(), CommercialPackage.eINSTANCE.getExpressionPriceParameters(),
			CommercialPackage.eINSTANCE.getSalesContract(), CommercialPackage.eINSTANCE.getPurchaseContract());

	@Inject
	private SeriesParser indices;

	/**
	 * @since 3.0
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
	}

	@Override
	public void finishTransforming() {
	}

	private SimpleContract instantiate(final LNGPriceCalculatorParameters priceInfo) {
		if (priceInfo instanceof FixedPriceParameters) {
			final FixedPriceParameters fixedPriceInfo = (FixedPriceParameters) priceInfo;
			return createFixedPriceContract(OptimiserUnitConvertor.convertToInternalConversionFactor(fixedPriceInfo.getPricePerMMBTU()));
		} else if (priceInfo instanceof ExpressionPriceParameters) {
			final ExpressionPriceParameters expressionPriceInfo = (ExpressionPriceParameters) priceInfo;
			return createPriceExpressionContract(expressionPriceInfo.getPriceExpression());

		}

		return null;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(final LNGPriceCalculatorParameters priceParameters) {
		return instantiate(priceParameters);
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {

	}

	/**
	 * @return
	 * @since 3.0
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}

	private FixedPriceContract createFixedPriceContract(final int pricePerMMBTU) {
		return new FixedPriceContract(pricePerMMBTU);
	}

	/**
	 * Create an internal representation of a PriceExpressionContract from the price expression string.
	 * 
	 * @param priceExpression
	 *            A string containing a valid price expression interpretable by a {@link SeriesParser}
	 * @return An internal representation of a price expression contract for use by the optimiser
	 */
	private PriceExpressionContract createPriceExpressionContract(final String priceExpression) {
		final ICurve curve = generateExpressionCurve(priceExpression);
		return new PriceExpressionContract(curve);
	}

	private StepwiseIntegerCurve generateExpressionCurve(final String priceExpression) {
		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {

			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}
}
