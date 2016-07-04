/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;

public class SwapSingleSequenceElementsTest {

	@Test
	public void test1() {
		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		final List<ISequenceElement> elements = new ArrayList<ISequenceElement>();
		elements.add(elementA);
		elements.add(elementB);
		elements.add(elementC);
		elements.add(elementD);

		final ListModifiableSequence seq = new ListModifiableSequence(elements);
		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 0, 1);

		Assert.assertEquals(1, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resource));

		move.apply(sequences);

		Assert.assertEquals(elementB, seq.get(0));
		Assert.assertEquals(elementA, seq.get(1));
		Assert.assertEquals(elementC, seq.get(2));
		Assert.assertEquals(elementD, seq.get(3));
	}

	@Test
	public void test2() {
		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		final List<ISequenceElement> elements = new ArrayList<ISequenceElement>();
		elements.add(elementA);
		elements.add(elementB);
		elements.add(elementC);
		elements.add(elementD);

		final ListModifiableSequence seq = new ListModifiableSequence(elements);
		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 2, 1);

		Assert.assertEquals(1, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resource));

		move.apply(sequences);

		Assert.assertEquals(elementA, seq.get(0));
		Assert.assertEquals(elementC, seq.get(1));
		Assert.assertEquals(elementB, seq.get(2));
		Assert.assertEquals(elementD, seq.get(3));
	}

	@Test
	public void testValidate1() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 0, 1);

		Assert.assertTrue(move.validate(sequences));
	}

	@Test
	public void testValidate2() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - both indexes are the same
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 1, 1);

		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testValidate3() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - both index is negative
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, -1, 1);

		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testValidate4() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - both index is negative
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 1, -1);

		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testValidate5() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - both index is too large
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 2, 1);

		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testValidate6() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - both index is too large
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(resource, 1, 6);

		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testValidate7s() {

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);
		final IModifiableSequence seq = Mockito.mock(IModifiableSequence.class);

		Mockito.when(sequences.size()).thenReturn(1);
		Mockito.when(sequences.getSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getSequence(resource)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(0)).thenReturn(seq);
		Mockito.when(sequences.getModifiableSequence(resource)).thenReturn(seq);

		Mockito.when(seq.size()).thenReturn(2);

		// Fail - resource is null
		final SwapSingleSequenceElements move = new SwapSingleSequenceElements(null, 1, 0);

		Assert.assertFalse(move.validate(sequences));
	}
}
