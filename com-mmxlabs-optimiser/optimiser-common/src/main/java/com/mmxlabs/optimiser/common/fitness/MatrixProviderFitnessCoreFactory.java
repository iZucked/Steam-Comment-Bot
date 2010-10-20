/**
 * Copyright (C) Minimax Labs Ltd., 2010
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
public final class MatrixProviderFitnessCoreFactory implements
		IFitnessCoreFactory {

	private final String fitnessCoreName;

	private final String fitnessComponentName;

	private final String dataProviderKey;

	public MatrixProviderFitnessCoreFactory(final String fitnessCoreName,
			final String fitnessComponentName, final String dataProviderKey) {
		this.fitnessCoreName = fitnessCoreName;
		this.fitnessComponentName = fitnessComponentName;
		this.dataProviderKey = dataProviderKey;
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
	public <T> IFitnessCore<T> instantiate() {
		return new MatrixProviderFitnessCore<T>(fitnessComponentName,
				dataProviderKey);
	}
}
