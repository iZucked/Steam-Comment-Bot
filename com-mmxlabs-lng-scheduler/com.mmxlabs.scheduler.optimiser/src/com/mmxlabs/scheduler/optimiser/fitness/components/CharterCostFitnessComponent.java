/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;

public class CharterCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	@Inject
	private IVesselProvider vesselProvider;

	public CharterCostFitnessComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
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
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// FIXME: Use VoyagePlans to get charter in rate
		final ICurve hireRate;
		switch (vesselAvailability.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vesselAvailability.getDailyCharterInRate();
			break;
		case TIME_CHARTER:
			hireRate = vesselAvailability.getDailyCharterInRate();
			break;
		case CARGO_SHORTS:
			hireRate = vesselAvailability.getDailyCharterInRate();
			break;
		default:
			// we are not interested in this sequence - we won't
			// be bothered for the rest of it.
			return false;
		}

		charterPrice = hireRate;
		firstLoadTime = -1;
		lastTime = -1;

		if (vesselAvailability.getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER)) {
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

		// TODO: UTC!
		return ((firstLoadTime == -1) || (lastTime == -1)) ? 0 : getDiscountedValue(firstLoadTime,
				Calculator.quantityFromRateTime(charterPrice.getValueAtPoint(firstLoadTime), lastTime - firstLoadTime))
				/ 24L / Calculator.ScaleFactor;
	}
}
