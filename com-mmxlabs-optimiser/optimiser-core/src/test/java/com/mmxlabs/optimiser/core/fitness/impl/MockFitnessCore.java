/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public final class MockFitnessCore<T> implements IFitnessCore<T> {

	private final IFitnessComponent<T> component;

	public MockFitnessCore(final String name) {
		this.component = new MockFitnessComponent<T>(name, this);
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean evaluate(final ISequences<T> sequences) {
		return true;
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		return true;
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return Collections.singleton(component);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

	@Override
	public void annotate(ISequences<T> sequences, IAnnotatedSolution<T> solution) {
		
	}
}
