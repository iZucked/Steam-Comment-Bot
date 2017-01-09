/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public final class MockFitnessCore implements IFitnessCore {
	@NonNull
	private final IFitnessComponent component;

	public MockFitnessCore(@NonNull final String name) {
		this.component = new MockFitnessComponent(name, this);
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull IEvaluationState evaluationState) {
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		return true;
	}

	@Override
	@NonNull
	public Collection<IFitnessComponent> getFitnessComponents() {
		return CollectionsUtil.makeArrayList(component);
	}

	@Override
	public void init(@NonNull final IOptimisationData data) {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {

	}
}
