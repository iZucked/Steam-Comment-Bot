/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

/**
 * {@link IFitnessCoreFactory} implementation for the {@link IMatrixProvider}s
 * 
 * @author Simon Goodall
 * 
 */
public final class MatrixProviderFitnessCoreFactory implements IFitnessCoreFactory {

	private final String fitnessCoreName;

	private final String fitnessComponentName;

	public MatrixProviderFitnessCoreFactory(final String fitnessCoreName, final String fitnessComponentName) {
		this.fitnessCoreName = fitnessCoreName;
		this.fitnessComponentName = fitnessComponentName;
	}

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(fitnessComponentName);
	}

	@Override
	public String getFitnessCoreName() {
		return fitnessCoreName;
	}

	@Override
	public IFitnessCore instantiate() {
		return new MatrixProviderFitnessCore(fitnessComponentName);
	}
}
