/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator.Followers;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapSingleSequenceElements;

public class SwapElementsInSequenceMoveGeneratorUnitTest {

	@Test
	public void testTwoElements() {
		final ConstrainedMoveGenerator cmg = Mockito.mock(ConstrainedMoveGenerator.class);

		final Random random = new Random();
		Mockito.when(cmg.getRandom()).thenReturn(random);

		final SwapElementsInSequenceMoveGeneratorUnit mg = create(cmg);

		// TODO: Build up a sequences structure - permit single combinations
		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(resource));

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);

		final IModifiableSequence seq = sequences.getModifiableSequence(resource);
		seq.add(elementA);
		seq.add(elementB);

		final Map<ISequenceElement, Pair<Integer, Integer>> reverseLookup = new HashMap<ISequenceElement, Pair<Integer, Integer>>();
		reverseLookup.put(elementA, new Pair<Integer, Integer>(0, 0));
		reverseLookup.put(elementB, new Pair<Integer, Integer>(0, 1));

		// Build up followers / preceeders
		final Map<ISequenceElement, Followers<ISequenceElement>> followers = new HashMap<ISequenceElement, ConstrainedMoveGenerator.Followers<ISequenceElement>>();
		final Map<ISequenceElement, Followers<ISequenceElement>> preceeders = new HashMap<ISequenceElement, ConstrainedMoveGenerator.Followers<ISequenceElement>>();

		// A can be followed by B
		followers.put(elementA, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementA)));
		preceeders.put(elementA, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));
		preceeders.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementA)));

		Mockito.when(cmg.getReverseLookup()).thenReturn(reverseLookup);
		Mockito.when(cmg.getValidFollowers()).thenReturn(followers);
		Mockito.when(cmg.getValidPreceeders()).thenReturn(preceeders);
		Mockito.when(cmg.getSequences()).thenReturn(sequences);

		final SwapSingleSequenceElements move = mg.generateMove();

		// Failure - currently cannot swap two adjacent elements.
		Assert.assertNull(move);

		// Assert.assertEquals(resource, move.getResource());
		// Assert.assertTrue(move.getIndexA() == 0 || move.getIndexB() == 0);
		// Assert.assertTrue(move.getIndexA() == 1 || move.getIndexB() == 1);
		// Assert.assertTrue(move.getIndexA() != move.getIndexB());

	}

	@Test
	public void testThreeElements() {
		final ConstrainedMoveGenerator cmg = Mockito.mock(ConstrainedMoveGenerator.class);

		final Random random = new Random();
		Mockito.when(cmg.getRandom()).thenReturn(random);

		final SwapElementsInSequenceMoveGeneratorUnit mg = create(cmg);

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(resource));

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);

		final IModifiableSequence seq = sequences.getModifiableSequence(resource);
		seq.add(elementA);
		seq.add(elementB);
		seq.add(elementC);

		final Map<ISequenceElement, Pair<Integer, Integer>> reverseLookup = new HashMap<ISequenceElement, Pair<Integer, Integer>>();
		reverseLookup.put(elementA, new Pair<Integer, Integer>(0, 0));
		reverseLookup.put(elementB, new Pair<Integer, Integer>(0, 1));
		reverseLookup.put(elementC, new Pair<Integer, Integer>(0, 2));

		// Build up followers / preceeders
		final Map<ISequenceElement, Followers<ISequenceElement>> followers = new HashMap<ISequenceElement, ConstrainedMoveGenerator.Followers<ISequenceElement>>();
		final Map<ISequenceElement, Followers<ISequenceElement>> preceeders = new HashMap<ISequenceElement, ConstrainedMoveGenerator.Followers<ISequenceElement>>();

		// A can be followed by B
		followers.put(elementA, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementC)));

		followers.put(elementC, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementA)));

		preceeders.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementA)));
		preceeders.put(elementC, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));

		preceeders.put(elementB, cmg.new Followers<ISequenceElement>(Collections.singleton(elementC)));
		preceeders.put(elementA, cmg.new Followers<ISequenceElement>(Collections.singleton(elementB)));

		Mockito.when(cmg.getReverseLookup()).thenReturn(reverseLookup);
		Mockito.when(cmg.getValidFollowers()).thenReturn(followers);
		Mockito.when(cmg.getValidPreceeders()).thenReturn(preceeders);
		Mockito.when(cmg.getSequences()).thenReturn(sequences);

		final SwapSingleSequenceElements move = mg.generateMove();

		Assert.assertNotNull(move);

		Assert.assertEquals(resource, move.getResource());
		Assert.assertTrue(move.getIndexA() == 0 || move.getIndexB() == 0);
		Assert.assertTrue(move.getIndexA() == 2 || move.getIndexB() == 2);
		Assert.assertTrue(move.getIndexA() != move.getIndexB());

	}

	private SwapElementsInSequenceMoveGeneratorUnit create(final ConstrainedMoveGenerator owner) {
		return new SwapElementsInSequenceMoveGeneratorUnit(owner);
	}

}
