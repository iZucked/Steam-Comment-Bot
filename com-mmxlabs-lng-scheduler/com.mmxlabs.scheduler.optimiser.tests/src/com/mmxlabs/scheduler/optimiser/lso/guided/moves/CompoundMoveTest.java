/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.moves.IMove;

public class CompoundMoveTest {

	@Test
	public void testNoChange() {
		final List<IMove> moveList = new LinkedList<>();
		final CompoundMove move = new CompoundMove(moveList);
		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		move.apply(sequences);

		Mockito.verifyNoMoreInteractions(sequences);

		Assert.assertTrue(move.getAffectedResources().isEmpty());
	}

	@Test
	public void testListCopied_1() {
		final List<IMove> moveList = new LinkedList<>();
		final CompoundMove move = new CompoundMove(moveList);

		// This move should not affect the compound move as the list should have already been processed by now
		final IMove extraMove = Mockito.mock(IMove.class);
		Mockito.doThrow(RuntimeException.class).when(extraMove).apply(Matchers.any());
		Mockito.when(extraMove.validate(Matchers.any())).thenThrow(RuntimeException.class);
		moveList.add(extraMove);

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		move.apply(sequences);

		Mockito.verifyNoMoreInteractions(sequences);

		Assert.assertTrue(move.getAffectedResources().isEmpty());
	}

	@Test
	public void testListCopied_2() {
		final List<IMove> moveList = new LinkedList<>();

		final IResource resource1 = Mockito.mock(IResource.class);
		final List<IResource> resources = Lists.newArrayList(resource1);

		final IMove extraMove = Mockito.mock(IMove.class);
		Mockito.when(extraMove.validate(Matchers.any())).thenReturn(Boolean.TRUE);
		Mockito.when(extraMove.getAffectedResources()).thenReturn(resources);
		moveList.add(extraMove);

		final CompoundMove move = new CompoundMove(moveList);

		// Should have no effect as should already have been processed
		moveList.clear();

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		move.apply(sequences);

		Mockito.verify(extraMove).apply(sequences);
		Mockito.verifyNoMoreInteractions(sequences);

		Assert.assertEquals(1, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resource1));
	}

	@Test
	public void testMoveOrder() {
		final List<IMove> moveList = new LinkedList<>();

		final IResource resource1 = Mockito.mock(IResource.class);
		final IResource resource2 = Mockito.mock(IResource.class);
		final List<IResource> resources1 = Lists.newArrayList(resource1);
		final List<IResource> resources2 = Lists.newArrayList(resource2);

		final IMove extraMove1 = Mockito.mock(IMove.class);
		Mockito.when(extraMove1.validate(Matchers.any())).thenReturn(Boolean.TRUE);
		Mockito.when(extraMove1.getAffectedResources()).thenReturn(resources1);
		moveList.add(extraMove1);

		final IMove extraMove2 = Mockito.mock(IMove.class);
		Mockito.when(extraMove2.validate(Matchers.any())).thenReturn(Boolean.TRUE);
		Mockito.when(extraMove2.getAffectedResources()).thenReturn(resources2);
		moveList.add(extraMove2);

		final CompoundMove move = new CompoundMove(moveList);

		final IModifiableSequences sequences = Mockito.mock(IModifiableSequences.class);

		move.apply(sequences);

		// Ensure moves applied in order
		final InOrder order = Mockito.inOrder(extraMove1, extraMove2);
		order.verify(extraMove1).apply(sequences);
		order.verify(extraMove2).apply(sequences);

		Mockito.verifyNoMoreInteractions(sequences);

		// Verify merged resources list
		Assert.assertEquals(2, move.getAffectedResources().size());
		Assert.assertTrue(move.getAffectedResources().contains(resource1));
		Assert.assertTrue(move.getAffectedResources().contains(resource2));
	}

}
