/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 *
 */
public abstract class SimpleContract implements ISimpleLoadPriceCalculator, ILoadPriceCalculator2, IShippingPriceCalculator {

	@Override
	public void prepareEvaluation(ScheduledSequences sequences) {
	}

	@Override
	public int calculateLoadUnitPrice(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int loadTime, int dischargeTime, int dischargePrice, int loadVolume, IVesselClass vesselClass, VoyagePlan plan) {
		return calculateSimpleLoadUnitPrice(loadTime);
	}

	@Override
	public void prepareEvaluation(ISequences sequences) {

	}

	@Override
	public int calculateUnitPrice(IPortSlot slot, int time) {
		return calculateSimpleLoadUnitPrice(time);
	}

}
