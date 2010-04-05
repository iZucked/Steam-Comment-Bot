package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessCoreFactory;

/**
 * {@link IFitnessCoreFactory} for the {@link SortingFitnessCore}
 * 
 * @author Simon Goodall
 * 
 */
public final class SortingFitnessFactory implements IFitnessCoreFactory {

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(SortingFitnessComponent.COMPONENT_NAME);
	}

	@Override
	public String getFitnessCoreName() {

		return SortingFitnessCore.CORE_NAME;
	}

	@Override
	public <T> IFitnessCore<T> instantiate() {
	
		// TODO: NEED TO somehow only allow integer instantiations 
		
		return new SortingFitnessCore<T>();
	}

}
