/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
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

	private static final Logger LOG = LoggerFactory.getLogger(LinearSimulatedAnnealingFitnessEvaluator.class);

	@Inject
	private IFitnessHelper fitnessHelper;

	private List<IFitnessComponent> fitnessComponents = Collections.emptyList();
	private List<IEvaluationProcess> evaluationProcesses = Collections.emptyList();

	private IThresholder thresholder;

	@Inject
	private IFitnessCombiner fitnessCombiner;

	private final Map<String, Long> initialFitnesses = new HashMap<String, Long>();
	private final Map<String, Long> currentFitnesses = new HashMap<String, Long>();
	private final Map<String, Long> bestFitnesses = new HashMap<String, Long>();

	private Triple<ISequences, ISequences, IEvaluationState> initialSequences = null;
	private Triple<ISequences, ISequences, IEvaluationState> currentSequences = null;
	private Triple<ISequences, ISequences, IEvaluationState> bestSequences = null;

	private long initialFitness = Long.MAX_VALUE;
	private long currentFitness = Long.MAX_VALUE;
	private long lastFitness = Long.MAX_VALUE;
	private long bestFitness = Long.MAX_VALUE;

	@Override
	public boolean evaluateSequences(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState,
			@NonNull final Collection<IResource> affectedResources) {

		final long totalFitness = evaluateSequencesIntern(fullSequences, evaluationState, affectedResources);
		boolean accept = false;
		if (totalFitness != Long.MAX_VALUE) {
			// Calculate fitness delta
			final long delta = totalFitness - currentFitness;

			// If fitness change is within the threshold, then accept the change
			accept = thresholder.accept(delta);

			if (accept) {

				// Update fitness functions state
				fitnessHelper.acceptFromComponents(getFitnessComponents(), fullSequences, affectedResources);

				// Update internal state
				updateBest(rawSequences, fullSequences, evaluationState, totalFitness);

			}
		}
		lastFitness = totalFitness;
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
	private void updateBest(@NonNull final ISequences rawSequences, final ISequences fullSequences, @NonNull final IEvaluationState evaluationState, final long totalFitness) {

		// Store current fitness and sequences
		currentFitness = totalFitness;
		currentSequences = new Triple<ISequences, ISequences, IEvaluationState>(new Sequences(rawSequences), new Sequences(fullSequences), evaluationState);

		for (final IFitnessComponent component : getFitnessComponents()) {
			currentFitnesses.put(component.getName(), component.getFitness());
		}

		// If this is the best state seen so far (this restart), then record it.
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
	private long evaluateSequencesIntern(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, getFitnessComponents())) {
				return Long.MAX_VALUE;
			}
		} else {
			if (!fitnessHelper.evaluateSequencesFromComponents(sequences, evaluationState, getFitnessComponents(), affectedResources)) {
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
		if (thresholder == null) {
			throw new IllegalStateException("No thresholder set");
		}
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData data) {

		// Initialise the fitness functions
		fitnessHelper.initFitnessComponents(getFitnessComponents(), data);
	}

	@Override
	public void setInitialSequences(@NonNull final ISequences initialRawSequences, @NonNull final ISequences initialFullSequences, @NonNull final IEvaluationState evaluationState) {

		// TODO check for MAX_VALUE here and throw some kind of death condition.
		final long totalFitness = evaluateSequencesIntern(initialFullSequences, evaluationState, null);

		if (totalFitness == Long.MAX_VALUE) {
			LOG.error("Initial sequences have Long.MAX_VALUE fitness, which is pretty bad.");
		}

		bestFitness = totalFitness;
		initialFitness = totalFitness;
		currentFitness = totalFitness;

		this.initialSequences = new Triple<ISequences, ISequences, IEvaluationState>(new Sequences(initialRawSequences), new Sequences(initialFullSequences), evaluationState);
		bestSequences = new Triple<ISequences, ISequences, IEvaluationState>(new Sequences(initialRawSequences), new Sequences(initialFullSequences), evaluationState);
		currentSequences = new Triple<ISequences, ISequences, IEvaluationState>(new Sequences(initialRawSequences), new Sequences(initialFullSequences), evaluationState);

		for (final IFitnessComponent component : fitnessComponents) {
			bestFitnesses.put(component.getName(), component.getFitness());
		}

		// Setup initial conditions
		thresholder.init();
	}

	@Override
	public void setFitnessComponents(@NonNull final List<IFitnessComponent> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	@Override
	@NonNull
	public final List<IFitnessComponent> getFitnessComponents() {
		assert fitnessComponents != null;
		return fitnessComponents;
	}

	@Override
	@NonNull
	public final List<IEvaluationProcess> getEvaluationProcesses() {
		assert evaluationProcesses != null;
		return evaluationProcesses;
	}

	@Override
	@Nullable
	public Triple<ISequences, ISequences, IEvaluationState> getBestSequences() {
		return bestSequences;
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
	public Triple<ISequences, ISequences, IEvaluationState> getCurrentSequences() {
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

	@Override
	public IAnnotatedSolution getBestAnnotatedSolution(@NonNull final IOptimisationContext context) {
		final Triple<ISequences, ISequences, IEvaluationState> p = getBestSequences();
		if (p == null) {
			return null;
		}
		final ISequences fullSequences = p.getSecond();
		final IEvaluationState evaluationState = p.getThird();
		assert fullSequences != null;
		assert evaluationState != null;
		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(context, fullSequences, evaluationState, getFitnessComponents(), getEvaluationProcesses());

		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, new HashMap<String, Long>(bestFitnesses));

		return result;
	}

	@Override
	public IAnnotatedSolution getCurrentAnnotatedSolution(@NonNull final IOptimisationContext context) {
		final Triple<ISequences, ISequences, IEvaluationState> p = getCurrentSequences();
		if (p == null) {
			return null;
		}
		final ISequences fullSequences = p.getSecond();
		final IEvaluationState evaluationState = p.getThird();
		assert fullSequences != null;
		assert evaluationState != null;

		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(context, fullSequences, evaluationState, getFitnessComponents(), getEvaluationProcesses());

		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, new HashMap<String, Long>(currentFitnesses));

		return result;
	}

	public void setEvaluationProcesses(@NonNull final List<IEvaluationProcess> evaluationProcesses) {
		this.evaluationProcesses = evaluationProcesses;

	}

	@Override
	public void step() {
		thresholder.step();
	}

	@Override
	public IAnnotatedSolution createAnnotatedSolution(final IOptimisationContext context, final ISequences fullSequences, final IEvaluationState evaluationState) {
		assert fullSequences != null;
		assert evaluationState != null;

		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(context, fullSequences, evaluationState, getFitnessComponents(), getEvaluationProcesses());

		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, new HashMap<String, Long>(currentFitnesses));

		return result;
	}

	public void setThresholder(final IThresholder thresholder) {
		this.thresholder = thresholder;
	}

	@Override
	public void restart() {
		currentFitness = initialFitness;
		currentSequences = initialSequences;
		thresholder.reset();
	}

	@Override
	public long getLastFitness() {
		return lastFitness;
	}
}
