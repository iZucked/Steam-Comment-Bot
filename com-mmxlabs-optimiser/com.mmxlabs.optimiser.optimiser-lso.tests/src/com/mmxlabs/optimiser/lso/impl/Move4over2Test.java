/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;

@SuppressWarnings("null")
public class Move4over2Test {

	@Test
	public void testApply() {

		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");

		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final Move4over2 move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, resource2End);

		move.apply(sequences);

		Mockito.verify(sequence1).getSegment(resource1Start, resource1End);
		Mockito.verify(sequence2).getSegment(resource2Start, resource2End);
	}

	@Test
	public void testApplyOnData() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");

		final IModifiableSequence sequence1 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence sequence2 = OptimiserTestUtil.makeSequence(6, 7, 8, 9, 10);

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;

		final Move4over2 move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, resource2End);

		move.apply(sequences);

		final List<ISequenceElement> expectedSequence1 = OptimiserTestUtil.makeList(7, 8, 3, 4, 5);
		final List<ISequenceElement> expectedSequence2 = OptimiserTestUtil.makeList(6, 1, 2, 9, 10);

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
		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");
		final IResource resource3 = Mockito.mock(IResource.class, "resource3");

		final IModifiableSequence sequence1 = OptimiserTestUtil.makeSequence(1, 2, 3, 4, 5);
		final IModifiableSequence sequence2 = OptimiserTestUtil.makeSequence(6, 7, 8, 9, 10);

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> sequenceMap = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final IModifiableSequences sequences = new ModifiableSequences(resources, sequenceMap);

		final int resource1Start = 0;
		final int resource1End = 2;

		final int resource2Start = 1;
		final int resource2End = 3;
		Move4over2 move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, resource2End);

		Assert.assertTrue(move.validate(sequences));

		final Map<IResource, ISequence> sequenceMap_r1 = CollectionsUtil.makeHashMap(resource1, sequence1);
		final Map<IResource, ISequence> sequenceMap_r2 = CollectionsUtil.makeHashMap(resource2, sequence2);

		Assert.assertFalse(move.validate(new Sequences(Collections.singletonList(resource2), sequenceMap_r2)));
		Assert.assertFalse(move.validate(new Sequences(Collections.singletonList(resource1), sequenceMap_r1)));

		move = new Move4over2(resource1, resource1Start, 100, resource2, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, 100, resource1End, resource2, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, 100);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, resource1End, resource2, 100, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, -1, resource2, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, -1, resource1End, resource2, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, -1);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, resource1End, resource2, -1, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource3, resource1Start, resource1End, resource2, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));

		move = new Move4over2(resource1, resource1Start, resource1End, resource3, resource2Start, resource2End);
		Assert.assertFalse(move.validate(sequences));
	}

	@Test
	public void testGet() {

		final IResource resource1 = Mockito.mock(IResource.class, "resource1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource2");
		final int resource1Start = 1;
		final int resource1End = 2;

		final int resource2Start = 3;
		final int resource2End = 4;
		final Move4over2 move = new Move4over2(resource1, resource1Start, resource1End, resource2, resource2Start, resource2End);

		Assert.assertSame(resource1, move.getResource1());
		Assert.assertSame(resource2, move.getResource2());
		Assert.assertEquals(1, move.getResource1Start());
		Assert.assertEquals(2, move.getResource1End());
		Assert.assertEquals(3, move.getResource2Start());
		Assert.assertEquals(4, move.getResource2End());
	}
}