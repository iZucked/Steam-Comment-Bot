/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.AfterElementFinder;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.IFinder;

public class InsertCargoMoveTest {

	@Test
	public void testInsertUsedCargo() {

		final ISequenceElement elementResourceAStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceAEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementResourceBStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceBEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);
		final IResource resourceB = Mockito.mock(IResource.class);

		final IFinder insertionFinder = new AfterElementFinder(elementResourceBStart);

		final InsertSegmentMove move = new InsertSegmentMove(resourceA, Lists.newArrayList(elementA, elementB), resourceB, insertionFinder);

		Assertions.assertEquals(2, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceA));
		Assertions.assertTrue(move.getAffectedResources().contains(resourceB));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList();
		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementResourceAStart, elementA, elementB, elementC, elementD, elementResourceAEnd));
		final ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList(elementResourceBStart, elementResourceBEnd));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceB)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceB)).thenReturn(sequenceB);

		move.apply(sequences);

		Assertions.assertTrue(modifiableUnusedSequences.isEmpty());
		Assertions.assertEquals(4, sequenceA.size());
		Assertions.assertEquals(4, sequenceB.size());

		Assertions.assertSame(elementResourceAStart, sequenceA.get(0));
		Assertions.assertSame(elementC, sequenceA.get(1));
		Assertions.assertSame(elementD, sequenceA.get(2));
		Assertions.assertSame(elementResourceAEnd, sequenceA.get(3));

		Assertions.assertSame(elementResourceBStart, sequenceB.get(0));
		Assertions.assertSame(elementA, sequenceB.get(1));
		Assertions.assertSame(elementB, sequenceB.get(2));
		Assertions.assertSame(elementResourceBEnd, sequenceB.get(3));
	}

	@Test
	public void testInsertUnusedCargo() {

		final ISequenceElement elementResourceAStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceAEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementResourceBStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceBEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);
		final IResource resourceB = Mockito.mock(IResource.class);

		final IFinder insertionFinder = new AfterElementFinder(elementResourceBStart);

		final InsertSegmentMove move = new InsertSegmentMove(null, Lists.newArrayList(elementA, elementB), resourceB, insertionFinder);

		Assertions.assertEquals(1, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceB));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementB, elementA);
		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementResourceAStart, elementC, elementD, elementResourceAEnd));
		final ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList(elementResourceBStart, elementResourceBEnd));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceA)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceA)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceB)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceB)).thenReturn(sequenceB);

		move.apply(sequences);

		Assertions.assertTrue(modifiableUnusedSequences.isEmpty());
		Assertions.assertEquals(4, sequenceA.size());
		Assertions.assertEquals(4, sequenceB.size());

		Assertions.assertSame(elementResourceAStart, sequenceA.get(0));
		Assertions.assertSame(elementC, sequenceA.get(1));
		Assertions.assertSame(elementD, sequenceA.get(2));
		Assertions.assertSame(elementResourceAEnd, sequenceA.get(3));

		Assertions.assertSame(elementResourceBStart, sequenceB.get(0));
		Assertions.assertSame(elementA, sequenceB.get(1));
		Assertions.assertSame(elementB, sequenceB.get(2));
		Assertions.assertSame(elementResourceBEnd, sequenceB.get(3));
	}

}
