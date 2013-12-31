/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class RouteCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent {

	@Inject
	private IRouteCostProvider routeCostProvider;
	@Inject
	private IVesselProvider vesselProvider;

	public RouteCostFitnessComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	private IVesselClass vesselClass;
	private long accumulator = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		vesselClass = vesselProvider.getVessel(resource).getVesselClass();
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
			final String route = details.getOptions().getRoute();
			final VesselState vesselState = details.getOptions().getVesselState();
			// addDiscountedValue(startTime,
			// routeCostProvider.getRouteCost(route, vesselClass, vesselState));
			accumulator += getDiscountedValue(time, routeCostProvider.getRouteCost(route, vesselClass, vesselState));
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
