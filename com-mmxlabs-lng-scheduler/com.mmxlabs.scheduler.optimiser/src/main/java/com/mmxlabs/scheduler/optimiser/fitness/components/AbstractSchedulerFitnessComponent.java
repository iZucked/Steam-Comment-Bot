/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

/**
 * @author hinton
 *
 */
public abstract class AbstractSchedulerFitnessComponent<T> implements
		ICargoSchedulerFitnessComponent<T> {
	private final IFitnessCore<T> core;
	private final String name;
	
	protected AbstractSchedulerFitnessComponent(final String name, final IFitnessCore<T> core) {
		super();
		this.core = core;
		this.name = name;
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
	public void init(IOptimisationData<T> data) {
	}
}
