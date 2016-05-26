/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
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

	@Inject
	private IVesselProvider vesselProvider;

	private long accumulator = 0;

	private boolean includeSequence = true;

	private ProfitAndLossSequences profitAndLossSequences;

	/**
	 */
	public ProfitAndLossAllocationComponent(final @NonNull String name, final @NonNull CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation(@NonNull final ProfitAndLossSequences profitAndLossSequences) {
		this.profitAndLossSequences = profitAndLossSequences;
		accumulator = 0;
	}

	@Override
	public void startSequence(final IResource resource) {
		@NonNull
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Exclude round trip cargo P&L
		includeSequence = vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP;
	}

	@Override
	public boolean nextVoyagePlan(@NonNull final VoyagePlan voyagePlan, final int time) {

		if (includeSequence) {
			final long value = profitAndLossSequences.getVoyagePlanGroupValue(voyagePlan);
			accumulator -= value;
		}
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
	public boolean evaluateUnusedSlots(@NonNull final List<@NonNull ISequenceElement> unusedElements, @NonNull final ProfitAndLossSequences profitAndLossSequences) {

		for (final ISequenceElement element : unusedElements) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			assert portSlot != null;
			accumulator -= profitAndLossSequences.getUnusedSlotGroupValue(portSlot);
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
		profitAndLossSequences = null;
		return setLastEvaluatedFitness(accumulator / Calculator.ScaleFactor);
	}
}
