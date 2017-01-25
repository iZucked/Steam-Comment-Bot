/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.movegenerators.impl.IRandomMoveGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;

public class RandomMoveGeneratorTest {

	@SuppressWarnings("null")
	@Test
	public void testGenerateMove() {

		final Random random = new Random(1);

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		final IRandomMoveGeneratorUnit unit = Mockito.mock(IRandomMoveGeneratorUnit.class);
		final ISequences sequences = Mockito.mock(ISequences.class);

		ILookupManager lookupManager = Mockito.mock(ILookupManager.class);

		moveGenerator.addMoveGeneratorUnit(unit);
		moveGenerator.generateMove(sequences, lookupManager, random);

		Mockito.verify(unit).generateRandomMove(moveGenerator, sequences, random);
		Mockito.verifyNoMoreInteractions(unit);
	}

	@Test
	public void testInit() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGenerateBreakPoint() {

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
		Random random = new Random();

		final ISequence sequence = OptimiserTestUtil.makeSequence(new IntegerElement(1), new IntegerElement(2), new IntegerElement(3));

		// Run it a few times to let rng take its course
		for (int i = 0; i < 10; ++i) {
			final int bp = moveGenerator.generateBreakPoint(sequence, random);
			Assert.assertTrue(bp >= 0);
			Assert.assertTrue(bp <= sequence.size());

		}
	}

	@Test
	public void testGenerateSortedBreakPoints() {
		// fail("Not yet implemented");
	}
}
