package com.mmxlabs.optimiser.lso.impl;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.fail;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;

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

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1,
				resource2, resource3);

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

		final List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(13, 14,
				3, 4, 5);
		final List<Integer> expectedSequence2 = CollectionsUtil.makeArrayList(6, 1,
				2, 9, 10);
		final List<Integer> expectedSequence3 = CollectionsUtil.makeArrayList(11, 12,
				7, 8, 15);

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
		fail("Not yet implemented");
	}

}
