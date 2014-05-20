/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IProfitAndLossDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.UnusedSlotDetails;
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

	/**
	 */
	public ProfitAndLossAllocationComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation() {
		accumulator = 0;

	}

	@Override
	public void startSequence(final IResource resource) {

	}

	@Override
	public boolean nextVoyagePlan(final VoyagePlan voyagePlan, final int time) {
		return true;
	}

	@Override
	public boolean nextObject(final Object object, final int time) {
		if (object instanceof IProfitAndLossDetails) {
			final IProfitAndLossDetails details = (IProfitAndLossDetails) object;
			// Minimising optimiser, so negate P&L
			accumulator -= details.getTotalGroupProfitAndLoss();
		}
		return true;
	}

	@Override
	public boolean endSequence() {
		return true;
	}

	@Override
	public boolean evaluateUnusedSlots(final List<ISequenceElement> unusedElements, final ScheduledSequences scheduleSequences) {

		final Map<IPortSlot, UnusedSlotDetails> unusedSlotDetailsMap = scheduleSequences.getUnusedSlotDetails();
		if (unusedSlotDetailsMap == null) {
			return true;
		}

		for (final ISequenceElement element : unusedElements) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			final UnusedSlotDetails unusedSlotDetails = unusedSlotDetailsMap.get(portSlot);
			if (unusedSlotDetails != null) {
				// Minimising optimiser, so negate P&L
				accumulator -= unusedSlotDetails.getTotalGroupProfitAndLoss();
			}
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
		return setLastEvaluatedFitness(accumulator / Calculator.ScaleFactor);
	}
}
