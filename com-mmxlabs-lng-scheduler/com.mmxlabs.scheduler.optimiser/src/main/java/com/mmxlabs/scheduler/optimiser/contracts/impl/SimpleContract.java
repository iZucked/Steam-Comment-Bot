/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class SimpleContract implements ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {

	@Override
	public void prepareEvaluation(final ISequences sequences) {
	}

	/**
	 * @since 2.0
	 */
	protected abstract int calculateSimpleUnitPrice(final int loadTime);

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations) {
		return calculateSimpleUnitPrice(loadTime);
	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption option, final int time) {
		return calculateSimpleUnitPrice(time);
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleUnitPrice(time);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateCooldownUnitPrice(final int time) {
		return calculateSimpleUnitPrice(time);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int salesPricePerMMBTu, final long transferVolume,
			final IDetailTree annotations) {
		return calculateSimpleUnitPrice(transferTime);
	}
}
