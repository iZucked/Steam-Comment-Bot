/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

public class CharterCostFitnessComponent<T> extends
		AbstractPerRouteSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	private IVesselProvider vesselProvider;

	final String vesselProviderKey;

	public CharterCostFitnessComponent(final String name,
			final String vesselProviderKey,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
		this.vesselProviderKey = vesselProviderKey;
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		this.vesselProvider = data.getDataComponentProvider(vesselProviderKey,
				IVesselProvider.class);
	}

	int firstLoadTime = -1;
	int lastTime;
	int charterPrice;
	PortType loadPortType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.
	 * AbstractPerRouteSchedulerFitnessComponent
	 * #reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		if (vessel.getVesselInstanceType().equals(
				VesselInstanceType.SPOT_CHARTER)
				|| vessel.getVesselInstanceType().equals(
						VesselInstanceType.TIME_CHARTER)) {
			charterPrice = vessel.getVesselClass().getHourlyCharterInPrice();
			firstLoadTime = -1;
			lastTime = -1;

			if (vessel.getVesselInstanceType().equals(
					VesselInstanceType.SPOT_CHARTER)) {
				loadPortType = PortType.Load;
			} else {
				loadPortType = PortType.Start;
			}

			return true; // we are interested
		} else {
			return false; // we are not interested in this sequence - we won't
							// be bothered for the rest of it.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.
	 * AbstractPerRouteSchedulerFitnessComponent
	 * #reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			if (detail.getPortSlot().getPortType().equals(loadPortType)) {
				if (firstLoadTime == -1)
					firstLoadTime = time;
			}
			lastTime = time;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.
	 * AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		// addDiscountedValue(firstLoadTime,
		// Calculator.multiply(lastTime - firstLoadTime, charterPrice));

		return (firstLoadTime == -1 || lastTime == -1) ? 0
				: getDiscountedValue(firstLoadTime, Calculator.multiply(
						lastTime - firstLoadTime, charterPrice));
	}
}
