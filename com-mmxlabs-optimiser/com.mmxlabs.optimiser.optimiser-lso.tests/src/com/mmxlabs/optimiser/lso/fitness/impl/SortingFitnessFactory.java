/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

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
	public IFitnessCore instantiate() {

		return new SortingFitnessCore();
	}

}
