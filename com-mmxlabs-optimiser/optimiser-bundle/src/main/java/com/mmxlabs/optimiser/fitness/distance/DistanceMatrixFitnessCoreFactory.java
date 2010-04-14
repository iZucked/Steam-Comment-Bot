package com.mmxlabs.optimiser.fitness.distance;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.Constants;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;
import com.mmxlabs.optimiser.fitness.MatrixProviderFitnessCore;

/**
 * {@link IFitnessCoreFactory} implementation for the distance matrix
 * 
 * @author Simon Goodall
 * 
 */
public final class DistanceMatrixFitnessCoreFactory implements
		IFitnessCoreFactory {

	public static final String FITNESS_CORE_NAME = "distance-matrix-core";

	public static final String FITNESS_COMPONENT_NAME = "distance";

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(FITNESS_COMPONENT_NAME);
	}

	@Override
	public String getFitnessCoreName() {
		return FITNESS_CORE_NAME;
	}

	@Override
	public <T> IFitnessCore<T> instantiate() {
		return new MatrixProviderFitnessCore<T>(FITNESS_COMPONENT_NAME,
				Constants.DATA_PROVIDER_KEY_distance_matrix);
	}
}
