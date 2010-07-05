package com.mmxlabs.optimiser.lso.impl;

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
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.impl.Sequences;

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

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
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

		final List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(5, 6,
				3, 4, 1, 2, 7, 8, 9, 10);

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		for (int i = 0; i < expectedSequence1.size(); ++i) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}
	}

	@Test
	public void testValidate() {
		final IResource resource1 = context.mock(IResource.class, "resource1");

		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);

		final Move4over1<Integer> move = new Move4over1<Integer>();

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 4;
		final int resource2End = 6;

		Assert.assertFalse(move.validate(sequences));

		move.setResource(resource1);
		Assert.assertFalse(move.validate(sequences));

		move.setSegment1Start(resource1Start);
		Assert.assertFalse(move.validate(sequences));

		move.setSegment1End(resource1End);
		Assert.assertFalse(move.validate(sequences));

		move.setSegment2Start(resource2Start);
		Assert.assertFalse(move.validate(sequences));

		move.setSegment2End(resource2End);
		Assert.assertTrue(move.validate(sequences));

		final Map<IResource, ISequence<Integer>> sequenceMap_r1 = Collections.emptyMap();

		List<IResource> r1 = Collections.emptyList();
		
		Assert.assertFalse(move.validate(new Sequences<Integer>(r1, sequenceMap_r1)));


		// NOTE: This test does not trigger the desired code path. Check cobertura reports
		move.setSegment1End(100);
		Assert.assertFalse(move.validate(sequences));
		move.setSegment1End(resource1End);

		move.setSegment1Start(100);
		Assert.assertFalse(move.validate(sequences));
		move.setSegment1Start(resource1Start);

		
		move.setSegment2End(100);
		Assert.assertFalse(move.validate(sequences));
		move.setSegment2End(resource2End);

		move.setSegment2Start(100);
		Assert.assertFalse(move.validate(sequences));
		move.setSegment2Start(resource2Start);

	}

	@Test
	public void testGetSetResource() {

		final Move4over1<Integer> move = new Move4over1<Integer>();

		final IResource resource1 = context.mock(IResource.class, "resource1");

		Assert.assertNull(move.getResource());
		move.setResource(resource1);
		Assert.assertSame(resource1, move.getResource());
	}

	@Test
	public void testGetSetSegment1Start() {

		final Move4over1<Integer> move = new Move4over1<Integer>();

		Assert.assertEquals(-1, move.getSegment1Start());
		final int pos = 10;
		move.setSegment1Start(pos);
		Assert.assertEquals(pos, move.getSegment1Start());
	}
	
	@Test
	public void testGetSetSegment1End() {

		final Move4over1<Integer> move = new Move4over1<Integer>();

		Assert.assertEquals(-1, move.getSegment1End());
		final int pos = 10;
		move.setSegment1End(pos);
		Assert.assertEquals(pos, move.getSegment1End());
	}
	

	@Test
	public void testGetSetSegment2Start() {

		final Move4over1<Integer> move = new Move4over1<Integer>();

		Assert.assertEquals(-1, move.getSegment2Start());
		final int pos = 10;
		move.setSegment2Start(pos);
		Assert.assertEquals(pos, move.getSegment2Start());
	}
	
	@Test
	public void testGetSetSegment2End() {

		final Move4over1<Integer> move = new Move4over1<Integer>();

		Assert.assertEquals(-1, move.getSegment2End());
		final int pos = 10;
		move.setSegment2End(pos);
		Assert.assertEquals(pos, move.getSegment2End());
	}
}
