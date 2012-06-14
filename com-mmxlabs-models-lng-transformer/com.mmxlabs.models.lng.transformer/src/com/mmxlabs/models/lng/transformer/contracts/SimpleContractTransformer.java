/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {
	private ISchedulerBuilder builder;
	private SimpleContractBuilder contractBuilder;
	private ModelEntityMap map;
	private MMXRootObject rootObject;
	private final Collection<EClass> handledClasses
		= Arrays.asList(
				CommercialPackage.eINSTANCE.getFixedPriceContract(),
				CommercialPackage.eINSTANCE.getIndexPriceContract());
	@Override
	public void startTransforming(final MMXRootObject rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.map = map;
		this.builder = builder;
		this.contractBuilder = new SimpleContractBuilder();
		builder.addBuilderExtension(contractBuilder);
	}

	@Override
	public void finishTransforming() {
		this.rootObject = null;
		this.map = null;
		this.builder = null;
		this.contractBuilder = null;
	}

	private SimpleContract instantiate(final Contract c) {
		if (c instanceof FixedPriceContract) {
			return contractBuilder.createFixedPriceContract(
					Calculator.scaleToInt(((FixedPriceContract) c).getPricePerMMBTU()));
		} else if (c instanceof IndexPriceContract) {
			return contractBuilder.createMarketPriceContract(map.getOptimiserObject(((IndexPriceContract) c).getIndex(), ICurve.class),
					Calculator.scaleToInt(((IndexPriceContract) c).getConstant()), Calculator.scaleToInt(((IndexPriceContract) c).getMultiplier()));
		}
		return null;
	}
	
	@Override
	public IShippingPriceCalculator transformSalesContract(final SalesContract sc) {
		return instantiate(sc);
	}

	@Override
	public ILoadPriceCalculator2 transformPurchaseContract(final PurchaseContract pc) {
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
}
