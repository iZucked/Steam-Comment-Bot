package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class RouteCostFitnessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> {

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
	public long rawEvaluateSequence(IResource resource, ISequence<T> sequence,
			List<VoyagePlan> plans, final int startTime) {
		final IVesselClass vesselClass = vesselProvider
				.getVessel(resource).getVesselClass();
//		long totalCost = 0;
//
//		for (final VoyagePlan plan : plans) {
//			for (final Object obj : plan.getSequence()) {
//				if (obj instanceof VoyageDetails) {
//					final VoyageDetails<T> details = (VoyageDetails<T>) obj;
//					final String route = details.getOptions().getRoute();
//					final VesselState vesselState = details.getOptions()
//							.getVesselState();
//					totalCost += routeCostProvider.getRouteCost(route,
//							vesselClass, vesselState);
//				}
//			}
//		}

		return totalCost;
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

	@Override
	public boolean shouldIterate() {
		return true;
	}

	IVesselClass vesselClass;
	long totalCost = 0;
	
	@Override
	public void beginIterating(IResource resource) {
		totalCost = 0;
		vesselClass = vesselProvider.getVessel(resource).getVesselClass();
	}

	@Override
	public void evaluateNextObject(final Object obj, final int startTime) {
		if (obj instanceof VoyageDetails) {
			final VoyageDetails<T> details = (VoyageDetails<T>) obj;
			final String route = details.getOptions().getRoute();
			final VesselState vesselState = details.getOptions()
					.getVesselState();
			totalCost += routeCostProvider.getRouteCost(route,
					vesselClass, vesselState);
		}		
	}

	@Override
	public void endIterating() {
		
	}
}
