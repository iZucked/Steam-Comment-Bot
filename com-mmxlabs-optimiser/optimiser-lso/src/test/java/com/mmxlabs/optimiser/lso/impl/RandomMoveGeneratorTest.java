package com.mmxlabs.optimiser.lso.impl;

import static org.junit.Assert.fail;

import java.util.Random;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.movegenerators.impl.IRandomMoveGeneratorUnit;
import com.mmxlabs.optimiser.lso.movegenerators.impl.RandomMoveGenerator;

@RunWith(JMock.class)
public class RandomMoveGeneratorTest {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings("unchecked")
	@Test
	public void testGenerateMove() {

		final Random random = new Random(1);

		final RandomMoveGenerator<Object> moveGenerator = new RandomMoveGenerator<Object>();

		final IRandomMoveGeneratorUnit<Object> unit = context
				.mock(IRandomMoveGeneratorUnit.class);

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
		fail("Not yet implemented");
	}

	@Test
	public void testRandomAccessors() {

		RandomMoveGenerator<Object> moveGenerator = new RandomMoveGenerator<Object>();

		// Initially should be null
		Assert.assertNull(moveGenerator.getRandom());

		Random random = new Random();

		moveGenerator.setRandom(random);
		Assert.assertSame(random, moveGenerator.getRandom());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSequencesAccessors() {

		RandomMoveGenerator<Object> moveGenerator = new RandomMoveGenerator<Object>();

		// Initially should be null
		Assert.assertNull(moveGenerator.getSequences());

		ISequences<Object> sequences = context.mock(ISequences.class);

		moveGenerator.setSequences(sequences);
		Assert.assertSame(sequences, moveGenerator.getSequences());
	}

	@Test
	public void testGenerateBreakPoint() {

		RandomMoveGenerator<Integer> moveGenerator = new RandomMoveGenerator<Integer>();
		moveGenerator.setRandom(new Random());

		ISequence<Integer> sequence = OptimiserTestUtil.makeSequence(1);
		
		// Run it a few times to let rng take its course
		for (int i = 0; i < 10; ++i) {
			int bp = moveGenerator.generateBreakPoint(sequence);
			Assert.assertTrue(bp >= 0);
			Assert.assertTrue(bp <= sequence.size());

		}
	}

	@Test
	public void testGenerateSortedBreakPoints() {
		fail("Not yet implemented");
	}
}
