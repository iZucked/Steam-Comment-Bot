/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * An {@link IFitnessComponent} and {@link IFitnessCore} combined implementation which applies a penalty for unused sequence elements which are really non-optional.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public class NonOptionalSlotFitnessCore implements IFitnessCore, IFitnessComponent {

	@NonNull
	private final String name;

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	private final Set<ISequenceElement> interestingElements = new HashSet<>();

	private int lastFitness = 0;

	public NonOptionalSlotFitnessCore(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public void init(@NonNull final IOptimisationData data) {

		interestingElements.addAll(data.getSequenceElements());
		interestingElements.removeAll(optionalElementsProvider.getOptionalElements());
		// Make sure these are retained
		interestingElements.addAll(optionalElementsProvider.getSoftRequiredElements());
	}

	@Override
	public void dispose() {

	}

	@Override
	@NonNull
	public Collection<IFitnessComponent> getFitnessComponents() {
		return CollectionsUtil.<IFitnessComponent> makeArrayList(this);
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		evaluation(sequences);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		evaluation(sequences);
		return true;
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {

	}

	private void evaluation(@NonNull final ISequences sequences) {
		int fitness = 0;
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			if (interestingElements.contains(element)) {
				++fitness;
			}
		}

		for (final IResource resource : sequences.getResources()) {
			if (vesselProvider.getVesselAvailability(resource).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final ISequence sequence = sequences.getSequence(resource);
				for (final ISequenceElement element : sequence) {
					if (interestingElements.contains(element)) {
						++fitness;
					}
				}
			}
		}
		lastFitness = fitness;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public long getFitness() {
		return lastFitness;
	}

	@Override
	@NonNull
	public IFitnessCore getFitnessCore() {
		return this;
	}
}
