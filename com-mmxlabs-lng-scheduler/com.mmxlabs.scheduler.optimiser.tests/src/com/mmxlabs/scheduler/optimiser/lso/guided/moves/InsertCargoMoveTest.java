package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
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

		final InsertCargoMove move = new InsertCargoMove(resourceA, Lists.newArrayList(elementA, elementB), resourceB, insertionFinder);

		Assert.assertEquals(2, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resourceA));
		Assert.assertTrue(move.getAffectedResources().contains(resourceB));

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

		Assert.assertTrue(modifiableUnusedSequences.isEmpty());
		Assert.assertEquals(4, sequenceA.size());
		Assert.assertEquals(4, sequenceB.size());

		Assert.assertSame(elementResourceAStart, sequenceA.get(0));
		Assert.assertSame(elementC, sequenceA.get(1));
		Assert.assertSame(elementD, sequenceA.get(2));
		Assert.assertSame(elementResourceAEnd, sequenceA.get(3));

		Assert.assertSame(elementResourceBStart, sequenceB.get(0));
		Assert.assertSame(elementA, sequenceB.get(1));
		Assert.assertSame(elementB, sequenceB.get(2));
		Assert.assertSame(elementResourceBEnd, sequenceB.get(3));
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

		final InsertCargoMove move = new InsertCargoMove(null, Lists.newArrayList(elementA, elementB), resourceB, insertionFinder);

		Assert.assertEquals(1, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resourceB));

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

		Assert.assertTrue(modifiableUnusedSequences.isEmpty());
		Assert.assertEquals(4, sequenceA.size());
		Assert.assertEquals(4, sequenceB.size());

		Assert.assertSame(elementResourceAStart, sequenceA.get(0));
		Assert.assertSame(elementC, sequenceA.get(1));
		Assert.assertSame(elementD, sequenceA.get(2));
		Assert.assertSame(elementResourceAEnd, sequenceA.get(3));

		Assert.assertSame(elementResourceBStart, sequenceB.get(0));
		Assert.assertSame(elementA, sequenceB.get(1));
		Assert.assertSame(elementB, sequenceB.get(2));
		Assert.assertSame(elementResourceBEnd, sequenceB.get(3));
	}

}
