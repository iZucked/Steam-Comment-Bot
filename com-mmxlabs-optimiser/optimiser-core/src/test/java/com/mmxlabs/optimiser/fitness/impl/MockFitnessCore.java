package com.mmxlabs.optimiser.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

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
	public void evaluate(final ISequences<T> sequences) {

	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return Collections.singleton(component);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}
}
