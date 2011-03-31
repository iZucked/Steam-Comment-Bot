/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.impl;

import java.util.Random;
import java.util.TreeSet;

import com.mmxlabs.optimiser.ga.IGeneticAlgorithm;
import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.Individual;

public abstract class AbstractGAAlgorithm<I extends Individual<I>> implements
		IGeneticAlgorithm<I> {

	private final IIndividualEvaluator<I> individualEvaluator;

	/**
	 * Top n individuals to keep between evolutions of the population.
	 */
	private final int N;

	/**
	 * Threshold (between 0.0 and 1.0) of change to mutate a single byte in new
	 * individuals
	 */
	private final float mutateThreshold;

	/**
	 * Random number generator to make decisions on.
	 */
	private final Random random;

	/**
	 * Complete population
	 */
	private final I[] population;

	/**
	 * Subset of population which are good and to be retained. There are
	 * {@link #N} items.
	 */
	private final I[] good;

	/**
	 * Subset of population which are bad and are to be replaced with new
	 * members. There are {@link #population#length} - {@link #N} items.
	 */

	private final I[] bad;

	/**
	 * The best individual found so far
	 */
	private I bestIndividual;

	/**
	 * The fitness of the {@link #bestIndividual}.
	 */
	private long bestFitness;

	@SuppressWarnings("unchecked")
	public AbstractGAAlgorithm(final Random random,
			final IIndividualEvaluator<I> individualEvaluator,
			final float mutateThreshold, final int numElements, final int topN) {
		this.random = random;
		this.individualEvaluator = individualEvaluator;
		this.mutateThreshold = mutateThreshold;
		this.N = topN;
		this.population = (I[]) new Individual[numElements];
		good = (I[]) new Individual[N];
		bad = (I[]) new Individual[population.length - N];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#init()
	 */
	@Override
	public void init() {
		// Create the initial population
		initPopulation();

		// Reset best state to unknown
		bestFitness = Long.MAX_VALUE;
		bestIndividual = null;

		// Calculate initial population fitness - update best state and populate
		// good/bad arrays
		evaluate(N, population, good, bad);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#initPopulation()
	 */
	@Override
	public final void initPopulation() {
		// Randomly init the population
		for (int i = 0; i < population.length; ++i) {
			population[i] = createNewIndividual();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#step()
	 */
	@Override
	public final void step() {

		// Update population
		crossover(good, bad);
		mutate(bad);

		// Evaluate new population - update best state and repopulate good/bad
		// arrays
		evaluate(N, population, good, bad);
	}

	/**
	 * Class to combined an {@link Individual} with it's fitness for use in a
	 * {@link TreeSet} ordered by fitness.
	 * 
	 */
	private static final class Tuple<I> implements Comparable<Tuple<I>> {
		public final I i;
		public final int idx;
		public final long f;

		public Tuple(final I i, final int idx, final long f) {
			this.i = i;
			this.idx = idx;
			this.f = f;
		}

		@Override
		public final int compareTo(final Tuple<I> o) {
			final long c = f - o.f;
			// Sort on fitness
			if (c < 0) {
				return -1;
			} else if (c > 0) {
				return -1;
			} else {
				// Then sort of original position. As this will be used in a
				// TreeSet we never want to return 0 as this will overwrite the
				// entry instead.
				assert idx != o.idx;
				return idx - o.idx;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#evaluate(int,
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual[],
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual[],
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual[])
	 */
	@Override
	public final void evaluate(final int N, final I[] population,
			final I[] good, final I[] bad) {

		int badIdx = 0;

		// TODO: We could record this between iterations, and only evaluate the
		// bad population?
		final TreeSet<Tuple<I>> topN = new TreeSet<Tuple<I>>();

		long worstFitness = Long.MAX_VALUE;

		for (int idx = 0; idx < population.length; ++idx) {
			final I individual = population[idx];
			final long f = individualEvaluator.evaluate(individual);

			// Fill up the TreeSet until we have N entries..
			if (topN.size() < N) {
				topN.add(new Tuple<I>(individual, idx, f));
				worstFitness = topN.first().f;

				// .. then process the rest populating the bad array
			} else if (f < worstFitness) {
				// remove lowest entry..
				final Tuple<I> lowest = topN.pollFirst();

				// .. and add it to the bad array as it is no longer a top N
				// entry
				bad[badIdx++] = lowest.i;

				// Add new entry into set
				topN.add(new Tuple<I>(individual, idx, f));

				// Record new worst fitness
				worstFitness = topN.first().f;
			} else {
				// If event worse
				bad[badIdx++] = individual;
			}
		}

		// Update the good array with
		int goodIdx = 0;
		for (final Tuple<I> t : topN) {
			good[goodIdx++] = t.i;
		}

		// Record best entry;
		final Tuple<I> t = topN.last();
		if (t.f < bestFitness) {
			bestIndividual = t.i;
			bestFitness = t.f;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getMutateThreshold()
	 */
	@Override
	public final float getMutateThreshold() {
		return mutateThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getRandom()
	 */
	@Override
	public final Random getRandom() {
		return random;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getIndividualEvaluator
	 * ()
	 */
	@Override
	public final IIndividualEvaluator<I> getIndividualEvaluator() {
		return individualEvaluator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getN()
	 */
	@Override
	public final int getN() {
		return N;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getPopulation()
	 */
	@Override
	public final I[] getPopulation() {
		return population;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getBestIndividual()
	 */
	@Override
	public final I getBestIndividual() {
		return bestIndividual;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#getBestFitness()
	 */
	@Override
	public final long getBestFitness() {
		return bestFitness;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#mutate(com.mmxlabs
	 * .optimiser.ga.bytearray.ByteArrayIndividual[])
	 */
	@Override
	public final void mutate(final I[] mutatable) {
		for (final I individual : mutatable) {
			op_mutate(individual, mutateThreshold);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.ga.bytearray.IGeneticAlgorithm#crossover(com.mmxlabs
	 * .optimiser.ga.bytearray.ByteArrayIndividual[],
	 * com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual[])
	 */
	@Override
	public final void crossover(final I[] good, final I[] bad) {

		int badIdx = 0;
		while (badIdx + 1 < bad.length) {
			final int idx1 = random.nextInt(good.length);
			final int idx2 = random.nextInt(good.length);
			op_crossover(good[idx1], good[idx2], bad[badIdx++], bad[badIdx++]);
		}
		if (badIdx < bad.length) {
			final int idx1 = random.nextInt(good.length);
			final int idx2 = random.nextInt(good.length);
			op_crossover(good[idx1], good[idx2], bad[badIdx++]);
		}
	}

}
