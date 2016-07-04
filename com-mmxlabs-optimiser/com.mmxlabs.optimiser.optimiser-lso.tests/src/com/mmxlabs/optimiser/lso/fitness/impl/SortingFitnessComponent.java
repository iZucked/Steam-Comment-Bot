/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * {@link IFitnessComponent} for the {@link SortingFitnessCore}
 * 
 * @author Simon Goodall
 */
public final class SortingFitnessComponent implements IFitnessComponent {

	@NonNull
	public static final String COMPONENT_NAME = "Sorting Fitness";

	@NonNull
	private final SortingFitnessCore core;

	public SortingFitnessComponent(@NonNull final SortingFitnessCore core) {
		this.core = core;
	}

	@Override
	public long getFitness() {

		return core.getSortingFitness();
	}

	@Override
	@NonNull
	public IFitnessCore getFitnessCore() {

		return core;
	}

	@Override
	@NonNull
	public String getName() {

		return COMPONENT_NAME;
	}
}
