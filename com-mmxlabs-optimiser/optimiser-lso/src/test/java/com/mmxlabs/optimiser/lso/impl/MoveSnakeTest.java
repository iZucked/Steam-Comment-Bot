package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

@RunWith(JMock.class)
public class MoveSnakeTest {
	Mockery context = new JUnit4Mockery();

	@SuppressWarnings("unchecked")
	@Test
	public void testApply() {

		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");

		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		final MoveSnake<Object> move = new MoveSnake<Object>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		move.setFromResources(CollectionsUtil.makeArrayList(resource1,
				resource2));
		move
				.setToResources(CollectionsUtil.makeArrayList(resource2,
						resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start,
				resource2Start));
		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End,
				resource2End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(
				resource2Start, resource1Start));

		context.checking(new Expectations() {
			{
				// Expect these methods to be invoked once
				oneOf(sequence1).getSegment(resource1Start, resource1End);
				oneOf(sequence2).getSegment(resource2Start, resource2End);

				// Allow any other method calls
				allowing(sequence1);
				allowing(sequence2);
			}
		});

		move.apply(sequences);

		context.assertIsSatisfied();
	}

	@Test
	public void testApplyOnData() {
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");
		final IResource resource3 = context.mock(IResource.class, "resource3");

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10));
		final IModifiableSequence<Integer> sequence3 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(11, 12, 13, 14, 15));

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2, resource3);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2,
						resource3, sequence3);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final MoveSnake<Integer> move = new MoveSnake<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final int resource3Start = 2;
		final int resource3End = 4;

		move.setFromResources(CollectionsUtil.makeArrayList(resource1,
				resource2, resource3));
		move.setToResources(CollectionsUtil.makeArrayList(resource2, resource3,
				resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start,
				resource2Start, resource3Start));
		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End,
				resource2End, resource3End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(
				resource2Start, resource3Start, resource1Start));

		move.apply(sequences);

		final List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(
				13, 14, 3, 4, 5);
		final List<Integer> expectedSequence2 = CollectionsUtil.makeArrayList(
				6, 1, 2, 9, 10);
		final List<Integer> expectedSequence3 = CollectionsUtil.makeArrayList(
				11, 12, 7, 8, 15);

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
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");
		final IResource resource3 = context.mock(IResource.class, "resource3");

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10));
		final IModifiableSequence<Integer> sequence3 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(11, 12, 13, 14, 15));

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2, resource3);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2,
						resource3, sequence3);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final MoveSnake<Integer> move = new MoveSnake<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final int resource3Start = 2;
		final int resource3End = 4;

		// This should check the null pointer checks in validate

		move.setFromResources(CollectionsUtil.makeArrayList(resource1,
				resource2, resource3));

		move.setToResources(CollectionsUtil.makeArrayList(resource2, resource3,
				resource1));

		move.setSegmentStarts(CollectionsUtil.makeArrayList(resource1Start,
				resource2Start, resource3Start));

		move.setSegmentEnds(CollectionsUtil.makeArrayList(resource1End,
				resource2End, resource3End));

		move.setInsertionPositions(CollectionsUtil.makeArrayList(
				resource2Start, resource3Start, resource1Start));

		Assert.assertTrue(move.validate(sequences));

		{
			final List<IResource> oldFroms = move.getFromResources();
			move.setFromResources(null);
			Assert.assertFalse(move.validate(sequences));
			move.setFromResources(oldFroms);
		}
		{
			final List<IResource> oldTos = move.getToResources();
			move.setToResources(null);
			Assert.assertFalse(move.validate(sequences));
			move.setToResources(oldTos);
		}
		{
			final List<Integer> old = move.getSegmentStarts();
			move.setSegmentStarts(null);
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentStarts(old);
		}

		{
			final List<Integer> old = move.getSegmentEnds();
			move.setSegmentEnds(null);
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentEnds(old);
		}

		{
			final List<Integer> old = move.getInsertionPositions();
			move.setInsertionPositions(null);
			Assert.assertFalse(move.validate(sequences));
			move.setInsertionPositions(old);
		}

		// Check other validate conditions

		// Duplicate froms
		{
			final List<IResource> oldFroms = move.getFromResources();
			move.setFromResources(CollectionsUtil.makeArrayList(resource1,
					resource1));
			Assert.assertFalse(move.validate(sequences));
			move.setFromResources(oldFroms);
		}
		// Duplicate tos
		{
			final List<IResource> oldTos = move.getToResources();
			move.setToResources(CollectionsUtil.makeArrayList(resource1,
					resource1));
			Assert.assertFalse(move.validate(sequences));
			move.setToResources(oldTos);
		}
		// Negative segment start
		{
			final List<Integer> old = move.getSegmentStarts();
			move.setSegmentStarts(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentStarts(old);
		}

		// Negative segment end
		{
			final List<Integer> old = move.getSegmentEnds();
			move.setSegmentEnds(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setSegmentEnds(old);
		}

		// Negative insert positions
		{
			final List<Integer> old = move.getInsertionPositions();
			move.setInsertionPositions(CollectionsUtil.makeArrayList(-1));
			Assert.assertFalse(move.validate(sequences));
			move.setInsertionPositions(old);
		}

		// end < start
		{
			final List<Integer> start = move.getSegmentEnds();
			final List<Integer> end = move.getSegmentEnds();
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

		final MoveSnake<Object> move = new MoveSnake<Object>();
		Assert.assertNull(move.getFromResources());
		final List<IResource> fromResources = Collections.emptyList();
		move.setFromResources(fromResources);
		Assert.assertSame(fromResources, move.getFromResources());
	}

	@Test
	public void testGetSetToResources() {

		final MoveSnake<Object> move = new MoveSnake<Object>();
		Assert.assertNull(move.getToResources());
		final List<IResource> toResources = Collections.emptyList();
		move.setToResources(toResources);
		Assert.assertSame(toResources, move.getToResources());
	}

	@Test
	public void testGetSetSegmentStarts() {

		final MoveSnake<Object> move = new MoveSnake<Object>();
		Assert.assertNull(move.getSegmentStarts());
		final List<Integer> starts = Collections.emptyList();
		move.setSegmentStarts(starts);
		Assert.assertSame(starts, move.getSegmentStarts());
	}

	@Test
	public void testGetSetSegmentEnds() {

		final MoveSnake<Object> move = new MoveSnake<Object>();
		Assert.assertNull(move.getSegmentEnds());
		final List<Integer> ends = Collections.emptyList();
		move.setSegmentEnds(ends);
		Assert.assertSame(ends, move.getSegmentEnds());
	}

	@Test
	public void testGetSetInsertionPositions() {

		final MoveSnake<Object> move = new MoveSnake<Object>();
		Assert.assertNull(move.getInsertionPositions());
		final List<Integer> inserts = Collections.emptyList();
		move.setInsertionPositions(inserts);
		Assert.assertSame(inserts, move.getInsertionPositions());
	}

	@Test
	public void testGetAffectedResources() {
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");

		final MoveSnake<Integer> move = new MoveSnake<Integer>();

		move.setFromResources(CollectionsUtil.makeArrayList(resource1));

		move.setToResources(CollectionsUtil.makeArrayList(resource2));

		Collection<IResource> affectedResources = move.getAffectedResources();

		Assert.assertNotNull(affectedResources);
		Assert.assertEquals(2, affectedResources.size());
		Assert.assertTrue(affectedResources.contains(resource1));
		Assert.assertTrue(affectedResources.contains(resource2));
	}
}
