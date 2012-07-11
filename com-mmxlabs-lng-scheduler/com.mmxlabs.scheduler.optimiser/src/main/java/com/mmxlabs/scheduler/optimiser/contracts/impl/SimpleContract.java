/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class SimpleContract implements ISimpleLoadPriceCalculator, ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {

	@Override
	public void prepareEvaluation(final ScheduledSequences sequences) {
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePrice, final int loadVolume,
			final IVessel vessel, final VoyagePlan plan, IDetailTree annotations) {
		return calculateSimpleLoadUnitPrice(loadTime);
	}

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption option, final int time) {
		return calculateSimpleLoadUnitPrice(time);
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleLoadUnitPrice(time);
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int salesPrice) {
		return calculateSimpleLoadUnitPrice(loadTime);
	}
}
