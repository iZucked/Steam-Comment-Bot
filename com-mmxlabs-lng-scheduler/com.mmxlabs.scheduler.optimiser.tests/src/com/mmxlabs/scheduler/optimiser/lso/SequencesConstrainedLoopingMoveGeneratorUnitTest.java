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
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

public class SequencesConstrainedLoopingMoveGeneratorUnitTest {

	private final ISequenceElement mockSequenceA = Mockito.mock(ISequenceElement.class);
	private final ISequenceElement mockSequenceB = Mockito.mock(ISequenceElement.class);
	private final Pair<ISequenceElement, ISequenceElement> mockedPair = new Pair<>(mockSequenceA, mockSequenceB);

	private final IResource mockResourceA = Mockito.mock(IResource.class);
	private final IResource mockResourceB = Mockito.mock(IResource.class);

	@Test
	public void test_SCLGU_1() {
		final List<Pair<ISequenceElement, ISequenceElement>> mockedList = new ArrayList<>();
		mockedList.add(mockedPair);

		final IBreakPointHelper breakPointHelper = Mockito.mock(IBreakPointHelper.class);
		Mockito.when(breakPointHelper.getValidBreaks()).thenReturn(mockedList);

		final SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = createMoveGenerator(breakPointHelper);

		final Pair<IResource, Integer> firstMockPair = new Pair<>(mockResourceA, 1);
		final Pair<IResource, Integer> secondMockPair = new Pair<>(mockResourceB, 2);

		final ILookupManager lookupManager = Mockito.mock(ILookupManager.class);
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
	}

	@Test
	public void test_SCLGU_2() {
		final List<Pair<ISequenceElement, ISequenceElement>> mockedList = new ArrayList<>();
		mockedList.add(mockedPair);

		final Random x = new Random();
		final IBreakPointHelper breakPointHelper = Mockito.mock(IBreakPointHelper.class);
		Mockito.when(breakPointHelper.getValidBreaks()).thenReturn(mockedList);

		final SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = createMoveGenerator(breakPointHelper);

		final ILookupManager lookupManager = Mockito.mock(ILookupManager.class);

		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerFullLoop = loopingMoveGenerator.findEdge(lookupManager, x);
	}

	@Test
	public void test_SCLGU_3() {
		final List<Pair<ISequenceElement, ISequenceElement>> mockedList = new ArrayList<>();
		mockedList.add(mockedPair);

		final Random x = new Random();
		final IBreakPointHelper breakPointHelper = Mockito.mock(IBreakPointHelper.class);
		Mockito.when(breakPointHelper.getValidBreaks()).thenReturn(mockedList);

		final SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = createMoveGenerator(breakPointHelper);

		final Pair<IResource, Integer> firstMockNullPair = new Pair<>(null, 1);
		final Pair<IResource, Integer> secondMockNullPair = new Pair<>(null, 2);

		final ILookupManager lookupManager = Mockito.mock(ILookupManager.class);

		Mockito.when(lookupManager.lookup(mockSequenceA)).thenReturn(firstMockNullPair);
		Mockito.when(lookupManager.lookup(mockSequenceB)).thenReturn(secondMockNullPair);
		final Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerNullLoop = loopingMoveGenerator.findEdge(lookupManager, x);

		// Check maximum loops
		assertNull(answerNullLoop);

	}

	private SequencesConstrainedLoopingMoveGeneratorUnit createMoveGenerator(final IBreakPointHelper breakPointHelper) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IBreakPointHelper.class).toInstance(breakPointHelper);
				bind(IFollowersAndPreceders.class).toInstance(Mockito.mock(IFollowersAndPreceders.class));
				bind(IMoveHelper.class).toInstance(Mockito.mock(IMoveHelper.class));
				bind(Boolean.class).annotatedWith(Names.named(SequencesConstrainedLoopingMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)).toInstance(Boolean.TRUE);
			}
		});
		final SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit();
		injector.injectMembers(loopingMoveGenerator);
		return loopingMoveGenerator;
	}
}
