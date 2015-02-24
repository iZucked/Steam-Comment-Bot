/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Basic group P&L fitness component
 * 
 * @author Simon Goodall
 * 
 */
public class ProfitAndLossAllocationComponent extends AbstractSchedulerFitnessComponent {

	@Inject
	private IPortSlotProvider portSlotProvider;

	private long accumulator = 0;

	private ScheduledSequences scheduledSequences;

	/**
	 */
	public ProfitAndLossAllocationComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation(@NonNull final ScheduledSequences scheduledSequences) {
		this.scheduledSequences = scheduledSequences;
		accumulator = 0;
	}

	@Override
	public void startSequence(final IResource resource) {

	}

	@Override
	public boolean nextVoyagePlan(@NonNull final VoyagePlan voyagePlan, final int time) {

		accumulator -= scheduledSequences.getVoyagePlanGroupValue(voyagePlan);
		return true;
	}

	@Override
	public boolean nextObject(final Object object, final int time) {
		return true;
	}

	@Override
	public boolean endSequence() {
		return true;
	}

	@Override
	public boolean evaluateUnusedSlots(@NonNull final List<ISequenceElement> unusedElements, @NonNull final ScheduledSequences scheduleSequences) {

		for (final ISequenceElement element : unusedElements) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			assert portSlot != null;
			accumulator -= scheduledSequences.getUnusedSlotGroupValue(portSlot);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #endEvaluationAndGetCost()
	 */
	@Override
	public long endEvaluationAndGetCost() {
		scheduledSequences = null;
		return setLastEvaluatedFitness(accumulator / Calculator.ScaleFactor);
	}
}
