/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.common.curves.ICurve;
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
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;

public class CharterCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private IVesselProvider vesselProvider;

	final String vesselProviderKey;

	public CharterCostFitnessComponent(final String name, final String vesselProviderKey, final CargoSchedulerFitnessCore core) {
		super(name, core);
		this.vesselProviderKey = vesselProviderKey;
	}

	@Override
	public void init(final IOptimisationData data) {
		this.vesselProvider = data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class);
	}

	int firstLoadTime = -1;
	int lastTime;
	ICurve charterPrice;
	PortType loadPortType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components. AbstractPerRouteSchedulerFitnessComponent #reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		final ICurve hireRate;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		case CARGO_SHORTS:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		default:
			// we are not interested in this sequence - we won't
			// be bothered for the rest of it.
			return false;
		}

		charterPrice = hireRate;
		firstLoadTime = -1;
		lastTime = -1;

		if (vessel.getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
			loadPortType = PortType.Load;
		} else {
			loadPortType = PortType.Start;
		}

		// we are interested
		return charterPrice != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components. AbstractPerRouteSchedulerFitnessComponent #reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		// TODO: is this actually being called on PortDetails objects or is it now being called
		// on PortOptions objects? (and why does it always return true? 
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			final PortOptions options = detail.getOptions();
			if (options.getPortSlot().getPortType().equals(loadPortType)) {
				if (firstLoadTime == -1) {
					firstLoadTime = time;
				}
			}
			lastTime = time;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components. AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		// addDiscountedValue(firstLoadTime,
		// Calculator.multiply(lastTime - firstLoadTime, charterPrice));

		return ((firstLoadTime == -1) || (lastTime == -1)) ? 0 : getDiscountedValue(firstLoadTime,
				Calculator.quantityFromRateTime((int) charterPrice.getValueAtPoint(firstLoadTime), lastTime - firstLoadTime));
	}
}
