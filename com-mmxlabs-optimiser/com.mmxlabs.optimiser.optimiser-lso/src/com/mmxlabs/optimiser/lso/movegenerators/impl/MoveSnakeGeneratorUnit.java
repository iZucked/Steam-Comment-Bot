/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.MoveSnake;

public final class MoveSnakeGeneratorUnit implements IRandomMoveGeneratorUnit {

	@Override
	@Nullable
	public IMove generateRandomMove(@NonNull final RandomMoveGenerator moveGenerator, @NonNull final ISequences sequences) {

		final Random random = moveGenerator.getRandom();

		final List<IResource> resources = sequences.getResources();

		final int numResources = resources.size();

		// Requires three resources
		// (well two at least, but then it would be a Move4over2)
		if (numResources < 3) {
			return null;
		}

		// Generate number between 3 and num resources (inclusive)
		// +3 to offset by 1 and then to re-add min size
		final int numChangedResources = random.nextInt(numResources - 2) + 3;

		// randomly pick numChangedResources IResources from the resources list
		// to generate the froms
		final List<IResource> froms = new ArrayList<IResource>(resources);
		Collections.shuffle(froms, random);
		int numToRemove = numResources - numChangedResources;
		while (numToRemove-- > 0) {
			froms.remove(0);
		}

		// Create the tos list by shifting (and wrapping) the froms by one place
		final List<IResource> tos = new ArrayList<IResource>(froms);
		Collections.rotate(tos, 1);

		final List<Integer> startPositions = new ArrayList<Integer>(numChangedResources);
		final List<Integer> endPositions = new ArrayList<Integer>(numChangedResources);
		final List<Integer> insertPositions = new ArrayList<Integer>(numChangedResources);

		// Fill in array so set ops don't fail
		for (int i = 0; i < numChangedResources; ++i) {
			startPositions.add(i, -1);
			endPositions.add(i, -1);
			insertPositions.add(i, -1);
		}

		// Generate outgoing segment
		final int[] breakPoints = new int[3];
		for (int i = 0; i < numChangedResources; ++i) {
			IResource resource = froms.get(i);
			assert resource != null;
			final ISequence sequence = sequences.getSequence(resource);
			moveGenerator.generateSortedBreakPoints(sequence, breakPoints);

			// Randomly pick the insertion point as first or last break point
			if (random.nextBoolean()) {
				startPositions.set(i, breakPoints[0]);
				endPositions.set(i, breakPoints[1]);
				// TODO: Why was this error not picked up before? -- only two routes?
				insertPositions.set((i + 1) % numChangedResources, breakPoints[2]);
			} else {
				startPositions.set(i, breakPoints[1]);
				endPositions.set(i, breakPoints[2]);
				insertPositions.set((i + 1) % numChangedResources, breakPoints[0]);
			}
		}

		// Create new move
		final MoveSnake move = new MoveSnake();

		// Set resources
		move.setFromResources(froms);
		move.setToResources(tos);

		// Set break points
		move.setSegmentStarts(startPositions);
		move.setSegmentEnds(endPositions);

		move.setInsertionPositions(insertPositions);

		return move;
	}

}
