/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
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
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IVesselProvider vesselProvider;

	private final Set<ISequenceElement> interestingElements = new HashSet<>();

	private int lastFitness = 0;

	public NonOptionalSlotFitnessCore(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public void init(@NonNull final IPhaseOptimisationData data) {

		interestingElements.addAll(data.getSequenceElements());
		interestingElements.removeAll(phaseOptimisationData.getOptionalElements());
		// Make sure these are retained
		interestingElements.addAll(phaseOptimisationData.getSoftRequiredElements());
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

	private void evaluation(@NonNull final ISequences sequences) {
		int fitness = 0;
		fitness += (int) sequences.getUnusedElements().stream() //
				.filter(interestingElements::contains) //
				.count();

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
