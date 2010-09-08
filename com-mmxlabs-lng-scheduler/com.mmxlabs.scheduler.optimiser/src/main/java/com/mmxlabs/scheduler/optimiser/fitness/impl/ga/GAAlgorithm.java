package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Random;
import java.util.TreeSet;

public final class GAAlgorithm<T> {

	private final IIndividualEvaluator<T> individualEvaluator;

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
	private final Individual[] population;

	/**
	 * Subset of population which are good and to be retained. There are
	 * {@link #N} items.
	 */
	private final Individual[] good;

	/**
	 * Subset of population which are bad and are to be replaced with new
	 * members. There are {@link #population#length} - {@link #N} items.
	 */

	private final Individual[] bad;

	/**
	 * The best individual found so far
	 */
	private Individual bestIndividual;

	/**
	 * The fitness of the {@link #bestIndividual}.
	 */
	private long bestFitness;

	/**
	 * Number of bytes in each individual.
	 */
	private final int numBytes;

	public GAAlgorithm(final Random random,
			final IIndividualEvaluator<T> individualEvaluator,
			final float mutateThreshold, final int numElements, final int topN,
			final int numBytes) {
		this.random = random;
		this.individualEvaluator = individualEvaluator;
		this.mutateThreshold = mutateThreshold;
		this.N = topN;
		this.population = new Individual[numElements];
		this.numBytes = numBytes;
		good = new Individual[N];
		bad = new Individual[population.length - N];
	}

	public void init() {
		// Create the inital population
		initPopulation();

		// Reset best state to unknown
		bestFitness = Long.MAX_VALUE;
		bestIndividual = null;

		// Calculate initial population fitness - update best state and populate
		// good/bad arrays
		evaluate(N, population, good, bad);
	}

	public final void initPopulation() {
		// Round to nearest byte
		// Randomly init the population
		for (int i = 0; i < population.length; ++i) {
			final byte[] bytes = new byte[numBytes];
			random.nextBytes(bytes);
			population[i] = new Individual(bytes);
		}
	}

	public final void step() {

		// Update population
		crossover(good, bad);
		mutate(bad);

		// Evaluate new population - update best state and repopulate good/bad
		// arrays
		evaluate(N, population, good, bad);
	}

	/**
	 * Applt mutate operator to all individuals in the array
	 * 
	 * @param mutatable
	 */
	public final void mutate(final Individual[] mutatable) {
		for (final Individual individual : mutatable) {
			op_mutate(individual, mutateThreshold);
		}
	}

	/**
	 * Apply the crossover op taking random individual from the good array and
	 * replacing all the element in the bad array.
	 * 
	 * @param good
	 * @param bad
	 */
	public final void crossover(final Individual[] good, final Individual[] bad) {

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

	/**
	 * Class to combined an {@link Individual} with it's fitness for use in a
	 * {@link TreeSet} ordered by fitness.
	 * 
	 */
	static final class Tuple implements Comparable<Tuple> {
		public final Individual i;
		public final int idx;
		public final long f;

		public Tuple(final Individual i, final int idx, final long f) {
			this.i = i;
			this.idx = idx;
			this.f = f;
		}

		@Override
		public final int compareTo(final Tuple o) {
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

	public void evaluate(final int N, final Individual[] population,
			final Individual[] good, final Individual[] bad) {

		int badIdx = 0;

		// TODO: We could record this between iterations, and only evaluate the
		// bad population?
		final TreeSet<Tuple> topN = new TreeSet<Tuple>();

		long worstFitness = Long.MAX_VALUE;

		for (int idx = 0; idx < population.length; ++idx) {
			final Individual individual = population[idx];
			final long f = individualEvaluator.evaluate(individual);

			// Fill up the TreeSet until we have N entries..
			if (topN.size() < N) {
				topN.add(new Tuple(individual, idx, f));
				worstFitness = topN.first().f;

				// .. then process the rest populating the bad array
			} else if (f < worstFitness) {
				// remove lowest entry..
				Tuple lowest = topN.pollFirst();

				// .. and add it to the bad array as it is no longer a top N
				// entry
				bad[badIdx++] = lowest.i;

				// Add new entry into set
				topN.add(new Tuple(individual, idx, f));

				// Record new worst fitness
				worstFitness = topN.first().f;
			} else {
				// If event worse
				bad[badIdx++] = individual;
			}
		}

		// Update the good array with
		int goodIdx = 0;
		for (final Tuple t : topN) {
			good[goodIdx++] = t.i;
		}

		// Record best entry;
		final Tuple t = topN.last();
		if (t.f < bestFitness) {
			bestIndividual = t.i;
			bestFitness = t.f;
		}
	}

	public float getMutateThreshold() {
		return mutateThreshold;
	}

	public Random getRandom() {
		return random;
	}

	public IIndividualEvaluator<T> getIndividualEvaluator() {
		return individualEvaluator;
	}

	public int getN() {
		return N;
	}

	public Individual[] getPopulation() {
		return population;
	}

	public Individual getBestIndividual() {
		return bestIndividual;
	}

	public long getBestFitness() {
		return bestFitness;
	}

	/**
	 * GA mutate operator. For each byte in the sequence, replace it with a
	 * random new byte with a probability given by {@link #mutateThreshold}
	 * 
	 * @param individual
	 * @param threshold
	 */
	public final void op_mutate(final Individual individual,
			final float threshold) {
		final byte[] storage = new byte[1];
		final byte[] bytes = individual.bytes;
		for (int i = 0; i < bytes.length; ++i) {
			if (random.nextFloat() < threshold) {
				random.nextBytes(storage);
				bytes[i] = storage[0];
			}
		}
	}

	/**
	 * GA crossover operator creating two new individuals. Crossover is applied
	 * at the byte boundary.
	 * 
	 * @param individual1
	 * @param individual2
	 * @param newIndividual1
	 * @param newIndividual2
	 */
	public final void op_crossover(final Individual individual1,
			final Individual individual2, final Individual newIndividual1,
			final Individual newIndividual2) {

		final byte[] bytes1 = individual1.bytes;
		final byte[] bytes2 = individual2.bytes;

		final byte[] newBytes1 = newIndividual1.bytes;
		final byte[] newBytes2 = newIndividual2.bytes;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert newIndividual1.length == newIndividual2.length;
		// assert bytes1.length == bytes2.length;
		// assert newBytes1.length == newBytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numBytes = bytes1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = random.nextInt(numBytes);

		// Copy first part
		for (int i = 0; i < cutoff; ++i) {
			newBytes1[i] = bytes1[i];
			newBytes2[i] = bytes2[i];
		}

		// Copy second part, swapping source
		for (int i = cutoff; i < numBytes; ++i) {
			newBytes1[i] = bytes2[i];
			newBytes2[i] = bytes1[i];
		}
	}

	/**
	 * GA crossover operator creating a single new individual. Crossover is
	 * applied at the byte boundary.
	 * 
	 * @param individual1
	 * @param individual2
	 * @param newIndividual1
	 */
	public final void op_crossover(final Individual individual1,
			final Individual individual2, final Individual newIndividual1) {

		final byte[] bytes1 = individual1.bytes;
		final byte[] bytes2 = individual2.bytes;

		final byte[] newBytes = newIndividual1.bytes;

		// Check data matches
		// assert individual1.length == individual2.length;
		// assert bytes1.length == bytes2.length;
		// assert bytes1.length == newBytes1.length;
		// assert individual1.length == newIndividual1.length;

		final int numBytes = bytes1.length;

		// Pick a break point for crossover at byte level (rather than bit
		// level)
		// This could be made better by performing some bit mask ops to
		// crossover at the bit level,
		// at the cost of marginally increased computation
		final int cutoff = random.nextInt(numBytes);

		// Randomly pick which way round the crossover happens
		if (random.nextBoolean()) {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newBytes[i] = bytes1[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numBytes; ++i) {
				newBytes[i] = bytes2[i];
			}
		} else {
			// Copy first part
			for (int i = 0; i < cutoff; ++i) {
				newBytes[i] = bytes2[i];
			}

			// Copy second part, swapping source
			for (int i = cutoff; i < numBytes; ++i) {
				newBytes[i] = bytes1[i];
			}
		}
	}

	public final int getNumBytes() {
		return numBytes;
	}

}
