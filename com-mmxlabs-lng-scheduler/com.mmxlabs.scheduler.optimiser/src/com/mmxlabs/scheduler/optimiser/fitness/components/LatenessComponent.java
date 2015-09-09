/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on weighted lateness.
 * 
 * @author Alex Churchill
 * 
 */
public final class LatenessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long sequenceAccumulator = 0;

	private long promptLateness = 0;
	private long totalLateness = 0;
	private long initialPromptLateness = 0;
	private long initialTotalLateness = 0;
	private boolean hasBeenInitialised = false;
	private Set<IPortSlot> lateSlots = null;
	private boolean newLateness = false;
	@Override
	public void startEvaluation(@NonNull ScheduledSequences scheduledSequences) {
		promptLateness = 0;
		totalLateness = 0;
		super.startEvaluation(scheduledSequences);
	}
	
	public LatenessComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
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
			sequenceAccumulator += scheduledSequences.getWeightedLatenessCost(detail.getOptions().getPortSlot());
			if (lateSlots != null && scheduledSequences.isLateSlot(detail.getOptions().getPortSlot()) && !lateSlots.contains(detail.getOptions().getPortSlot())) {
				newLateness = true;
			}
			Pair<Interval, Long> latenessDetails = scheduledSequences.getLatenessCost(detail.getOptions().getPortSlot());
			if (latenessDetails != null) {
				long lateness = latenessDetails.getSecond();
				if (latenessDetails.getFirst() == Interval.PROMPT) {
					promptLateness += lateness;
				}
				totalLateness += lateness;
			}
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
		if (initialised() && ((promptLateness > getInitialPromptLateness() || totalLateness > getInitialTotalLateness()) || newLateness == true)) {
			newLateness = false;
			return Long.MAX_VALUE;
		}

		return sequenceAccumulator;
	}

	@Override
	public long endEvaluationAndGetCost() {
		if (!initialised()) {
			setInitialPromptLateness(promptLateness);
			setInitialTotalLateness(totalLateness);
			lateSlots = scheduledSequences.getLateSlotsSet();
			setInitialised(true);
		}
		return super.endEvaluationAndGetCost();
	}

	private void setInitialPromptLateness(long lateness) {
		initialPromptLateness = lateness;
	}

	private void setInitialTotalLateness(long lateness) {
		initialTotalLateness = lateness;
	}

	private long getInitialPromptLateness() {
		return initialPromptLateness;
	}

	private long getInitialTotalLateness() {
		return initialTotalLateness;
	}

	private boolean initialised() {
		return hasBeenInitialised;
	}

	private void setInitialised(boolean initialised) {
		hasBeenInitialised = initialised;
	}
}
