package com.mmxlabs.optimiser.fitness;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;

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
