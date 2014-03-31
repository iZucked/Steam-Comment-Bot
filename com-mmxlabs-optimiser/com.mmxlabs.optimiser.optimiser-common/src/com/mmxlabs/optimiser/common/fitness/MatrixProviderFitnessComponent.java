/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * Simple {@link IFitnessComponent} implementation for a {@link MatrixProviderFitnessCore}.
 * 
 * @author Simon Goodall
 * 
 */
public final class MatrixProviderFitnessComponent implements IFitnessComponent {

	private final String name;
	private final MatrixProviderFitnessCore core;

	public MatrixProviderFitnessComponent(final String name, final MatrixProviderFitnessCore core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return core.getNewFitness();
	}

	@Override
	public IFitnessCore getFitnessCore() {
		return core;
	}

	@Override
	public String getName() {
		return name;
	}
}
