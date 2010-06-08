package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
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
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.Sequences;

@RunWith(JMock.class)
public class Move3over2Test {

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

		final Move3over2<Object> move = new Move3over2<Object>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;

		move.setResource1(resource1);
		move.setResource2(resource2);

		move.setResource1Start(resource1Start);
		move.setResource1End(resource1End);

		move.setResource2Position(resource2Start);

		context.checking(new Expectations() {
			{
				// Expect these methods to be invoked once
				oneOf(sequence1).getSegment(resource1Start, resource1End);

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

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move3over2<Integer> move = new Move3over2<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;

		move.setResource1(resource1);
		move.setResource2(resource2);

		move.setResource1Start(resource1Start);
		move.setResource1End(resource1End);

		move.setResource2Position(resource2Start);

		move.apply(sequences);

		final List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(
				3, 4, 5);
		final List<Integer> expectedSequence2 = CollectionsUtil.makeArrayList(
				6, 1, 2, 7, 8, 9, 10);

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		for (int i = 0; i < expectedSequence1.size(); ++i) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}

		Assert.assertEquals(expectedSequence2.size(), sequence2.size());
		for (int i = 0; i < expectedSequence2.size(); ++i) {
			Assert.assertEquals(expectedSequence2.get(i), sequence2.get(i));
		}
	}

	@Test
	public void testValidate() {
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10));

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move3over2<Integer> move = new Move3over2<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;

		Assert.assertFalse(move.validate(sequences));

		move.setResource1(resource1);
		Assert.assertFalse(move.validate(sequences));

		move.setResource2(resource2);
		Assert.assertFalse(move.validate(sequences));

		move.setResource1Start(resource1Start);
		Assert.assertFalse(move.validate(sequences));

		move.setResource1End(resource1End);
		Assert.assertFalse(move.validate(sequences));

		move.setResource2Position(resource2Start);

		final Map<IResource, ISequence<Integer>> sequenceMap_r1 = CollectionsUtil
				.makeHashMap(resource1, sequence1);
		final Map<IResource, ISequence<Integer>> sequenceMap_r2 = CollectionsUtil
				.makeHashMap(resource2, sequence2);

		Assert.assertFalse(move.validate(new Sequences<Integer>(Collections
				.singletonList(resource2), sequenceMap_r2)));
		Assert.assertFalse(move.validate(new Sequences<Integer>(Collections
				.singletonList(resource1), sequenceMap_r1)));

		Assert.assertTrue(move.validate(sequences));

		move.setResource1End(100);
		Assert.assertFalse(move.validate(sequences));
		move.setResource1End(resource1End);

		move.setResource1End(-1);
		Assert.assertFalse(move.validate(sequences));
		move.setResource1End(resource1End);
		
		move.setResource1Start(100);
		Assert.assertFalse(move.validate(sequences));
		move.setResource1Start(resource1Start);

		move.setResource1Start(-1);
		Assert.assertFalse(move.validate(sequences));
		move.setResource1Start(resource1Start);

		move.setResource2Position(100);
		Assert.assertFalse(move.validate(sequences));
		move.setResource2Position(resource2Start);
		
		move.setResource2Position(-1);
		Assert.assertFalse(move.validate(sequences));
		move.setResource2Position(resource2Start);
		
	}

	@Test
	public void testGetSetResource1() {

		final Move3over2<Object> move = new Move3over2<Object>();
		final IResource resource1 = context.mock(IResource.class);
		move.setResource1(resource1);
		Assert.assertSame(resource1, move.getResource1());
	}

	@Test
	public void testGetSetResource2() {

		final Move3over2<Object> move = new Move3over2<Object>();
		final IResource resource2 = context.mock(IResource.class);
		move.setResource2(resource2);
		Assert.assertSame(resource2, move.getResource2());
	}

	@Test
	public void testGetSetResource1Start() {

		final Move3over2<Object> move = new Move3over2<Object>();
		Assert.assertEquals(-1, move.getResource1Start());
		final int pos = 10;
		move.setResource1Start(pos);
		Assert.assertEquals(pos, move.getResource1Start());
	}

	@Test
	public void testGetSetResource1End() {

		final Move3over2<Object> move = new Move3over2<Object>();
		Assert.assertEquals(-1, move.getResource1End());
		final int pos = 10;
		move.setResource1End(pos);
		Assert.assertEquals(pos, move.getResource1End());
	}

	@Test
	public void testGetSetResource2Position() {

		final Move3over2<Object> move = new Move3over2<Object>();
		Assert.assertEquals(-1, move.getResource2Position());
		final int pos = 10;
		move.setResource2Position(pos);
		Assert.assertEquals(pos, move.getResource2Position());
	}
}
