/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class SequencesConstrainedLoopingMoveGeneratorUnitTest {

	ConstrainedMoveGenerator mockOwner = Mockito.mock(ConstrainedMoveGenerator.class);
	ISequenceElement mockSequenceA = Mockito.mock(ISequenceElement.class);
	ISequenceElement mockSequenceB = Mockito.mock(ISequenceElement.class);
	Pair<ISequenceElement, ISequenceElement> mockedPair = new Pair<>(mockSequenceA, mockSequenceB);

	// Map<ISequenceElement, Pair<IResource, Integer>> mockReverseLookup = new HashMap<>();

	IResource mockResourceA = Mockito.mock(IResource.class);
	IResource mockResourceB = Mockito.mock(IResource.class);

	ArrayList<Pair<ISequenceElement, ISequenceElement>> mockedList = new ArrayList<>();

	SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(mockOwner);

	@Test
	public void test_SCLGU() {

		mockedList.add(mockedPair);

		Mockito.when(mockOwner.getValidBreaks()).thenReturn(mockedList);

		final Pair<IResource, Integer> firstMockPair = new Pair<>(mockResourceA, 1);
		final Pair<IResource, Integer> secondMockPair = new Pair<>(mockResourceB, 2);

		ILookupManager lookupManager = Mockito.mock(ILookupManager.class);
		Mockito.when(lookupManager.lookup(mockSequenceA)).thenReturn(firstMockPair);
		Mockito.when(lookupManager.lookup(mockSequenceB)).thenReturn(secondMockPair);

		final Random x = new Random();

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answer = loopingMoveGenerator.findEdge(lookupManager, x);

		final Pair<IResource, Integer> firstPair = answer.getFirst();
		final Pair<IResource, Integer> secondPair = answer.getSecond();

		// Match pairs, assert they are different
		assertEquals(firstMockPair, firstPair);
		assertNotNull(firstPair);
		assertEquals(secondMockPair, secondPair);
		assertNotNull(secondPair);
		assertNotEquals(firstPair, secondPair);

		lookupManager = Mockito.mock(ILookupManager.class);

		// Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);
		Mockito.when(mockOwner.getValidBreaks()).thenReturn(mockedList);

		loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(mockOwner);

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerFullLoop = loopingMoveGenerator.findEdge(lookupManager, x);

		final Pair<IResource, Integer> firstMockNullPair = new Pair<>(null, 1);
		final Pair<IResource, Integer> secondMockNullPair = new Pair<>(null, 2);

		lookupManager = Mockito.mock(ILookupManager.class);

		Mockito.when(lookupManager.lookup(mockSequenceA)).thenReturn(firstMockNullPair);
		Mockito.when(lookupManager.lookup(mockSequenceB)).thenReturn(secondMockNullPair);
		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerNullLoop = loopingMoveGenerator.findEdge(lookupManager, x);

		// Check maximum loops
		assertNull(answerNullLoop);

	}

}
