/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;

/**
 * 
 * TODO add future discount curve support.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractSchedulerFitnessComponent<T> implements
		ICargoSchedulerFitnessComponent<T> {
	private final IFitnessCore<T> core;
	private final String name;
	private ICurve discountCurve = new ConstantValueCurve(1);

	protected AbstractSchedulerFitnessComponent(final String name,
			final IFitnessCore<T> core) {
		super();
		this.core = core;
		this.name = name;
	}

	protected long getDiscountedValue(final int time, final long value) {
		return (long) (discountCurve.getValueAtPoint(time) * value);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final IFitnessCore<T> getFitnessCore() {
		return core;
	}

	private long lastEvaluatedFitness;
	private long lastAcceptedFitness;

	/**
	 * Convenience method for subclasses to set the fitness and return its
	 * value.
	 * 
	 * @param lastEvaluatedFitness
	 * @return
	 */
	protected long setLastEvaluatedFitness(final long lastEvaluatedFitness) {
		this.lastEvaluatedFitness = lastEvaluatedFitness;
		return lastEvaluatedFitness;
	}

	@Override
	public long getFitness() {
		return lastEvaluatedFitness;
	}

	@Override
	public void rejectLastEvaluation() {
		lastEvaluatedFitness = lastAcceptedFitness;
	}

	@Override
	public void acceptLastEvaluation() {
		lastAcceptedFitness = lastEvaluatedFitness;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		discountCurve = data.getDataComponentProvider(
				SchedulerConstants.DCP_discountCurveProvider,
				IDiscountCurveProvider.class).getDiscountCurve(getName());
	}

	@Override
	public boolean annotateNextObject(final Object object, final int time,
			final IAnnotatedSolution<T> solution) {
		return nextObject(object, time);
	}

	@Override
	public void endEvaluationAndAnnotate(IAnnotatedSolution<T> solution) {

	}
}
