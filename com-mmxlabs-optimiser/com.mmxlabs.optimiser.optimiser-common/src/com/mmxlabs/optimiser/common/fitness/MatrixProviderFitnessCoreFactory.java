/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
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

	@NonNull
	private final String fitnessCoreName;

	@NonNull
	private final String fitnessComponentName;

	public MatrixProviderFitnessCoreFactory(@NonNull final String fitnessCoreName, @NonNull final String fitnessComponentName) {
		this.fitnessCoreName = fitnessCoreName;
		this.fitnessComponentName = fitnessComponentName;
	}

	@Override
	@NonNull
	public Collection<String> getFitnessComponentNames() {
		return CollectionsUtil.makeArrayList(fitnessComponentName);
	}

	@Override
	@NonNull
	public String getFitnessCoreName() {
		return fitnessCoreName;
	}

	@Override
	@NonNull
	public IFitnessCore instantiate() {
		return new MatrixProviderFitnessCore(fitnessComponentName);
	}
}
