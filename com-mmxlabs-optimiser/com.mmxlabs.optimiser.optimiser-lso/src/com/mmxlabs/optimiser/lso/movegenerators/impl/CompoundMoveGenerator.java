/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * Move generator which delegates to other move generators
 * 
 * @author hinton
 * 
 */
public class CompoundMoveGenerator implements IMoveGenerator {
	private final List<Double> weights = new LinkedList<Double>();
	private final List<IMoveGenerator> generators = new LinkedList<IMoveGenerator>();
	private double totalWeight = 0;
	private ISequences sequences;
	private Random random;

	public void addGenerator(final IMoveGenerator generator, final double weight) {
		weights.add(weight);
		generators.add(generator);
		totalWeight += weight;
	}

	@Override
	public IMove generateMove() {
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
	@NonNull
	public ISequences getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(@NonNull final ISequences sequences) {
		for (final IMoveGenerator generator : generators) {
			generator.setSequences(sequences);
		}
		this.sequences = sequences;
	}

	public void setRandom(@NonNull final Random random) {
		this.random = random;
	}
}
