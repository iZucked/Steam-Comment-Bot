/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

public class Move2over2Test {

	@Test
	public void testValidate() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final IModifiableSequence sequence1 = new ListModifiableSequence(CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(1), new IntegerElement(2), new IntegerElement(3),
				new IntegerElement(4), new IntegerElement(5)));
		final IModifiableSequence sequence2 = new ListModifiableSequence(CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(6), new IntegerElement(7), new IntegerElement(8),
				new IntegerElement(9), new IntegerElement(10), new IntegerElement(11)));

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final Move2over2 move = new Move2over2();

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
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final IModifiableSequence sequence1 = new ListModifiableSequence(CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(1), new IntegerElement(2), new IntegerElement(3),
				new IntegerElement(4), new IntegerElement(5)));
		final IModifiableSequence sequence2 = new ListModifiableSequence(CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(6), new IntegerElement(7), new IntegerElement(8),
				new IntegerElement(9), new IntegerElement(10), new IntegerElement(11)));

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final Move2over2 move = new Move2over2();

		final int resource1Position = 3;
		final int resource2Position = 4;

		move.setPreserveStartAndEnd(true);
		move.setResource1(resource1);
		move.setResource2(resource2);
		move.setResource1Position(resource1Position);
		move.setResource2Position(resource2Position);

		move.apply(sequences);

		final List<ISequenceElement> expectedSequence1 = CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(1), new IntegerElement(2), new IntegerElement(3),
				new IntegerElement(10), new IntegerElement(5));

		final List<ISequenceElement> expectedSequence2 = CollectionsUtil.makeArrayList2(ISequenceElement.class, new IntegerElement(6), new IntegerElement(7), new IntegerElement(8),
				new IntegerElement(9), new IntegerElement(4), new IntegerElement(11));

		Assert.assertEquals(expectedSequence1.size(), sequence1.size());
		Assert.assertEquals(expectedSequence2.size(), sequence2.size());

		for (int i = 0; i < expectedSequence1.size(); i++) {
			Assert.assertEquals(expectedSequence1.get(i), sequence1.get(i));
		}

		for (int i = 0; i < expectedSequence2.size(); i++) {
			Assert.assertEquals(expectedSequence2.get(i), sequence2.get(i));
		}
	}
}
