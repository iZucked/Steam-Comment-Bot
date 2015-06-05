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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long sequenceAccumulator = 0;
	private long initialSum = 0;
	private long totalViolations = 0;
	private boolean hasBeenInitialised = false;

	public CapacityComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation(@NonNull ScheduledSequences scheduledSequences) {
		totalViolations = 0;
		super.startEvaluation(scheduledSequences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(@NonNull final IResource resource) {
		sequenceAccumulator = 0;
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

			sequenceAccumulator += scheduledSequences.getCapacityViolationCount(detail.getOptions().getPortSlot());
			totalViolations += scheduledSequences.getCapacityViolationCount(detail.getOptions().getPortSlot());
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
		if (initialised() && (totalViolations > initialSum)) {
			return Long.MAX_VALUE;
		}

		return sequenceAccumulator;
	}
	
	@Override
	public long endEvaluationAndGetCost() {
		if (!initialised()) {
			initialSum = totalViolations;
			setInitialised(true);
		}
		return super.endEvaluationAndGetCost();
	}

	private boolean initialised() {
		return hasBeenInitialised;
	}

	private void setInitialised(boolean initialised) {
		hasBeenInitialised = initialised;
	}

}
