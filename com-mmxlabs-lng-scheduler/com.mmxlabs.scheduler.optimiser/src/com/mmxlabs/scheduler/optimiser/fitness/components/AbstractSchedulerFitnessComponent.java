/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;

/**
 * Base class for cargo scheduler fitness components. Provides a name, a reference to the core, evaluated & accepted fitness tracking, and a discount curve usable via
 * {@link #getDiscountedValue(int, long)}.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractSchedulerFitnessComponent implements ICargoSchedulerFitnessComponent {
	private final IFitnessCore core;
	private final String name;
	private ICurve discountCurve = new ConstantValueCurve(1);

	@Inject(optional = true)
	private IDiscountCurveProvider discountCurveProvider;

	protected AbstractSchedulerFitnessComponent(final String name, final IFitnessCore core) {
		super();
		this.core = core;
		this.name = name;
	}

	protected long getDiscountedValue(final int time, final long value) {
		final double factor = discountCurve.getValueAtPoint(time);

		final long result = (long) (value * factor);

		return result;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final IFitnessCore getFitnessCore() {
		return core;
	}

	private long lastEvaluatedFitness;
	private long lastAcceptedFitness;

	/**
	 * Convenience method for subclasses to set the fitness and return its value.
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
	public void init(@NonNull final IOptimisationData data) {
		if (discountCurveProvider != null) {
			discountCurve = discountCurveProvider.getDiscountCurve(getName());
		}
	}
}
