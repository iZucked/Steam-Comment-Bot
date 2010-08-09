package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

/**
 * Abstract implementation of {@link ICargoSchedulerFitnessComponent}
 * implementing the common parts of the interface.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public abstract class AbstractCargoSchedulerFitnessComponent<T> implements
		ICargoSchedulerFitnessComponent<T> {

	/**
	 * Full fitness of last confirmed {@link ISequences}
	 */
	protected long oldFitness;

	/**
	 * Full fitness of currently evaluated {@link ISequences}
	 */
	protected long newFitness;

	/**
	 * Per resource fitness of last confirmed {@link ISequences}
	 */
	protected Map<IResource, Long> oldFitnessByResource;

	/**
	 * Per resource fitness of currently evaluated {@link ISequences}
	 */
	protected Map<IResource, Long> newFitnessByResource;

	private final CargoSchedulerFitnessCore<T> core;

	private final String name;

	protected AbstractCargoSchedulerFitnessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		this.name = name;
		this.core = core;

		oldFitnessByResource = new HashMap<IResource, Long>();
		newFitnessByResource = new HashMap<IResource, Long>();
	}

	@Override
	public long getFitness() {
		return newFitness;
	}

	@Override
	public IFitnessCore<T> getFitnessCore() {
		return core;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public abstract void init(IOptimisationData<T> data);

	@Override
	public abstract void evaluateSequence(IResource resource,
			ISequence<T> sequence, IAnnotatedSequence<T> annotatedSequence,
			boolean newSequence);

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

	protected void updateFitness(final IResource resource, final long fitness,
			final boolean newSequence) {
		if (newSequence) {
			final long oldValue = newFitnessByResource.get(resource);
			newFitness -= oldValue;

			// Save resource fitness
			newFitnessByResource.put(resource, fitness);
			newFitness += fitness;

		} else {
			oldFitnessByResource.put(resource, fitness);
			oldFitness += fitness;
		}
	}

	@Override
	public void prepare() {
		oldFitness = 0;
		oldFitnessByResource.clear();
	}

	@Override
	public void prepareDelta() {
		complete();
	}
	
	@Override
	public void complete() {

		// Copy across proposed values into accepted values
		newFitness = oldFitness;

		for (final Map.Entry<IResource, Long> entry : oldFitnessByResource
				.entrySet()) {
			final IResource key = entry.getKey();
			newFitnessByResource.put(key, oldFitnessByResource.get(key));
		}
	}

	@Override
	public void dispose() {
		oldFitnessByResource.clear();
		newFitnessByResource.clear();
	}
}
