/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * Fitness component which accumulates port costs associated with sequences.
 * 
 * @author hinton
 * 
 */
public class PortCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent {

	@Inject
	private IPortCostProvider portCostProvider;
	@Inject
	private IVesselProvider vesselProvider;

	private IVessel currentVessel;
	private long sequenceAccumulator = 0;

	public PortCostFitnessComponent(final String name, final IFitnessCore core) {
		super(name, core);
	}

	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		currentVessel = vesselProvider.getVessel(resource);
		sequenceAccumulator = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails details = (PortDetails) object;
			final IPortSlot slot = details.getOptions().getPortSlot();
			final long portCost = portCostProvider.getPortCost(slot.getPort(), currentVessel, slot.getPortType());
			sequenceAccumulator += portCost;
		}

		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		currentVessel = null;
		return sequenceAccumulator / Calculator.ScaleFactor;
	}
}
