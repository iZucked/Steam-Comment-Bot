/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * {@link IFitnessComponent} for the {@link SortingFitnessCore}
 * 
 * @author Simon Goodall
 */
public final class SortingFitnessComponent implements IFitnessComponent {

	public static final String COMPONENT_NAME = "Sorting Fitness";

	private final SortingFitnessCore core;

	public SortingFitnessComponent(final SortingFitnessCore core) {
		this.core = core;
	}

	@Override
	public long getFitness() {

		return core.getSortingFitness();
	}

	@Override
	public IFitnessCore getFitnessCore() {

		return core;
	}

	@Override
	public String getName() {

		return COMPONENT_NAME;
	}
}
