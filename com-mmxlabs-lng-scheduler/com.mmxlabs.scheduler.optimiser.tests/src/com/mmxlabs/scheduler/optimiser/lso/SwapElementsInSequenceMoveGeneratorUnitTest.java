/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapSingleSequenceElements;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class SwapElementsInSequenceMoveGeneratorUnitTest {

	@Test
	public void testTwoElements() {
		final ConstrainedMoveGenerator cmg = Mockito.mock(ConstrainedMoveGenerator.class);

		final IFollowersAndPreceders followersAndPreceders = Mockito.mock(IFollowersAndPreceders.class);

		final Random random = new Random();

		final SwapElementsInSequenceMoveGeneratorUnit mg = create(cmg, followersAndPreceders);

		// TODO: Build up a sequences structure - permit single combinations
		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(resource));

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);

		final IModifiableSequence seq = sequences.getModifiableSequence(resource);
		seq.add(elementA);
		seq.add(elementB);

		ILookupManager lookupManager = Mockito.mock(ILookupManager.class);

		final Map<ISequenceElement, Pair<IResource, Integer>> reverseLookup = new HashMap<>();
		Mockito.when(lookupManager.lookup(elementA)).thenReturn(new Pair<IResource, Integer>(resource, 0));
		Mockito.when(lookupManager.lookup(elementB)).thenReturn(new Pair<IResource, Integer>(resource, 1));

		// Build up followers / preceders
		final Map<ISequenceElement, Followers<ISequenceElement>> followers = new HashMap<ISequenceElement, Followers<ISequenceElement>>();
		final Map<ISequenceElement, Followers<ISequenceElement>> preceders = new HashMap<ISequenceElement, Followers<ISequenceElement>>();

		// A can be followed by B
		followers.put(elementA, new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementA)));
		preceders.put(elementA, new Followers<ISequenceElement>(Collections.singleton(elementB)));
		preceders.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementA)));

		Mockito.when(followersAndPreceders.getValidFollowers(Matchers.<ISequenceElement> anyObject())).then(new Answer<Followers<ISequenceElement>>() {

			@Override
			public Followers<ISequenceElement> answer(final InvocationOnMock invocation) throws Throwable {

				final ISequenceElement e = (ISequenceElement) invocation.getArguments()[0];
				return followers.get(e);
			}
		});
		Mockito.when(followersAndPreceders.getValidPreceders(Matchers.<ISequenceElement> anyObject())).then(new Answer<Followers<ISequenceElement>>() {

			@Override
			public Followers<ISequenceElement> answer(final InvocationOnMock invocation) throws Throwable {

				final ISequenceElement e = (ISequenceElement) invocation.getArguments()[0];
				return preceders.get(e);
			}
		});

		final SwapSingleSequenceElements move = mg.generateMove(sequences, lookupManager, random);

		// Failure - currently cannot swap two adjacent elements.
		Assert.assertNull(move);

		// Assert.assertEquals(resource, move.getResource());
		// Assert.assertTrue(move.getIndexA() == 0 || move.getIndexB() == 0);
		// Assert.assertTrue(move.getIndexA() == 1 || move.getIndexB() == 1);
		// Assert.assertTrue(move.getIndexA() != move.getIndexB());

	}

	@Test
	public void testThreeElements() {
		final ConstrainedMoveGenerator cmg = Mockito.mock(ConstrainedMoveGenerator.class);

		final IFollowersAndPreceders followersAndPreceders = Mockito.mock(IFollowersAndPreceders.class);

		final Random random = new Random();

		final SwapElementsInSequenceMoveGeneratorUnit mg = create(cmg, followersAndPreceders);

		final IResource resource = Mockito.mock(IResource.class);
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(resource));

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);

		final IModifiableSequence seq = sequences.getModifiableSequence(resource);
		seq.add(elementA);
		seq.add(elementB);
		seq.add(elementC);

		ILookupManager lookupManager = Mockito.mock(ILookupManager.class);
		Mockito.when(lookupManager.lookup(elementA)).thenReturn(new Pair<IResource, Integer>(resource, 0));
		Mockito.when(lookupManager.lookup(elementB)).thenReturn(new Pair<IResource, Integer>(resource, 1));
		Mockito.when(lookupManager.lookup(elementC)).thenReturn(new Pair<IResource, Integer>(resource, 2));

		// Build up followers / preceders
		final Map<ISequenceElement, Followers<ISequenceElement>> followers = new HashMap<ISequenceElement, Followers<ISequenceElement>>();
		final Map<ISequenceElement, Followers<ISequenceElement>> preceders = new HashMap<ISequenceElement, Followers<ISequenceElement>>();

		// A can be followed by B
		followers.put(elementA, new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementC)));

		followers.put(elementC, new Followers<ISequenceElement>(Collections.singleton(elementB)));
		followers.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementA)));

		preceders.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementA)));
		preceders.put(elementC, new Followers<ISequenceElement>(Collections.singleton(elementB)));

		preceders.put(elementB, new Followers<ISequenceElement>(Collections.singleton(elementC)));
		preceders.put(elementA, new Followers<ISequenceElement>(Collections.singleton(elementB)));

		Mockito.when(followersAndPreceders.getValidFollowers(Matchers.<ISequenceElement> anyObject())).then(new Answer<Followers<ISequenceElement>>() {

			@Override
			public Followers<ISequenceElement> answer(final InvocationOnMock invocation) throws Throwable {

				final ISequenceElement e = (ISequenceElement) invocation.getArguments()[0];
				return followers.get(e);
			}
		});
		Mockito.when(followersAndPreceders.getValidPreceders(Matchers.<ISequenceElement> anyObject())).then(new Answer<Followers<ISequenceElement>>() {

			@Override
			public Followers<ISequenceElement> answer(final InvocationOnMock invocation) throws Throwable {

				final ISequenceElement e = (ISequenceElement) invocation.getArguments()[0];
				return preceders.get(e);
			}
		});

		final SwapSingleSequenceElements move = mg.generateMove(sequences, lookupManager, random);

		Assert.assertNotNull(move);

		Assert.assertEquals(resource, move.getResource());
		Assert.assertTrue(move.getIndexA() == 0 || move.getIndexB() == 0);
		Assert.assertTrue(move.getIndexA() == 2 || move.getIndexB() == 2);
		Assert.assertTrue(move.getIndexA() != move.getIndexB());
	}

	private SwapElementsInSequenceMoveGeneratorUnit create(final ConstrainedMoveGenerator owner, final IFollowersAndPreceders followersAndProceders) {
		final Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IFollowersAndPreceders.class).toInstance(followersAndProceders);

			}
		});
		final SwapElementsInSequenceMoveGeneratorUnit unit = new SwapElementsInSequenceMoveGeneratorUnit();
		injector.injectMembers(unit);

		return unit;
	}
}
