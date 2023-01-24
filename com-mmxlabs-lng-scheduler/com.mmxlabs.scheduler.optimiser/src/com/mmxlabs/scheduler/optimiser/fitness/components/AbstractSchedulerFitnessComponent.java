/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

/**
 * Base class for cargo scheduler fitness components. Provides a name, a
 * reference to the core, evaluated & accepted fitness tracking.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractSchedulerFitnessComponent implements ICargoSchedulerFitnessComponent {

	private long lastEvaluatedFitness;
	private long lastAcceptedFitness;

	private final @NonNull IFitnessCore core;

	private final @NonNull String name;

	protected AbstractSchedulerFitnessComponent(@NonNull final String name, @NonNull final IFitnessCore core) {
		super();
		this.core = core;
		this.name = name;
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
}
