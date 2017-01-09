/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

/**
 * {@link IFitnessCore} implementation to minimise total matrix value.
 * 
 * @author Simon Goodall
 * 
 */
public final class MatrixProviderFitnessCore implements IFitnessCore {

	/**
	 * The source matrix data (x/from, y/to)
	 */
	@Inject
	private IMatrixProvider<ISequenceElement, Number> matrix;

	/**
	 * Map containing proposed fitness value for each {@link IResource} for current {@link ISequences}
	 */
	private Map<IResource, Long> newFitnessByResource;

	/**
	 * Last accepted fitness value for each {@link IResource}
	 */
	private Map<IResource, Long> oldFitnessByResource;

	/**
	 * Scale factor used to convert raw fitness to an integer based value
	 */
	private double scaleFactor = 1.0;

	/**
	 * Current total fitness of proposed {@link ISequences}
	 */
	private long newFitness;

	/**
	 * Current total fitness of accepted {@link ISequences}
	 */
	private long oldFitness;

	private final @NonNull IFitnessComponent component;

	public MatrixProviderFitnessCore(@NonNull final String componentName) {
		component = new MatrixProviderFitnessComponent(componentName, this);
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

		// Copy across proposed values into accepted values
		oldFitness = newFitness;

		// Only need to copy affected resource values
		final Collection<IResource> loopResources = affectedResources != null ? affectedResources : sequences.getResources();

		for (final IResource resource : loopResources) {
			final Long value = newFitnessByResource.get(resource);
			oldFitnessByResource.put(resource, value);
		}
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {

		// Perform a full evaluation
		oldFitness = 0;

		for (final IResource resource : sequences.getResources()) {
			assert resource != null;
			final ISequence sequence = sequences.getSequence(resource);
			assert sequence != null;
			final long value = evaluateSequence(sequence);
			oldFitnessByResource.put(resource, value);
			oldFitness += value;
		}

		// Update the newFitness value as this is used in getFitness()
		newFitness = oldFitness;
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		// Reset this value
		newFitness = oldFitness;

		final Collection<IResource> loopResources = affectedResources != null ? affectedResources : sequences.getResources();
		for (final IResource resource : loopResources) {
			assert resource != null;

			// Subtract old resource fitness from total
			final long oldValue = oldFitnessByResource.get(resource);
			newFitness -= oldValue;

			// Calculate new resource fitness
			final ISequence sequence = sequences.getSequence(resource);
			final long value = evaluateSequence(sequence);

			// Save resource fitness
			newFitnessByResource.put(resource, value);

			// Update total fitness
			newFitness += value;
		}
		return true;
	}

	@Override
	@NonNull
	public Collection<IFitnessComponent> getFitnessComponents() {
		return CollectionsUtil.<IFitnessComponent> makeArrayList(component);
	}

	@Override
	public void init(@NonNull final IOptimisationData data) {
		oldFitnessByResource = new HashMap<IResource, Long>();
		newFitnessByResource = new HashMap<IResource, Long>();
	}

	private long evaluateSequence(@NonNull final ISequence sequence) {

		// loop over sequence accumulating value
		double totalValue = 0.0;

		ISequenceElement prev = null;
		for (final ISequenceElement e : sequence) {
			if (prev != null) {
				totalValue += matrix.get(prev, e).doubleValue();
			}
			prev = e;
		}

		// Convert floating point to integer
		return (long) Math.ceil(scaleFactor * totalValue);
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * Set the fitness scale factor. A full evaluation needs to be performed after calling this method.
	 * 
	 * @param scaleFactor
	 */
	public void setScaleFactor(final double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public IMatrixProvider<ISequenceElement, Number> getMatrix() {
		return matrix;
	}

	public long getNewFitness() {
		return newFitness;
	}

	@Override
	public void dispose() {
		matrix = null;
		newFitnessByResource = null;
		oldFitnessByResource = null;
		// TODO: What about the component?
	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		evaluate(sequences, evaluationState);
		// Would annotate here
	}
}
