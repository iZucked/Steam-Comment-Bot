/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.impl;

import java.util.List;
import java.util.Map;

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
public class Move2over2Test {
	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testValidate() {
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");
		
		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10, 11));
		
		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);
		
		final Move2over2<Integer> move = new Move2over2<Integer>();
		
		final int resource1Position = 3;
		final int resource2Position = 4;
		
		move.setPreserveStartAndEnd(true);
		move.setResource1(resource1);
		move.setResource2(resource2);
		move.setResource1Position(resource1Position);
		move.setResource2Position(resource2Position);
		
		Assert.assertTrue(move.validate(sequences));
		
		move.setResource1Position(0);
		Assert.assertFalse(move.validate(sequences));
		
		move.setResource1Position(4);
		Assert.assertTrue(move.validate(sequences));
	}
	
	@Test
	public void testApplyOnData() {
		final IResource resource1 = context.mock(IResource.class, "resource1");
		final IResource resource2 = context.mock(IResource.class, "resource2");
		
		final IModifiableSequence<Integer> sequence1 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(1, 2, 3, 4, 5));
		final IModifiableSequence<Integer> sequence2 = new ListModifiableSequence<Integer>(
				CollectionsUtil.makeArrayList(6, 7, 8, 9, 10, 11));
		
		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Integer>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				resources, sequenceMap);
		
		final Move2over2<Integer> move = new Move2over2<Integer>();
		
		final int resource1Position = 3;
		final int resource2Position = 4;
		
		move.setPreserveStartAndEnd(true);
		move.setResource1(resource1);
		move.setResource2(resource2);
		move.setResource1Position(resource1Position);
		move.setResource2Position(resource2Position);
		
		move.apply(sequences);
		
		final List<Integer> expectedSequence1 = CollectionsUtil.makeArrayList(
				1,2,3,10,5);
		final List<Integer> expectedSequence2 = CollectionsUtil.makeArrayList(
				6,7,8,9,4,11);		
		
		
		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		Assert.assertEquals(expectedSequence2.size(), sequence2.size());
		
		for (int i = 0; i<expectedSequence1.size(); i++) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}
		
		for (int i = 0; i<expectedSequence2.size(); i++) {
			Assert.assertEquals(expectedSequence2.get(i), sequence2.get(i));
		}
	}
}
