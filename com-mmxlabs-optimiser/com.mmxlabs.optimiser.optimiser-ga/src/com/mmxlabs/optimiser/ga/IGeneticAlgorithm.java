/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga;

import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IGeneticAlgorithm<I extends Individual<I>> {

	void init();

	void initPopulation();

	/**
	 * Advance the optimisation. First, generate a new population using mutation and crossover to evolve the individuals. Then evaluate the new population.
	 */
	void step();

	/**
	 * Apply mutate operator to all individuals in the array
	 * 
	 * @param mutatable
	 */
	void mutate(final I @NonNull [] mutatable);

	/**
	 * Apply the crossover op taking random individual from the good array and replacing all the element in the bad array.
	 * 
	 * @param good
	 * @param bad
	 */
	void crossover(final I @NonNull [] good, final I @NonNull [] bad);

	/**
	 * Evaluate the population, storing the top N values in the good array and the remaining into the bad array.
	 * 
	 * @param N
	 *            Number of "good" individuals
	 * @param population
	 *            Complete population of individuals
	 * @param good
	 *            Array of N good individuals
	 * @param bad
	 *            Array of population - N individuals
	 */
	void evaluate(final int N, final @NonNull I @NonNull [] population, final @NonNull I @NonNull [] good, final @NonNull I @NonNull [] bad);

	/**
	 * Returns the change of a mutation occurring
	 * 
	 * @return
	 */
	float getMutateThreshold();

	/**
	 * Returns the random number generator used by the algorithm
	 * 
	 * @return
	 */
	Random getRandom();

	/**
	 * Returns the {@link IIndividualFactory} used to generate new {@link Individual}s.
	 * 
	 * @return
	 */
	IIndividualFactory<I> getIndividualFactory();

	/**
	 * Returns the {@link IIndividualEvaluator} used to evaluate the fitness of {@link Individual}s
	 * 
	 * @return
	 */
	IIndividualEvaluator<I> getIndividualEvaluator();

	/**
	 * Return the number of top individuals
	 * 
	 * @return
	 */
	int getN();

	/**
	 * Returns the current population
	 * 
	 * @return
	 */
	I[] getPopulation();

	/**
	 * Returns the current best individual
	 * 
	 * @return
	 */
	@Nullable I getBestIndividual();

	/**
	 * Returns the current best fitness
	 * 
	 * @return
	 */
	long getBestFitness();

	/**
	 * GA mutate operator. For each byte in the sequence, replace it with a random new byte with a probability given by {@link #mutateThreshold}
	 * 
	 * @param individual
	 * @param threshold
	 */
	void op_mutate(final I individual, final float threshold);

	/**
	 * GA crossover operator creating two new individuals. Crossover is applied at the byte boundary.
	 * 
	 * @param individual1
	 * @param individual2
	 * @param newIndividual1
	 * @param newIndividual2
	 */
	void op_crossover(final I individual1, final I individual2, final I newIndividual1, final I newIndividual2);

	/**
	 * GA crossover operator creating a single new individual. Crossover is applied at the byte boundary.
	 * 
	 * @param individual1
	 * @param individual2
	 * @param newIndividual1
	 */
	void op_crossover(final I individual1, final I individual2, final I newIndividual1);

}