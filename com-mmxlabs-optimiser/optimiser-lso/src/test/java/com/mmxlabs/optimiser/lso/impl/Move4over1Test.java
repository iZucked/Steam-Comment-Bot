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

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;

@RunWith(JMock.class)
public class Move4over1Test {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings("unchecked")
	@Test
	public void testApply() {

		final IResource resource1 = context.mock(IResource.class, "resource1");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");

		final List<IResource> resources = CollectionsUtil
				.makeArrayList(resource1);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		final Move4over1<Object> move = new Move4over1<Object>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 3;
		final int resource2End = 5;

		move.setResource(resource1);

		move.setSegment1Start(resource1Start);
		move.setSegment1End(resource1End);

		move.setSegment2Start(resource2Start);
		move.setSegment2End(resource2End);

		context.checking(new Expectations() {
			{
				// Expect these methods to be invoked once
				oneOf(sequence1).getSegment(resource1Start, resource1End);
				oneOf(sequence1).getSegment(resource2Start, resource2End);

				// Allow any other method calls
				allowing(sequence1);
			}
		});

		move.apply(sequences);

		context.assertIsSatisfied();
	}

	@Test
	public void testApplyOnData() {
		final IResource resource1 = context.mock(IResource.class, "resource1");

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

		List<IResource> resources = CollectionsUtil.makeArrayList(resource1);

		Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move4over1<Integer> move = new Move4over1<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 4;
		final int resource2End = 6;

		move.setResource(resource1);

		move.setSegment1Start(resource1Start);
		move.setSegment1End(resource1End);

		move.setSegment2Start(resource2Start);
		move.setSegment2End(resource2End);

		move.apply(sequences);

		List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(5, 6,
				3, 4, 1, 2, 7, 8, 9, 10);

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		for (int i = 0; i < expectedSequence1.size(); ++i) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}
	}
}
