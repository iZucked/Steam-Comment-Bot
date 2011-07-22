/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * A fitness component which computes the revenue from charter-outs, and also
 * can generate potential charter-out opportunities if it's configured to do so.
 * 
 * TODO Consider details of the charter market; the spot charters might also
 * benefit from this.
 * 
 * TODO export potential charter outs, and hook the settings up to the interface
 * somewhere.
 * 
 * @author hinton
 * 
 */
public class CharterOutFitnessComponent<T> extends
		AbstractPerRouteSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {
	/**
	 * If true, look for large blocks of idle time where charter-outs could go,
	 * and assign some value to them. Otherwise do nothing.
	 */
	private boolean generateOpportunities = false;

	/**
	 * Vessel provider, for finding out whether a resource corresponds to a
	 * fleet vessel
	 */
	private IVesselProvider vesselProvider;
	/**
	 * Key for looking up {@link vesselProvider}
	 */
	private final String vesselProviderKey;
	/**
	 * The hourly charter price of the current vessel, when iterating
	 */
	private int charterOutPrice;
	/**
	 * The minimum idle time which we should consider feasible as a potential
	 * charter-out
	 */
	private int idleTimeThreshold;
	/**
	 * The proportion of the charter out price which we should assign to
	 * "potential" charter outs (as opposed to confirmed ones)
	 */
	private double generatedDiscountFactor;

	/**
	 * Accumulator for (discounted) fitness value
	 */
	private long fitnessAccumulator;

	public CharterOutFitnessComponent(final String name,
			final String vesselProviderKey, final IFitnessCore<T> core) {
		super(name, core);
		this.vesselProviderKey = vesselProviderKey;
	}

	@Override
	public void init(IOptimisationData<T> data) {
		this.vesselProvider = data.getDataComponentProvider(vesselProviderKey,
				IVesselProvider.class);
		super.init(data);
	}

	@Override
	protected boolean reallyStartSequence(IResource resource) {
		final IVessel vessel = vesselProvider.getVessel(resource);
		fitnessAccumulator = 0;

		if (vessel.getVesselInstanceType().equals(VesselInstanceType.FLEET)) {
			// fleet vessels have potential charter-out value;
			charterOutPrice = vessel.getHourlyCharterOutPrice();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof VoyageDetails) {
			@SuppressWarnings("unchecked")
			final VoyageDetails<T> voyageDetails = (VoyageDetails<T>) object;
			// check idle time is quite long, so a charter out is not ridiculous
			if (generateOpportunities
					&& voyageDetails.getIdleTime() > idleTimeThreshold) {
				// TODO this is where a market model would slot in (or maybe
				// above)

				// TODO the discount factor probably shouldn't be a double, but
				// it's not used at the moment.

				fitnessAccumulator += getDiscountedValue(time,
						Calculator.multiply(voyageDetails.getIdleTime(),
								charterOutPrice * generatedDiscountFactor));
			}
		} else if (object instanceof PortDetails) {
			final PortDetails portDetails = (PortDetails) object;
			if (portDetails.getPortSlot().getPortType()
					.equals(PortType.CharterOut)) 
				fitnessAccumulator += getDiscountedValue(time,
						Calculator.multiply(portDetails.getVisitDuration(),
								charterOutPrice));
		}
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		// result is negated because this is (potential) revenue.
		return -fitnessAccumulator;
	}
}