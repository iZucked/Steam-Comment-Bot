package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequence;

public class IndexFinderTest {

	@Test
	public void testIndexFinder() {

		final ISequence sequenceA = Mockito.mock(ISequence.class);

		Assert.assertEquals(1, new IndexFinder(1).findInsertionIndex(sequenceA));
	}

}
