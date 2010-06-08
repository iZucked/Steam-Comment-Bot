package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.impl.Sequences;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * A {@link IFitnessEvaluator} implementation to apply simulated annealing to
 * the optimisation process and use a linear combination of weights to
 * {@link IFitnessComponent} values. This implementation only tracks a single
 * best state.
 * 
 * TODO: Split class up into difference parts, the SA rule, combiner etc...
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

	private double stepSize = Double.MAX_VALUE;

	private double temperature = Double.MAX_VALUE;

	private Map<String, Double> fitnessComponentWeights = null;

	private ISequences<T> bestSequences = null;

	private double bestFitness = Double.MAX_VALUE;

	private ISequences<T> currentSequences = null;

	private double currentFitness = Double.MAX_VALUE;

	private double currentThreshold = Double.MAX_VALUE;

	private int numberOfIterations = Integer.MAX_VALUE;

	private int iteration;

	@Override
	public boolean checkSequences(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		final double totalFitness = evaluteSequences(sequences,
				affectedResources);

		// Calculate fitness delta
		final double delta = totalFitness - currentFitness;

		final boolean accept;

		// If fitness change is within the threshold, then accept the change
		if (delta < currentThreshold) {

			// Store current fitness and sequences
			currentFitness = totalFitness;
			currentSequences = new Sequences<T>(sequences);

			// If this is the best state seen so far, then record it.
			if (currentFitness < bestFitness) {
				// Store this as the new best
				bestFitness = currentFitness;
				bestSequences = new Sequences<T>(sequences);
			}

			fitnessHelper.acceptFromComponents(fitnessComponents, sequences,
					affectedResources);

			accept = true;
		} else {

			accept = false;
		}

		step();

		return accept;
	}

	/**
	 * Method used to perform the evaluation of {@link ISequences}.
	 * 
	 * @param sequences
	 * @param affectedResources
	 * @return
	 */
	private double evaluteSequences(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Evaluates the current sequences
		if (affectedResources == null) {
			fitnessHelper.evaluateSequencesFromComponents(sequences,
					fitnessComponents);
		} else {
			fitnessHelper.evaluateSequencesFromComponents(sequences,
					fitnessComponents, affectedResources);
		}

		// Sum up total fitness, combining raw values with weights
		double totalFitness = 0.0;
		for (final IFitnessComponent<T> component : fitnessComponents) {
			final long rawFitness = component.getFitness();
			final double weight = fitnessComponentWeights.get(component
					.getName());
			final double fitness = weight * (double) rawFitness;
			totalFitness += fitness;
		}
		return totalFitness;
	}

	@Override
	public void init() {
		if (fitnessComponents == null) {
			throw new IllegalStateException("No fitness components set");
		}

		if (fitnessHelper == null) {
			throw new IllegalStateException("No fitness helper set");
		}

		if (fitnessComponentWeights == null) {
			throw new IllegalStateException("No fitness component weights set");
		}

		if (numberOfIterations == Integer.MAX_VALUE) {
			throw new IllegalStateException("Number of iterations not set");
		}

		if (temperature == Double.MAX_VALUE) {
			throw new IllegalStateException("Initial temperature not set");
		}
	}

	@Override
	public void setOptimisationData(IOptimisationData<T> data) {

		// Initialise the fitness functions
		fitnessHelper.initFitnessComponents(getFitnessComponents(), data);
	}

	@Override
	public void setInitialSequences(ISequences<T> initialSequences) {

		if (initialSequences == null) {
			throw new IllegalArgumentException(
					"Initial sequences cannot be null");
		}

		final double totalFitness = evaluteSequences(initialSequences, null);
		bestFitness = totalFitness;
		currentFitness = totalFitness;
		bestSequences = new Sequences<T>(initialSequences);
		currentSequences = new Sequences<T>(initialSequences);

		// Setup initial conditions
		iteration = 0;
		stepSize = temperature / (double) numberOfIterations;
		currentThreshold = temperature;
	}

	/**
	 * Step the evaluator to the next threshold value.
	 */
	private void step() {

		++iteration;

		currentThreshold -= stepSize;
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

	/**
	 * Set the initial temperature value
	 * 
	 * @param temperature
	 */
	public void setTemperature(final double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Returns the initial temperature value
	 * 
	 * @return
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Set a map of {@link IFitnessComponent} name to weight - used to calculate
	 * the full fitness value.
	 * 
	 * @return
	 */
	public void setFitnessComponentWeights(
			final Map<String, Double> fitnessComponentWeights) {
		this.fitnessComponentWeights = fitnessComponentWeights;
	}

	/**
	 * Returns a map of {@link IFitnessComponent} name to weight.
	 * 
	 * @return
	 */
	public Map<String, Double> getFitnessComponentWeights() {
		return fitnessComponentWeights;
	}

	/**
	 * Returns the best fitness so far
	 * 
	 * @return
	 */
	public double getBestFitness() {
		return bestFitness;
	}

	/**
	 * Set the number of iterations for this optimisation
	 * 
	 * @param numberOfIterations
	 */
	public void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	/**
	 * Returns the number of iterations for this optimisation
	 * 
	 * @return
	 */
	public int getNumberOfIterations() {
		return numberOfIterations;
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
	public double getCurrentFitness() {
		return currentFitness;
	}
}
