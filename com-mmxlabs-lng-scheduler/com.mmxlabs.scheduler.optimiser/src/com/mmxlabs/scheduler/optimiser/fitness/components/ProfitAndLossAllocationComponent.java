/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
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
	public boolean nextVoyagePlan(@NonNull final VoyagePlan voyagePlan, final int time) {

		final long value = profitAndLossSequences.getVoyagePlanGroupValue(voyagePlan);
		accumulator -= value;
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

		accumulator -= unusedElements.stream() //
				.map(portSlotProvider::getPortSlot) //
				.mapToLong(profitAndLossSequences::getUnusedSlotGroupValue) //
				.sum();

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
