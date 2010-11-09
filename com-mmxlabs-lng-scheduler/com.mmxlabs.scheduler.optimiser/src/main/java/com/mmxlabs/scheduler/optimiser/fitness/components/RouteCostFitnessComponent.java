/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class RouteCostFitnessComponent<T> extends
		AbstractPerRouteSchedulerFitnessComponent<T> {

	private IRouteCostProvider routeCostProvider;
	private IVesselProvider vesselProvider;
	private final String routePriceProviderKey;
	private final String vesselProviderKey;

	public RouteCostFitnessComponent(
			final String routePriceProviderKey, final String vesselProviderKey,
			final String name, final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
		this.routePriceProviderKey = routePriceProviderKey;
		this.vesselProviderKey = vesselProviderKey;
	}

	@Override
	public void init(IOptimisationData<T> data) {
		setRouteCostProvider(data.getDataComponentProvider(
				routePriceProviderKey, IRouteCostProvider.class));
		setVesselProvider(data.getDataComponentProvider(vesselProviderKey,
				IVesselProvider.class));
	}

	public IRouteCostProvider getRouteCostProvider() {
		return routeCostProvider;
	}

	public void setRouteCostProvider(IRouteCostProvider routePriceProvider) {
		this.routeCostProvider = routePriceProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}
	
	private IVesselClass vesselClass;
	private long accumulator = 0;

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		vesselClass = vesselProvider.getVessel(resource).getVesselClass();
		accumulator = 0;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object obj, final int time) {
		if (obj instanceof VoyageDetails) {
			@SuppressWarnings("unchecked")
			final VoyageDetails<T> details = (VoyageDetails<T>) obj;
			final String route = details.getOptions().getRoute();
			final VesselState vesselState = details.getOptions()
					.getVesselState();
//			addDiscountedValue(startTime, 
//					routeCostProvider.getRouteCost(route, vesselClass, vesselState));
			accumulator += routeCostProvider.getRouteCost(route, vesselClass, vesselState);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator;
	}
}
