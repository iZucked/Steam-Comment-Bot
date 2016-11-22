package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

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

public class InsertDESPurchaseMoveTest {

	@Test
	public void testInsertUnusedDESSale() {

		final ISequenceElement elementDESPurchaseStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementDESPurchaseEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementDESPurchase = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementDESSale = Mockito.mock(ISequenceElement.class);

		final IResource resourceDESPurchase = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(null, elementDESSale);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceDESPurchase, elementDESPurchase);

		final InsertDESPurchaseMove move = new InsertDESPurchaseMove(resourceDESPurchase, elementDESPurchase, elementDESSale, null);

		Assert.assertEquals(1, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resourceDESPurchase));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementDESPurchase, elementDESSale);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementDESPurchaseStart, elementDESPurchaseEnd));

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceDESPurchase)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceDESPurchase)).thenReturn(sequenceA);

		move.apply(sequences);

		Assert.assertTrue(modifiableUnusedSequences.isEmpty());

		Assert.assertSame(elementDESPurchaseStart, sequenceA.get(0));
		Assert.assertSame(elementDESPurchase, sequenceA.get(1));
		Assert.assertSame(elementDESSale, sequenceA.get(2));
		Assert.assertSame(elementDESPurchaseEnd, sequenceA.get(3));
	}

	@Test
	public void testInsertUsedDESSale() {

		final ISequenceElement elementDESPurchaseStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementDESPurchaseEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementDESPurchase = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementDESSale = Mockito.mock(ISequenceElement.class);

		final IResource resourceDESPurchase = Mockito.mock(IResource.class);
		final IResource resourceCurrentDESSale = Mockito.mock(IResource.class);

		final Pair<IResource, ISequenceElement> elementPairB = new Pair<>(resourceCurrentDESSale, elementDESSale);
		final Pair<IResource, ISequenceElement> elementPairE = new Pair<>(resourceDESPurchase, elementDESPurchase);

		final InsertDESPurchaseMove move = new InsertDESPurchaseMove(resourceDESPurchase, elementDESPurchase, elementDESSale, resourceCurrentDESSale);

		Assert.assertEquals(2, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resourceDESPurchase));
		Assert.assertTrue(move.getAffectedResources().contains(resourceCurrentDESSale));

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final List<ISequenceElement> modifiableUnusedSequences = Lists.newArrayList(elementDESPurchase);
		ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementDESPurchaseStart, elementDESPurchaseEnd));
		ListModifiableSequence sequenceB = new ListModifiableSequence(Lists.newArrayList());

		Mockito.when(sequences.getModifiableUnusedElements()).thenReturn(modifiableUnusedSequences);
		Mockito.when(sequences.getUnusedElements()).thenReturn(modifiableUnusedSequences);

		Mockito.when(sequences.getModifiableSequence(resourceDESPurchase)).thenReturn(sequenceA);
		Mockito.when(sequences.getSequence(resourceDESPurchase)).thenReturn(sequenceA);

		Mockito.when(sequences.getModifiableSequence(resourceCurrentDESSale)).thenReturn(sequenceB);
		Mockito.when(sequences.getSequence(resourceCurrentDESSale)).thenReturn(sequenceB);

		move.apply(sequences);

		Assert.assertTrue(modifiableUnusedSequences.isEmpty());
		Assert.assertEquals(0, sequenceB.size());

		Assert.assertSame(elementDESPurchaseStart, sequenceA.get(0));
		Assert.assertSame(elementDESPurchase, sequenceA.get(1));
		Assert.assertSame(elementDESSale, sequenceA.get(2));
		Assert.assertSame(elementDESPurchaseEnd, sequenceA.get(3));
	}

}
