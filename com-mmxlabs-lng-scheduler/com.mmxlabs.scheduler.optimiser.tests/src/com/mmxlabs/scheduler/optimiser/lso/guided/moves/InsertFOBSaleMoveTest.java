/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

public class InsertFOBSaleMoveTest {

	@Test
	public void testInsertUnusedFOBPurchase() {

		final ISequenceElement elementFOBSaleStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementFOBSaleEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementFOBSale = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementFOBPurchase = Mockito.mock(ISequenceElement.class);

		final IResource resourceFOBSale = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(null, elementFOBPurchase);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceFOBSale, elementFOBSale);

		final InsertFOBSaleMove move = new InsertFOBSaleMove(resourceFOBSale, elementFOBSale, elementFOBPurchase, null);

		Assertions.assertEquals(1, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceFOBSale));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementFOBSale, elementFOBPurchase);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementFOBSaleStart, elementFOBSaleEnd));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceFOBSale)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceFOBSale)).thenReturn(sequenceA);

		move.apply(sequences);

		Assertions.assertTrue(modifiableUnusedSequences.isEmpty());

		Assertions.assertSame(elementFOBSaleStart, sequenceA.get(0));
		Assertions.assertSame(elementFOBPurchase, sequenceA.get(1));
		Assertions.assertSame(elementFOBSale, sequenceA.get(2));
		Assertions.assertSame(elementFOBSaleEnd, sequenceA.get(3));
	}

	@Test
	public void testInsertUsedFOBPurchase() {

		final ISequenceElement elementFOBSaleStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementFOBSaleEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementFOBSale = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementFOBPurchase = Mockito.mock(ISequenceElement.class);

		final IResource resourceFOBSale = Mockito.mock(IResource.class);
		final IResource resourceCurrentFOBPurchase = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(resourceCurrentFOBPurchase, elementFOBPurchase);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceFOBSale, elementFOBSale);

		final InsertFOBSaleMove move = new InsertFOBSaleMove(resourceFOBSale, elementFOBSale, elementFOBPurchase, resourceCurrentFOBPurchase);

		Assertions.assertEquals(2, move.getAffectedResources().size());
		Assertions.assertTrue(move.getAffectedResources().contains(resourceFOBSale));
		Assertions.assertTrue(move.getAffectedResources().contains(resourceCurrentFOBPurchase));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementFOBSale);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementFOBSaleStart, elementFOBSaleEnd));
		ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList());

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceFOBSale)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceFOBSale)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceCurrentFOBPurchase)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceCurrentFOBPurchase)).thenReturn(sequenceB);

		move.apply(sequences);

		Assertions.assertTrue(modifiableUnusedSequences.isEmpty());
		Assertions.assertEquals(0, sequenceB.size());

		Assertions.assertSame(elementFOBSaleStart, sequenceA.get(0));
		Assertions.assertSame(elementFOBPurchase, sequenceA.get(1));
		Assertions.assertSame(elementFOBSale, sequenceA.get(2));
		Assertions.assertSame(elementFOBSaleEnd, sequenceA.get(3));
	}

}
