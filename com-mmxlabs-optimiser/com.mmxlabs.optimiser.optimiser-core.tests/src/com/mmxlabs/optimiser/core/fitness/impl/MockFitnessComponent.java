/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

public final class MockFitnessComponent implements IFitnessComponent {
	@NonNull
	private final String name;

	@NonNull
	private final IFitnessCore core;

	public MockFitnessComponent(@NonNull final String name, @NonNull final IFitnessCore core) {
		this.name = name;
		this.core = core;
	}

	@Override
	public long getFitness() {
		return 0;
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
