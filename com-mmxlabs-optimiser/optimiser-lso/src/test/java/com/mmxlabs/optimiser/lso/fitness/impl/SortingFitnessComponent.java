package com.mmxlabs.optimiser.lso.fitness.impl;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * {@link IFitnessComponent} for the {@link SortingFitnessCore}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class SortingFitnessComponent<T> implements IFitnessComponent<T> {

	public static final String COMPONENT_NAME = "Sorting Fitness";

	private final SortingFitnessCore<T> core;

	public SortingFitnessComponent(final SortingFitnessCore<T> core) {
		this.core = core;
	}

	@Override
	public long getFitness() {

		return core.getSortingFitness();
	}

	@Override
	public IFitnessCore<T> getFitnessCore() {

		return core;
	}

	@Override
	public String getName() {

		return COMPONENT_NAME;
	}
}
