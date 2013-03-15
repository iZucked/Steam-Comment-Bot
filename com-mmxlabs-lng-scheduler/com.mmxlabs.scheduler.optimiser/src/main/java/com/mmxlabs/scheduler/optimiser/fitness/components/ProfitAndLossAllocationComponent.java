/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IProfitAndLossDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Basic group P&L fitness component
 * 
 * @author Simon Goodall
 * 
 */
public class ProfitAndLossAllocationComponent extends AbstractSchedulerFitnessComponent {

	private long accumulator = 0;

	/**
	 * @since 2.0
	 */
	public ProfitAndLossAllocationComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation() {
		accumulator = 0;

	}

	@Override
	public void startSequence(IResource resource) {

	}

	@Override
	public boolean nextVoyagePlan(VoyagePlan voyagePlan, int time) {
		return true;
	}

	@Override
	public boolean nextObject(Object object, int time) {
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
