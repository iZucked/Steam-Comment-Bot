/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * A {@link IFitnessEvaluator} implementation to apply simulated annealing to the optimisation process and use a linear combination of weights to {@link IFitnessComponent} values. This implementation
 * only tracks a single best state.
 * 
 * TODO: RENAME Class now it has been refactored
 * 
 * @author Simon Goodall
 * 
 */
public final class LinearSimulatedAnnealingFitnessEvaluator implements IFitnessEvaluator {

	private static final Logger log = LoggerFactory.getLogger(LinearSimulatedAnnealingFitnessEvaluator.class);

	private IFitnessHelper fitnessHelper = null;

	private List<IFitnessComponent> fitnessComponents = null;

	private IThresholder thresholder;

	private IFitnessCombiner fitnessCombiner;

	private ISequences bestSequences = null;

	private long bestFitness = Long.MAX_VALUE;

	private final Map<String, Long> bestFitnesses = new HashMap<String, Long>();
	private final Map<String, Long> currentFitnesses = new HashMap<String, Long>();

	private ISequences currentSequences = null;

	private long currentFitness = Long.MAX_VALUE;

	@Override
	public boolean evaluateSequences(final ISequences sequences, final Collection<IResource> affectedResources) {

		final long totalFitness = evaluateSequencesIntern(sequences, affectedResources);
		boolean accept = false;
		if (totalFitness != Long.MAX_VALUE) {
			// Calculate fitness delta
			final long delta = totalFitness - currentFitness;

			// If fitness change is within the threshold, then accept the change
			accept = thresholder.accept(delta);

			if (accept) {
				// Update internal state
				updateBest(sequences, totalFitness);

				// Update fitness functions state
				fitnessHelper.acceptFromComponents(fitnessComponents, sequences, affectedResources);

				for (final IFitnessComponent component : fitnessComponents) {
					currentFitnesses.put(component.getName(), component.getFitness());
				}
			}
		}
		// Step to the next threshold levels
		thresholder.step();

		return accept;
	}

	/**
	 * Update internal state, storing new fitness as new current and updating best fitness if required.
	 * 
	 * @param sequences
	 * @param totalFitness
	 */
	private void updateBest(final ISequences sequences, final long totalFitness) {

		// Store current fitness and sequences
		currentFitness = totalFitness;
		currentSequences = new Sequences(sequences);

		// If this is the best state seen so far, then record it.
		if (currentFitness < bestFitness) {
			// Store this as the new best
			// Do we need to copy here too?
			bestSequences = currentSequences;
			bestFitness = currentFitness;

			for (final IFitnessComponent component : fitnessComponents) {
				bestFitnesses.put(component.getName(), component.getFitness());
			}
		}
	}

	/**
	 * Method used to perform the evaluation of {@link ISequences}.
	 * 
	 * @param sequences
	 * @param affectedResources
	 * @return
	 */
	private long evaluateSequencesIntern(final ISequences sequences, final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, fitnessComponents)) {
				return Long.MAX_VALUE;
			}
		} else {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, fitnessComponents, affectedResources)) {
				return Long.MAX_VALUE;
			}
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
	public void setOptimisationData(final IOptimisationData data) {

		// Initialise the fitness functions
		fitnessHelper.initFitnessComponents(getFitnessComponents(), data);
	}

	@Override
	public void setInitialSequences(final ISequences initialSequences) {

		if (initialSequences == null) {
			throw new IllegalArgumentException("Initial sequences cannot be null");
		}

		// TODO check for MAX_VALUE here and throw some kind of death condition.
		final long totalFitness = evaluateSequencesIntern(initialSequences, null);

		if (totalFitness == Long.MAX_VALUE) {
			log.error("Initial sequences have Long.MAX_VALUE fitness, which is pretty bad.");
		}

		bestFitness = totalFitness;
		currentFitness = totalFitness;
		bestSequences = new Sequences(initialSequences);
		currentSequences = new Sequences(initialSequences);

		for (final IFitnessComponent component : fitnessComponents) {
			bestFitnesses.put(component.getName(), component.getFitness());
		}

		// Setup initial conditions
		thresholder.init();
	}

	@Override
	public void setFitnessComponents(final List<IFitnessComponent> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	@Override
	public List<IFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}

	@Override
	public IFitnessHelper getFitnessHelper() {
		return fitnessHelper;
	}

	@Override
	public void setFitnessHelper(final IFitnessHelper fitnessHelper) {
		this.fitnessHelper = fitnessHelper;
	}

	@Override
	public ISequences getBestSequences() {
		return bestSequences;
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
	@Override
	public long getBestFitness() {
		return bestFitness;
	}

	/**
	 * Returns the current {@link ISequences}
	 * 
	 * @return
	 */
	@Override
	public ISequences getCurrentSequences() {
		return currentSequences;
	}

	/**
	 * Returns the fitness of the current {@link ISequences}
	 * 
	 * @return
	 */
	@Override
	public long getCurrentFitness() {
		return currentFitness;
	}

	public Map<String, Long> getBestFitnesses() {
		return bestFitnesses;
	}

	public IFitnessCombiner getFitnessCombiner() {
		return fitnessCombiner;
	}

	public void setFitnessCombiner(final IFitnessCombiner fitnessCombiner) {
		this.fitnessCombiner = fitnessCombiner;
	}

	@Override
	public void dispose() {
		this.bestFitnesses.clear();
		this.currentFitnesses.clear();
		this.bestSequences = null;
		this.currentSequences = null;
		this.fitnessCombiner = null;
		this.fitnessComponents = null;
		this.fitnessHelper = null;
		this.thresholder = null;
	}

	@Override
	public IAnnotatedSolution getBestAnnotatedSolution(final IOptimisationContext context, final boolean forExport) {
		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(context, getBestSequences(), getFitnessComponents(), forExport);

		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, new HashMap<String, Long>(bestFitnesses));

		return result;
	}

	@Override
	public IAnnotatedSolution getCurrentAnnotatedSolution(final IOptimisationContext context, final boolean forExport) {
		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(context, getCurrentSequences(), getFitnessComponents(), forExport);

		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, new HashMap<String, Long>(currentFitnesses));

		return result;
	}

}
