/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

public class RemoveElementsMoveTest {

	@Test
	public void testRemoveElements() {

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

		final RemoveElementsMove move = new RemoveElementsMove(Lists.newArrayList(elementPairB, elementPairE));

		Assertions.assertEquals(2, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));
		Assertions.assertTrue(move.getAffectedResources().contains(resourceB));

		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementF);

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementA, elementB, elementC));
		final ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList(elementD, elementE));

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceB)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceB)).thenReturn(sequenceB);

		move.apply(sequences);

		// Check element order has swapped
		Assertions.assertSame(elementA, sequenceA.get(0));
		Assertions.assertSame(elementC, sequenceA.get(1));

		Assertions.assertSame(elementD, sequenceB.get(0));

		// Elements should be appended in input order
		Assertions.assertSame(elementF, modifiableUnusedSequences.get(0));
		Assertions.assertSame(elementB, modifiableUnusedSequences.get(1));
		Assertions.assertSame(elementE, modifiableUnusedSequences.get(2));

	}

}
