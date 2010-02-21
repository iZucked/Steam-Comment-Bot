package com.acme.optimiser.lso.impl;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.IModifiableSequences;
import com.acme.optimiser.IResource;
import com.acme.optimiser.impl.ListModifiableSequence;
import com.acme.optimiser.impl.ModifiableSequences;
import com.acme.optimiser.lso.impl.internal.MoveUtil;

@RunWith(JMock.class)
public class Move4opt2Test {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testApply() {

		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");

		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = MoveUtil.makeArrayList(resource1,
				resource2);

		final Map<IResource, IModifiableSequence<Object>> map = MoveUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		final Move4opt2<Object> move = new Move4opt2<Object>();

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
				MoveUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				MoveUtil.makeArrayList(6, 7, 8, 9, 10));

		List<IResource> resources = MoveUtil
				.makeArrayList(resource1, resource2);

		Map<IResource, IModifiableSequence<Integer>> sequenceMap = MoveUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move4opt2<Integer> move = new Move4opt2<Integer>();

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


		List<Integer> expectedSequence1 = MoveUtil.makeArrayList( 7, 8, 3, 4, 5);
		List<Integer> expectedSequence2 = MoveUtil
				.makeArrayList(6,1, 2, 9, 10);

		Assert.fail("Not complete - check output sequences");
	}
}
