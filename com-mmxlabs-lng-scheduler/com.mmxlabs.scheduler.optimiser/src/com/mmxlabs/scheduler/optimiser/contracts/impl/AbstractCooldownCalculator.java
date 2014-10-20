/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;

/**
 */
public abstract class AbstractCooldownCalculator implements ICooldownCalculator {

	protected final ICurve expressionCurve;

	public AbstractCooldownCalculator(final ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	public void prepareEvaluation(ISequences sequences) {
		// TODO Auto-generated method stub
		
	}

	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		return expressionCurve.getValueAtPoint(time);
	}

	protected int calculateSimpleUnitPrice(final int time) {
		return expressionCurve.getValueAtPoint(time);
	}

	@Override
	public abstract long calculateCooldownCost(IVesselClass vesselClass, IPort port, int cv, int time);
	

}
