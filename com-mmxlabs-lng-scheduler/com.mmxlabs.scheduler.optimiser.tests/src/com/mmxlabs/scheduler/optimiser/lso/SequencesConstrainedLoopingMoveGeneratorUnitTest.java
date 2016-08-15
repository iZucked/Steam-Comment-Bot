package com.mmxlabs.scheduler.optimiser.lso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class SequencesConstrainedLoopingMoveGeneratorUnitTest {

	ConstrainedMoveGenerator mockOwner = Mockito.mock(ConstrainedMoveGenerator.class);
	ISequenceElement mockSequenceA = Mockito.mock(ISequenceElement.class);
	ISequenceElement mockSequenceB = Mockito.mock(ISequenceElement.class);
	Pair<ISequenceElement, ISequenceElement> mockedPair = new Pair<>(mockSequenceA, mockSequenceB);

	Map<ISequenceElement, Pair<IResource, Integer>> mockReverseLookup = new HashMap<>();

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

		mockReverseLookup.put(mockSequenceA, firstMockPair);
		mockReverseLookup.put(mockSequenceB, secondMockPair);

		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);

		final Random x = new Random();

		Mockito.when(mockOwner.getRandom()).thenReturn(x);

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answer = loopingMoveGenerator.findEdge();

		final Pair<IResource, Integer> firstPair = answer.getFirst();
		final Pair<IResource, Integer> secondPair = answer.getSecond();

		// Match pairs, assert they are different
		assertEquals(firstMockPair, firstPair);
		assertNotNull(firstPair);
		assertEquals(secondMockPair, secondPair);
		assertNotNull(secondPair);
		assertNotEquals(firstPair, secondPair);

		mockReverseLookup = new HashMap<>();

		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);
		Mockito.when(mockOwner.getValidBreaks()).thenReturn(mockedList);

		loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(mockOwner);

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerFullLoop = loopingMoveGenerator.findEdge();

		final Pair<IResource, Integer> firstMockNullPair = new Pair<>(null, 1);
		final Pair<IResource, Integer> secondMockNullPair = new Pair<>(null, 2);

		mockReverseLookup = new HashMap<>();

		mockReverseLookup.put(mockSequenceA, firstMockNullPair);
		mockReverseLookup.put(mockSequenceB, secondMockNullPair);

		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerNullLoop = loopingMoveGenerator.findEdge();

		// Check maximum loops
		assertNull(answerNullLoop);

	}

}
