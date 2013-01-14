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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.FixedPriceContract;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.PriceExpressionContract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * 
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {

	private ModelEntityMap map;

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getFixedPriceContract(), CommercialPackage.eINSTANCE.getIndexPriceContract(),
			CommercialPackage.eINSTANCE.getPriceExpressionContract());

	@Inject
	private SeriesParser indices;

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ResourcelessModelEntityMap map, final ISchedulerBuilder builder) {
		this.map = map;
	}

	@Override
	public void finishTransforming() {
		this.map = null;
	}

	private SimpleContract instantiate(final Contract c) {
		if (c instanceof FixedPriceContract) {
			FixedPriceContract contract = (FixedPriceContract) c;
			return createFixedPriceContract(OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getPricePerMMBTU()));
		} else if (c instanceof IndexPriceContract) {
			IndexPriceContract contract = (IndexPriceContract) c;
			return createMarketPriceContract(map.getOptimiserObject(contract.getIndex(), ICurve.class), OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getConstant()),
					OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getMultiplier()));
		} else if (c instanceof PriceExpressionContract) {
			PriceExpressionContract contract = (PriceExpressionContract) c;
			return createPriceExpressionContract(contract.getPriceExpression());
		}
		return null;
	}

	@Override
	public ISalesPriceCalculator transformSalesContract(final SalesContract sc) {
		return instantiate(sc);
	}

	@Override
	public ILoadPriceCalculator transformPurchaseContract(final PurchaseContract pc) {
		return instantiate(pc);
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {

	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}

	com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract createFixedPriceContract(final int pricePerMMBTU) {
		return new com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract(pricePerMMBTU);
	}

	MarketPriceContract createMarketPriceContract(final ICurve index, final int offset, final int multiplier) {
		return new MarketPriceContract(index, offset, multiplier);
	}

	/**
	 * Create an internal representation of a PriceExpressionContract from the price expression string.
	 * 
	 * @param priceExpression
	 *            A string containing a valid price expression interpretable by a {@link SeriesParser}
	 * @return An internal representation of a price expression contract for use by the optimiser
	 */
	com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract createPriceExpressionContract(String priceExpression) {
		final ICurve curve = generateExpressionCurve(priceExpression);
		return new com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract(curve);
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
