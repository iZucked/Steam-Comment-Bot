/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;

public class SwapElementsMoveTest {

	@Test
	public void testUnusedElementsSwap() {

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);

		final Pair<IResource, ISequenceElement> elementPairA = new Pair<>(null, elementA);
		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(null, elementB);

		final SwapElementsMove move = new SwapElementsMove(elementPairA, elementPairB);

		Assertions.assertEquals(0, move.getAffectedResources().size());

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementA, elementB, elementC, elementD);

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementB, modifiableUnusedSequences.get(0));
		Assertions.assertSame(elementA, modifiableUnusedSequences.get(1));
		// Check element order is the same
		Assertions.assertSame(elementC, modifiableUnusedSequences.get(2));
		Assertions.assertSame(elementD, modifiableUnusedSequences.get(3));
	}

	@Test
	public void testUnusedToSequenceElementsSwap() {

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementF = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(null, elementB);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceA, elementE);

		final SwapElementsMove move = new SwapElementsMove(elementPairB, elementPairE);

		Assertions.assertEquals(1, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementA, elementB, elementC);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementD, elementE, elementF));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementA, modifiableUnusedSequences.get(0));
		Assertions.assertSame(elementE, modifiableUnusedSequences.get(1));
		Assertions.assertSame(elementC, modifiableUnusedSequences.get(2));

		Assertions.assertSame(elementD, sequenceA.get(0));
		Assertions.assertSame(elementB, sequenceA.get(1));
		Assertions.assertSame(elementF, sequenceA.get(2));
	}

	@Test
	public void testSequenceToUnusedElementsSwap() {

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementF = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(null, elementB);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceA, elementE);

		final SwapElementsMove move = new SwapElementsMove(elementPairE, elementPairB);

		Assertions.assertEquals(1, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementA, elementB, elementC);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementD, elementE, elementF));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementA, modifiableUnusedSequences.get(0));
		Assertions.assertSame(elementE, modifiableUnusedSequences.get(1));
		Assertions.assertSame(elementC, modifiableUnusedSequences.get(2));

		Assertions.assertSame(elementD, sequenceA.get(0));
		Assertions.assertSame(elementB, sequenceA.get(1));
		Assertions.assertSame(elementF, sequenceA.get(2));
	}

	@Test
	public void testSequenceToOtherSequenceElementsSwap() {

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementF = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);
		final IResource resourceB = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(resourceA, elementB);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceB, elementE);

		final SwapElementsMove move = new SwapElementsMove(elementPairB, elementPairE);

		Assertions.assertEquals(2, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));
		Assertions.assertTrue(move.getAffectedResources().contains(resourceB));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementA, elementB, elementC));
		ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList(elementD, elementE, elementF));

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceB)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceB)).thenReturn(sequenceB);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementA, sequenceA.get(0));
		Assertions.assertSame(elementE, sequenceA.get(1));
		Assertions.assertSame(elementC, sequenceA.get(2));

		Assertions.assertSame(elementD, sequenceB.get(0));
		Assertions.assertSame(elementB, sequenceB.get(1));
		Assertions.assertSame(elementF, sequenceB.get(2));
	}

	@Test
	public void testSequenceToSameSequenceElementsSwap() {

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementF = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(resourceA, elementB);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceA, elementE);

		final SwapElementsMove move = new SwapElementsMove(elementPairB, elementPairE);

		Assertions.assertEquals(1, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementA, elementB, elementC, elementD, elementE, elementF));

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementA, sequenceA.get(0));
		Assertions.assertSame(elementE, sequenceA.get(1));
		Assertions.assertSame(elementC, sequenceA.get(2));
		Assertions.assertSame(elementD, sequenceA.get(3));
		Assertions.assertSame(elementB, sequenceA.get(4));
		Assertions.assertSame(elementF, sequenceA.get(5));
	}
}
