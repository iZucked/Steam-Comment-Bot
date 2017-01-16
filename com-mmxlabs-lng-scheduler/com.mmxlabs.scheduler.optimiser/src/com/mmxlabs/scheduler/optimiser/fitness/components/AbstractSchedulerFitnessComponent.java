/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@NonNull
	private final IFitnessCore core;

	@NonNull
	private final String name;

	@NonNull
	private ICurve discountCurve = new ConstantValueCurve(1);

	@Inject(optional = true)
	@Nullable
	private IDiscountCurveProvider discountCurveProvider;

	protected AbstractSchedulerFitnessComponent(@NonNull final String name, @NonNull final IFitnessCore core) {
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
	@NonNull
	public final String getName() {
		return name;
	}

	@Override
	@NonNull
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
		IDiscountCurveProvider pDiscountCurveProvider = discountCurveProvider;
		if (pDiscountCurveProvider != null) {
			ICurve curve = pDiscountCurveProvider.getDiscountCurve(getName());
			if (curve != null) {
				discountCurve = curve;
			}
		}
	}
}
