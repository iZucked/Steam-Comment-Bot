package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Map;

import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;

/**
 * {@link IFitnessCombiner} which calculates the weighted linear combination of
 * raw fitness values to calculate the overall fitness value.
 * 
 * @author Simon Goodall
 * 
 */
public final class LinearFitnessCombiner implements IFitnessCombiner {

	private Map<String, Double> fitnessComponentWeights = null;

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

	public <T> long calculateFitness(
			final Collection<IFitnessComponent<T>> fitnessComponents) {
		// Sum up total fitness, combining raw values with weights
		long totalFitness = 0;
		for (final IFitnessComponent<T> component : fitnessComponents) {
			final long rawFitness = component.getFitness();
			final double weight = fitnessComponentWeights.get(component
					.getName());
			final long fitness = Math.round(weight * (double) rawFitness);
			totalFitness += fitness;
		}
		return totalFitness;
	}

}
