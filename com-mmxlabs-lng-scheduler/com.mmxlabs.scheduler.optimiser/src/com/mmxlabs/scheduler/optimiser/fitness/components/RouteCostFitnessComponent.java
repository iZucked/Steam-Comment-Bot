/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class RouteCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent {

	public RouteCostFitnessComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	private long accumulator = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object obj, final int time) {
		if (obj instanceof VoyageDetails) {
			final VoyageDetails details = (VoyageDetails) obj;
			accumulator += getDiscountedValue(time, details.getRouteCost());
		}
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
