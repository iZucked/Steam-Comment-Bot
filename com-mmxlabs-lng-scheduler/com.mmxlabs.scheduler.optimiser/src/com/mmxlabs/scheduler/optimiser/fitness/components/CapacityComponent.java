/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long accumulator = 0;

	public CapacityComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(@NonNull final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(@NonNull final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;

			accumulator += scheduledSequences.getCapacityViolationCount(detail.getOptions().getPortSlot());
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

		return accumulator;
	}
}
