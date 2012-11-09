/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.FixedPriceContract;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
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
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {

	private ModelEntityMap map;

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getFixedPriceContract(), CommercialPackage.eINSTANCE.getIndexPriceContract());

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
			return createFixedPriceContract(OptimiserUnitConvertor.convertToInternalConversionFactor(((FixedPriceContract) c).getPricePerMMBTU()));
		} else if (c instanceof IndexPriceContract) {
			return createMarketPriceContract(map.getOptimiserObject(((IndexPriceContract) c).getIndex(), ICurve.class),
					OptimiserUnitConvertor.convertToInternalConversionFactor(((IndexPriceContract) c).getConstant()),
					OptimiserUnitConvertor.convertToInternalConversionFactor(((IndexPriceContract) c).getMultiplier()));
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

}
