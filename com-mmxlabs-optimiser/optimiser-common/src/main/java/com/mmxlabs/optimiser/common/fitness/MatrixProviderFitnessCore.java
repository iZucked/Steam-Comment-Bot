package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

/**
 * {@link IFitnessCore} implementation to minimise total matrix value.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class MatrixProviderFitnessCore<T> implements IFitnessCore<T> {

	/**
	 * The source matrix data (x/from, y/to)
	 */
	private IMatrixProvider<T, Number> matrix;

	/**
	 * Map containing proposed fitness value for each {@link IResource} for
	 * current {@link ISequences}
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

	private final IFitnessComponent<T> component;

	/**
	 * The key used to obtain the matrix from an {@link IOptimisationData}
	 * instance.
	 */
	private final String matrixProviderKey;

	public MatrixProviderFitnessCore(final String componentName,
			final String matrixProviderKey) {
		component = new MatrixProviderFitnessComponent<T>(componentName, this);
		this.matrixProviderKey = matrixProviderKey;
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Copy across proposed values into accepted values
		oldFitness = newFitness;

		// Only need to copy affected resource values
		for (final IResource resource : affectedResources) {
			final Long value = newFitnessByResource.get(resource);
			oldFitnessByResource.put(resource, value);
		}
	}

	@Override
	public void evaluate(final ISequences<T> sequences) {

		// Perform a full evaluation
		oldFitness = 0;

		for (final IResource resource : sequences.getResources()) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final long value = evaluateSequence(sequence);
			oldFitnessByResource.put(resource, value);
			oldFitness += value;
		}

		// Update the newFitness value as this is used in getFitness()
		newFitness = oldFitness;
	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Reset this value
		newFitness = oldFitness;

		for (final IResource resource : affectedResources) {
			// Subtract old resource fitness from total
			final long oldValue = oldFitnessByResource.get(resource);
			newFitness -= oldValue;

			// Calculate new resource fitness
			final ISequence<T> sequence = sequences.getSequence(resource);
			final long value = evaluateSequence(sequence);

			// Save resource fitness
			newFitnessByResource.put(resource, value);

			// Update total fitness
			newFitness += value;
		}
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return Collections.singleton(component);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(final IOptimisationData<T> data) {
		oldFitnessByResource = new HashMap<IResource, Long>();
		newFitnessByResource = new HashMap<IResource, Long>();
		setMatrix(data.getDataComponentProvider(matrixProviderKey,
				IMatrixProvider.class));
	}

	private long evaluateSequence(final ISequence<T> sequence) {

		// loop over sequence accumulating value
		double totalValue = 0.0;

		T prev = null;
		for (final T e : sequence) {
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
	 * Set the fitness scale factor. A full evaluation needs to be performed
	 * after calling this method.
	 * 
	 * @param scaleFactor
	 */
	public void setScaleFactor(final double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public IMatrixProvider<T, Number> getMatrix() {
		return matrix;
	}

	public void setMatrix(final IMatrixProvider<T, Number> matrix) {
		this.matrix = matrix;
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
}
