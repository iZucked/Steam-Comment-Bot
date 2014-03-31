/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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

public final class MockFitnessCore implements IFitnessCore {

	private final IFitnessComponent component;

	public MockFitnessCore(final String name) {
		this.component = new MockFitnessComponent(name, this);
	}

	@Override
	public void accepted(final ISequences sequences, final Collection<IResource> affectedResources) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean evaluate(final ISequences sequences) {
		return true;
	}

	@Override
	public boolean evaluate(final ISequences sequences, final Collection<IResource> affectedResources) {
		return true;
	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return Collections.singleton(component);
	}

	@Override
	public void init(final IOptimisationData data) {

	}

	@Override
	public void annotate(final ISequences sequences, final IAnnotatedSolution solution, final boolean forExport) {

	}
}
