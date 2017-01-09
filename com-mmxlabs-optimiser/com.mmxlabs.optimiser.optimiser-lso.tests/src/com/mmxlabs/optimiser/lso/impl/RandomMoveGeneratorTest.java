/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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

		moveGenerator.setRandom(random);
		moveGenerator.addMoveGeneratorUnit(unit);
		moveGenerator.setSequences(sequences);
		moveGenerator.generateMove();

		Mockito.verify(unit).generateRandomMove(moveGenerator, sequences);
		Mockito.verifyNoMoreInteractions(unit);
	}

	@Test
	public void testInit() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRandomAccessors() {

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		// Initially should be null
		Assert.assertNull(moveGenerator.getRandom());

		final Random random = new Random();

		moveGenerator.setRandom(random);
		Assert.assertSame(random, moveGenerator.getRandom());
	}

	@Test
	public void testSequencesAccessors() {

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		// Initially should be null
		Assert.assertNull(moveGenerator.getSequences());

		final ISequences sequences = Mockito.mock(ISequences.class);

		moveGenerator.setSequences(sequences);
		Assert.assertSame(sequences, moveGenerator.getSequences());
	}

	@Test
	public void testGenerateBreakPoint() {

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
		moveGenerator.setRandom(new Random());

		final ISequence sequence = OptimiserTestUtil.makeSequence(new IntegerElement(1), new IntegerElement(2), new IntegerElement(3));

		// Run it a few times to let rng take its course
		for (int i = 0; i < 10; ++i) {
			final int bp = moveGenerator.generateBreakPoint(sequence);
			Assert.assertTrue(bp >= 0);
			Assert.assertTrue(bp <= sequence.size());

		}
	}

	@Test
	public void testGenerateSortedBreakPoints() {
		// fail("Not yet implemented");
	}
}
