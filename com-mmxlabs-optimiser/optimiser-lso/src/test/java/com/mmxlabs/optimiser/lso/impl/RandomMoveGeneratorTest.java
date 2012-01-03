/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Random;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.movegenerators.impl.IRandomMoveGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;

@RunWith(JMock.class)
public class RandomMoveGeneratorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGenerateMove() {

		final Random random = new Random(1);

		final RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		final IRandomMoveGeneratorUnit unit = context.mock(IRandomMoveGeneratorUnit.class);

		moveGenerator.setRandom(random);
		moveGenerator.addMoveGeneratorUnit(unit);

		context.checking(new Expectations() {
			{
				// Expect these methods to be invoked once
				oneOf(unit).generateRandomMove(moveGenerator, null);
			}
		});

		moveGenerator.generateMove();

		context.assertIsSatisfied();
	}

	@Test
	public void testInit() {
		// fail("Not yet implemented");
	}

	@Test
	public void testRandomAccessors() {

		RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		// Initially should be null
		Assert.assertNull(moveGenerator.getRandom());

		Random random = new Random();

		moveGenerator.setRandom(random);
		Assert.assertSame(random, moveGenerator.getRandom());
	}

	@Test
	public void testSequencesAccessors() {

		RandomMoveGenerator moveGenerator = new RandomMoveGenerator();

		// Initially should be null
		Assert.assertNull(moveGenerator.getSequences());

		ISequences sequences = context.mock(ISequences.class);

		moveGenerator.setSequences(sequences);
		Assert.assertSame(sequences, moveGenerator.getSequences());
	}

	@Test
	public void testGenerateBreakPoint() {

		RandomMoveGenerator moveGenerator = new RandomMoveGenerator();
		moveGenerator.setRandom(new Random());

		ISequence sequence = OptimiserTestUtil.makeSequence(new IntegerElement(1), new IntegerElement(2), new IntegerElement(3));

		// Run it a few times to let rng take its course
		for (int i = 0; i < 10; ++i) {
			int bp = moveGenerator.generateBreakPoint(sequence);
			Assert.assertTrue(bp >= 0);
			Assert.assertTrue(bp <= sequence.size());

		}
	}

	@Test
	public void testGenerateSortedBreakPoints() {
		// fail("Not yet implemented");
	}
}
