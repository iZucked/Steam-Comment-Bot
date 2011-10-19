/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * Move generator which delegates to other move generators
 * 
 * @author hinton
 * 
 */
public class CompoundMoveGenerator<T> implements IMoveGenerator<T> {
	private final List<Double> weights = new LinkedList<Double>();
	private final List<IMoveGenerator<T>> generators = new LinkedList<IMoveGenerator<T>>();
	private double totalWeight = 0;
	private ISequences<T> sequences;
	private Random random;

	public void addGenerator(final IMoveGenerator<T> generator, final double weight) {
		weights.add(weight);
		generators.add(generator);
		totalWeight += weight;
	}

	@Override
	public IMove<T> generateMove() {
		double d = random.nextDouble() * totalWeight;
		for (int i = 0; i < weights.size(); i++) {
			final double w = weights.get(i);
			if (d <= w) {
				return generators.get(i).generateMove();
			}
			d -= w;
		}
		return null;
	}

	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences<T> sequences) {
		for (final IMoveGenerator<T> generator : generators) {
			generator.setSequences(sequences);
		}
		this.sequences = sequences;
	}

	public void setRandom(final Random random) {
		this.random = random;
	}
}
