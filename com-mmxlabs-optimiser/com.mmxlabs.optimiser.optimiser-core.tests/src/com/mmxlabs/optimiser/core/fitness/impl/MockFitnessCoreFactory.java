/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * Mock implementation of an {@link IFitnessCoreFactory} which just return the core name and a {@link Collection} of component names.
 * 
 * @author Simon Goodall
 * 
 */
public final class MockFitnessCoreFactory implements IFitnessCoreFactory {

	@NonNull
	private final String coreName;

	@NonNull
	private final Collection<String> componentNames;

	public MockFitnessCoreFactory(@NonNull final String coreName, @NonNull final Collection<String> componentNames) {
		this.coreName = coreName;
		this.componentNames = componentNames;
	}

	@Override
	@NonNull
	public Collection<String> getFitnessComponentNames() {
		return componentNames;
	}

	@Override
	@NonNull
	public String getFitnessCoreName() {
		return coreName;
	}

	@SuppressWarnings("null")
	@Override
	@NonNull
	public IFitnessCore instantiate() {
		return new MockFitnessCore(componentNames.iterator().next());
	}
}