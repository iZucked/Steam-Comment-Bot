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
public class Move4over2Test {

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

		final Move4over2<Object> move = new Move4over2<Object>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		move.setResource1(resource1);
		move.setResource2(resource2);

		move.setResource1Start(resource1Start);
		move.setResource1End(resource1End);

		move.setResource2Start(resource2Start);
		move.setResource2End(resource2End);

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

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10));

		List<IResource> resources = CollectionsUtil.makeArrayList(resource1,
				resource2);

		Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move4over2<Integer> move = new Move4over2<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		move.setResource1(resource1);
		move.setResource2(resource2);

		move.setResource1Start(resource1Start);
		move.setResource1End(resource1End);

		move.setResource2Start(resource2Start);
		move.setResource2End(resource2End);

		move.apply(sequences);

		List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(7, 8,
				3, 4, 5);
		List<Integer> expectedSequence2 = CollectionsUtil.makeArrayList(6, 1,
				2, 9, 10);

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		for (int i = 0; i < expectedSequence1.size(); ++i) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}

		Assert.assertEquals(expectedSequence2.size(), sequence2.size());
		for (int i = 0; i < expectedSequence2.size(); ++i) {
			Assert.assertEquals(expectedSequence2.get(i), sequence2.get(i));
		}
	}
}
