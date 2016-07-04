/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

public class MoveSnakeTest {

	@Test
	public void testApply() {

		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");

		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<@NonNull IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<@NonNull IResource, @NonNull IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final MoveSnake move = new MoveSnake();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		move.setFromResources(CollectionsUtil.makeArrayList(resource1, resource2));
		move.setToResources(CollectionsUtil.makeArrayList(resource2, resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start, resource2Start));
		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End, resource2End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(resource2Start, resource1Start));

		move.apply(sequences);

		Mockito.verify(sequence1).getSegment(resource1Start, resource1End);
		Mockito.verify(sequence2).getSegment(resource2Start, resource2End);
	}

	@Test
	public void testApplyOnData() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");
		final IResource resource3 = Mockito.mock(IResource.class, "resource3");

		final IModifiableSequence sequence1 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence sequence2 = OptimiserTestUtil.makeSequence(6, 7, 8, 9, 10);
		final IModifiableSequence sequence3 = OptimiserTestUtil.makeSequence(11, 12, 13, 14, 15);

		final List<@NonNull IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2, resource3);

		final Map<@NonNull IResource, @NonNull IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2, resource3, sequence3);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final MoveSnake move = new MoveSnake();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final int resource3Start = 2;
		final int resource3End = 4;

		move.setFromResources(CollectionsUtil.makeArrayList(resource1, resource2, resource3));
		move.setToResources(CollectionsUtil.makeArrayList(resource2, resource3, resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start, resource2Start, resource3Start));
		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End, resource2End, resource3End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(resource2Start, resource3Start, resource1Start));

		move.apply(sequences);

		final List<ISequenceElement> expectedSequence1 = OptimiserTestUtil.makeList(13, 14, 3, 4, 5);
		final List<ISequenceElement> expectedSequence2 = OptimiserTestUtil.makeList(6, 1, 2, 9, 10);
		final List<ISequenceElement> expectedSequence3 = OptimiserTestUtil.makeList(11, 12, 7, 8, 15);

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		for (int i = 0; i < expectedSequence1.size(); ++i) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}

		Assert.assertEquals(expectedSequence2.size(), sequence2.size());
		for (int i = 0; i < expectedSequence2.size(); ++i) {
			Assert.assertEquals(expectedSequence2.get(i), sequence2.get(i));
		}

		Assert.assertEquals(expectedSequence3.size(), sequence3.size());
		for (int i = 0; i < expectedSequence3.size(); ++i) {
			Assert.assertEquals(expectedSequence3.get(i), sequence3.get(i));
		}
	}

	@Test
	public void testValidate() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");
		final IResource resource3 = Mockito.mock(IResource.class, "resource3");

		final IModifiableSequence sequence1 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence sequence2 = OptimiserTestUtil.makeSequence(6, 7, 8, 9, 10);
		final IModifiableSequence sequence3 = OptimiserTestUtil.makeSequence(11, 12, 13, 14, 15);

		final List<@NonNull IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2, resource3);

		final Map<@NonNull IResource, @NonNull IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2, resource3, sequence3);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final MoveSnake move = new MoveSnake();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final int resource3Start = 2;
		final int resource3End = 4;

		// This should check the null pointer checks in validate

		move.setFromResources(CollectionsUtil.makeArrayList(resource1, resource2, resource3));

		move.setToResources(CollectionsUtil.makeArrayList(resource2, resource3, resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start, resource2Start, resource3Start));

		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End, resource2End, resource3End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(resource2Start, resource3Start, resource1Start));

		Assert.assertTrue(move.validate(sequences));

		{
			final List<@NonNull IResource> oldFroms = move.getFromResources();
			move.setFromResources(null);
			Assert.assertFalse(move.validate(sequences));
			move.setFromResources(oldFroms);
		}
		{
			final List<@NonNull IResource> oldTos = move.getToResources();
			move.setToResources(null);
			Assert.assertFalse(move.validate(sequences));
			move.setToResources(oldTos);
		}
		{
			final List<@NonNull Integer> old = move.getSegmentStarts();
			move.setSegmentStarts(null);
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentStarts(old);
		}

		{
			final List<@NonNull Integer> old = move.getSegmentEnds();
			move.setSegmentEnds(null);
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentEnds(old);
		}

		{
			final List<@NonNull Integer> old = move.getInsertionPositions();
			move.setInsertionPositions(null);
			Assert.assertFalse(move.validate(sequences));
			move.setInsertionPositions(old);
		}

		// Check other validate conditions

		// Duplicate froms
		{
			final List<@NonNull IResource> oldFroms = move.getFromResources();
			move.setFromResources(CollectionsUtil.makeArrayList(resource1, resource1));
			Assert.assertFalse(move.validate(sequences));
			move.setFromResources(oldFroms);
		}
		// Duplicate tos
		{
			final List<@NonNull IResource> oldTos = move.getToResources();
			move.setToResources(CollectionsUtil.makeArrayList(resource1, resource1));
			Assert.assertFalse(move.validate(sequences));
			move.setToResources(oldTos);
		}
		// Negative segment start
		{
			final List<@NonNull Integer> old = move.getSegmentStarts();
			move.setSegmentStarts(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentStarts(old);
		}

		// Negative segment end
		{
			final List<@NonNull Integer> old = move.getSegmentEnds();
			move.setSegmentEnds(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentEnds(old);
		}

		// Negative insert positions
		{
			final List<@NonNull Integer> old = move.getInsertionPositions();
			move.setInsertionPositions(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setInsertionPositions(old);
		}

		// end < start
		{
			final List<@NonNull Integer> start = move.getSegmentEnds();
			final List<@NonNull Integer> end = move.getSegmentEnds();
			move.setSegmentStarts(CollectionsUtil.makeArrayList(5));
			move.setSegmentEnds(CollectionsUtil.makeArrayList(3));
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentStarts(start);
			move.setSegmentEnds(end);
		}

		// TODO: end > size()

	}

	@Test
	public void testGetSetFromResources() {

		final MoveSnake move = new MoveSnake();
		Assert.assertNull(move.getFromResources());
		final List<@NonNull IResource> fromResources = Collections.emptyList();
		move.setFromResources(fromResources);
		Assert.assertSame(fromResources, move.getFromResources());
	}

	@Test
	public void testGetSetToResources() {

		final MoveSnake move = new MoveSnake();
		Assert.assertNull(move.getToResources());
		final List<@NonNull IResource> toResources = Collections.emptyList();
		move.setToResources(toResources);
		Assert.assertSame(toResources, move.getToResources());
	}

	@Test
	public void testGetSetSegmentStarts() {

		final MoveSnake move = new MoveSnake();
		Assert.assertNull(move.getSegmentStarts());
		final List<@NonNull Integer> starts = Collections.emptyList();
		move.setSegmentStarts(starts);
		Assert.assertSame(starts, move.getSegmentStarts());
	}

	@Test
	public void testGetSetSegmentEnds() {

		final MoveSnake move = new MoveSnake();
		Assert.assertNull(move.getSegmentEnds());
		final List<@NonNull Integer> ends = Collections.emptyList();
		move.setSegmentEnds(ends);
		Assert.assertSame(ends, move.getSegmentEnds());
	}

	@Test
	public void testGetSetInsertionPositions() {

		final MoveSnake move = new MoveSnake();
		Assert.assertNull(move.getInsertionPositions());
		final List<@NonNull Integer> inserts = Collections.emptyList();
		move.setInsertionPositions(inserts);
		Assert.assertSame(inserts, move.getInsertionPositions());
	}

	@Test
	public void testGetAffectedResources() {
		final @NonNull IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final @NonNull IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final MoveSnake move = new MoveSnake();

		move.setFromResources(CollectionsUtil.makeArrayList(resource1));

		move.setToResources(CollectionsUtil.makeArrayList(resource2));

		final Collection<IResource> affectedResources = move.getAffectedResources();

		Assert.assertNotNull(affectedResources);
		Assert.assertEquals(2, affectedResources.size());
		Assert.assertTrue(affectedResources.contains(resource1));
		Assert.assertTrue(affectedResources.contains(resource2));
	}
}
