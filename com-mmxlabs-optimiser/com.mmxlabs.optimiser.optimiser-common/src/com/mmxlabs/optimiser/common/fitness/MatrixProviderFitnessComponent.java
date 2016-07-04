/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * Simple {@link IFitnessComponent} implementation for a {@link MatrixProviderFitnessCore}.
 * 
 * @author Simon Goodall
 * 
 */
public final class MatrixProviderFitnessComponent implements IFitnessComponent {
	@NonNull
	private final String name;

	@NonNull
	private final MatrixProviderFitnessCore core;

	public MatrixProviderFitnessComponent(@NonNull final String name, @NonNull final MatrixProviderFitnessCore core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return core.getNewFitness();
	}

	@Override
	@NonNull
	public IFitnessCore getFitnessCore() {
		return core;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}
}
