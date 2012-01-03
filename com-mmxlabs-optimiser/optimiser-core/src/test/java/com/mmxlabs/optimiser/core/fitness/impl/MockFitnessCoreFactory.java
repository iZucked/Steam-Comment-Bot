/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * Mock implementation of an {@link IFitnessCoreFactory} which just return the core name and a {@link Collection} of component names.
 * 
 * @author Simon Goodall
 * 
 */
public final class MockFitnessCoreFactory implements IFitnessCoreFactory {

	private final String coreName;
	private final Collection<String> componentNames;

	public MockFitnessCoreFactory(final String coreName, final Collection<String> componentNames) {
		this.coreName = coreName;
		this.componentNames = componentNames;
	}

	@Override
	public Collection<String> getFitnessComponentNames() {
		return componentNames;
	}

	@Override
	public String getFitnessCoreName() {
		return coreName;
	}

	@Override
	public IFitnessCore instantiate() {
		return new MockFitnessCore(componentNames.iterator().next());
	}
}