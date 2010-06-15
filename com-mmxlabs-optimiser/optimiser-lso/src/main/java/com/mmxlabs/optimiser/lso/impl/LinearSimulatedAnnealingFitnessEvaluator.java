package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.impl.Sequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * A {@link IFitnessEvaluator} implementation to apply simulated annealing to
 * the optimisation process and use a linear combination of weights to
 * {@link IFitnessComponent} values. This implementation only tracks a single
 * best state.
 * 
 * TODO: RENAME Class now it has been refactored
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class LinearSimulatedAnnealingFitnessEvaluator<T> implements
		IFitnessEvaluator<T> {

	private IFitnessHelper<T> fitnessHelper = null;

	private List<IFitnessComponent<T>> fitnessComponents = null;

	private IThresholder thresholder;

	private IFitnessCombiner fitnessCombiner;

	public IFitnessCombiner getFitnessCombiner() {
		return fitnessCombiner;
	}

	public void setFitnessCombiner(IFitnessCombiner fitnessCombiner) {
		this.fitnessCombiner = fitnessCombiner;
	}

	private ISequences<T> bestSequences = null;

	private long bestFitness = Long.MAX_VALUE;

	private ISequences<T> currentSequences = null;

	private long currentFitness = Long.MAX_VALUE;

	@Override
	public boolean evaluateSequences(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		final long totalFitness = evaluateSequencesIntern(sequences,
				affectedResources);

		// Calculate fitness delta
		final long delta = totalFitness - currentFitness;

		// If fitness change is within the threshold, then accept the change
		final boolean accept = thresholder.accept(delta);

		if (accept) {
			// Update internal state
			updateBest(sequences, totalFitness);

			// Update fitness functions state
			fitnessHelper.acceptFromComponents(fitnessComponents, sequences,
					affectedResources);
		}

		// Step to the next threshold levels
		thresholder.step();

		return accept;
	}

	/**
	 * Update internal state, storing new fitness as new current and updating
	 * best fitness if required.
	 * 
	 * @param sequences
	 * @param totalFitness
	 */
	private void updateBest(final ISequences<T> sequences,
			final long totalFitness) {

		// Store current fitness and sequences
		currentFitness = totalFitness;
		currentSequences = new Sequences<T>(sequences);

		// If this is the best state seen so far, then record it.
		if (currentFitness < bestFitness) {
			// Store this as the new best
			bestSequences = new Sequences<T>(sequences);
			bestFitness = currentFitness;
		}
	}

	/**
	 * Method used to perform the evaluation of {@link ISequences}.
	 * 
	 * @param sequences
	 * @param affectedResources
	 * @return
	 */
	private long evaluateSequencesIntern(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			fitnessHelper.evaluateSequencesFromComponents(sequences,
					fitnessComponents);
		} else {
			fitnessHelper.evaluateSequencesFromComponents(sequences,
					fitnessComponents, affectedResources);
		}

		return fitnessCombiner.calculateFitness(fitnessComponents);
	}

	@Override
	public void init() {
		if (fitnessComponents == null) {
			throw new IllegalStateException("No fitness components set");
		}

		if (fitnessHelper == null) {
			throw new IllegalStateException("No fitness helper set");
		}

		if (fitnessCombiner == null) {
			throw new IllegalStateException("No fitness combiner set");
		}

		if (thresholder == null) {
			throw new IllegalStateException("No thresholder set");
		}
	}

	@Override
	public void setOptimisationData(final IOptimisationData<T> data) {

		// Initialise the fitness functions
		fitnessHelper.initFitnessComponents(getFitnessComponents(), data);
	}

	@Override
	public void setInitialSequences(final ISequences<T> initialSequences) {

		if (initialSequences == null) {
			throw new IllegalArgumentException(
					"Initial sequences cannot be null");
		}

		final long totalFitness = evaluateSequencesIntern(initialSequences,
				null);
		bestFitness = totalFitness;
		currentFitness = totalFitness;
		bestSequences = new Sequences<T>(initialSequences);
		currentSequences = new Sequences<T>(initialSequences);

		// Setup initial conditions
		thresholder.init();
	}

	@Override
	public void setFitnessComponents(
			final List<IFitnessComponent<T>> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	@Override
	public List<IFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	@Override
	public IFitnessHelper<T> getFitnessHelper() {
		return fitnessHelper;
	}

	@Override
	public void setFitnessHelper(final IFitnessHelper<T> fitnessHelper) {
		this.fitnessHelper = fitnessHelper;
	}

	@Override
	public Collection<ISequences<T>> getBestSequences() {
		return Collections.singletonList(bestSequences);
	}

	public IThresholder getThresholder() {
		return thresholder;
	}

	public void setThresholder(final IThresholder thresholder) {
		this.thresholder = thresholder;
	}

	/**
	 * Returns the best fitness so far
	 * 
	 * @return
	 */
	public long getBestFitness() {
		return bestFitness;
	}

	/**
	 * Returns the current {@link ISequences}
	 * 
	 * @return
	 */
	public ISequences<T> getCurrentSequences() {
		return currentSequences;
	}

	/**
	 * Returns the fitness of the current {@link ISequences}
	 * 
	 * @return
	 */
	public long getCurrentFitness() {
		return currentFitness;
	}
}
