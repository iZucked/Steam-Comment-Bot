/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * Move generator to randomly generate moves with an equal probability distribution. This class uses registered {@link IRandomMoveGeneratorUnit} implementations to determine how each type of move is
 * created. This separates the {@link RandomMoveGenerator} from any move implementations.
 * 
 * @author Simon Goodall
 * 
 */

public final class RandomMoveGenerator implements IMoveGenerator {

	private final List<IRandomMoveGeneratorUnit> units = new ArrayList<IRandomMoveGeneratorUnit>();

	private final List<Double> weights = new ArrayList<Double>();

	private double totalWeight = 0;

	public RandomMoveGenerator() {

	}

	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {
		// Pin for null analysis
		final ISequences pSequences = rawSequences;
		assert pSequences != null;
		double newMove = random.nextDouble() * totalWeight;
		for (int i = 0; i < units.size(); i++) {
			final double weight = weights.get(i);
			if (newMove <= weight) {
				return units.get(i).generateRandomMove(this, pSequences, random);
			}
			newMove -= weight;
		}

		return null;
	}

	/**
	 * Randomly generate a break point in the given sequence.
	 * 
	 * @param sequence
	 * @param random 
	 * @return
	 */
	public int generateBreakPoint(@NonNull final ISequence sequence, Random random) {
		final int breakPoint;

		// if (true) {
		// Skip start/end as breakpoint options
		breakPoint = random.nextInt(sequence.size() - 1) + 1;
		// } else {
		// breakPoint = random.nextInt(1 + sequence.size());
		// }

		// Validate break point -- should it include start/end elements?

		assert breakPoint > 0;
		assert breakPoint < sequence.size();

		return breakPoint;
	}

	/**
	 * Generate a list of break points in linear order. The number of break points is defined by the size of the input array.
	 * 
	 * @param sequence
	 * @param breakPoints
	 * @param random 
	 */
	public void generateSortedBreakPoints(final ISequence sequence, final int[] breakPoints, Random random) {

		// Generate the break points
		for (int i = 0; i < breakPoints.length; ++i) {
			breakPoints[i] = generateBreakPoint(sequence, random);
		}

		// Sort the list
		Arrays.sort(breakPoints);
	}

	/**
	 * Register a {@link IRandomMoveGeneratorUnit} so a new {@link IMove} type can be created.
	 * 
	 * @param unit
	 */
	public void addMoveGeneratorUnit(final IRandomMoveGeneratorUnit unit, final double weight) {
		totalWeight += weight;
		weights.add(weight);
		units.add(unit);
	}

	public void addMoveGeneratorUnit(@NonNull final IRandomMoveGeneratorUnit unit) {
		this.addMoveGeneratorUnit(unit, 1);
	}

	/**
	 * Remove a previously registered {@link IRandomMoveGeneratorUnit}.
	 * 
	 * @param unit
	 */
	public void removeMoveGeneratorUnit(@NonNull final IRandomMoveGeneratorUnit unit) {
		final int i = units.indexOf(unit);
		if (i != -1) {
			totalWeight -= weights.get(i);
			units.remove(i);
			weights.remove(i);
		}
	}

	/**
	 * Setter to register a {@link Collection} of {@link IRandomMoveGeneratorUnit} objects.
	 * 
	 * @param units
	 */
	public void setMoveGeneratorUnits(final Collection<IRandomMoveGeneratorUnit> units) {
		this.units.addAll(units);
	}
}
