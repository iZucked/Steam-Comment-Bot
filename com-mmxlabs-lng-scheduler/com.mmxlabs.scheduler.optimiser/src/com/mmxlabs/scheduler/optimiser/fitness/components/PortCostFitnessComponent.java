/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * Fitness component which accumulates port costs associated with sequences.
 * 
 * @author hinton
 * 
 */
public class PortCostFitnessComponent extends AbstractPerRouteSchedulerFitnessComponent {

	private long sequenceAccumulator = 0;

	public PortCostFitnessComponent(final String name, final IFitnessCore core) {
		super(name, core);
	}

	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		sequenceAccumulator = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails details = (PortDetails) object;
			sequenceAccumulator += details.getPortCosts();
		}

		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		return sequenceAccumulator / Calculator.ScaleFactor;
	}
}
