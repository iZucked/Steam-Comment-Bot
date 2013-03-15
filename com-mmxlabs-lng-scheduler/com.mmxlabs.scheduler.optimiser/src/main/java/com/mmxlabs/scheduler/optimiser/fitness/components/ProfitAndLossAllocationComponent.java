/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IProfitAndLossDetails;

/**
 * Basic group P&L calculator.
 * 
 * TODO: should this be divided differently?
 * 
 * Maybe in the new design we want different P&L bits in different places, specifically charter out revenue.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossAllocationComponent extends AbstractPerRouteSchedulerFitnessComponent {
	private long accumulator = 0;

	/**
	 * @since 2.0
	 */
	public ProfitAndLossAllocationComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		if (object instanceof IProfitAndLossDetails) {
			IProfitAndLossDetails details = (IProfitAndLossDetails) object;
			// Minimising optimiser, so negate P&L
			accumulator -= details.getTotalGroupProfitAndLoss();
		}
		return true;
	}

	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator / Calculator.ScaleFactor;
	}
}
