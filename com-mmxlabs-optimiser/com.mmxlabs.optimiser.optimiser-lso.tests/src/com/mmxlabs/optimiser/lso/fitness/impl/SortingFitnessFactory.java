/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

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
	@NonNull
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(SortingFitnessComponent.COMPONENT_NAME);
	}

	@Override
	@NonNull
	public String getFitnessCoreName() {

		return SortingFitnessCore.CORE_NAME;
	}

	@Override
	@NonNull
	public IFitnessCore instantiate() {

		return new SortingFitnessCore();
	}

}
