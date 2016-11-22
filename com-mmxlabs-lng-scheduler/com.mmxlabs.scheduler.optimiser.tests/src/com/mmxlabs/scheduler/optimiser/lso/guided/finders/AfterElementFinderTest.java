package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;

public class AfterElementFinderTest {

	@Test
	public void testAfterElementFinder() {

		final ISequenceElement elementResourceAStart = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementResourceAEnd = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementResourceAStart, elementA, elementB, elementC, elementD, elementResourceAEnd));

		Assert.assertEquals(1, new AfterElementFinder(elementResourceAStart).findInsertionIndex(sequenceA));
		Assert.assertEquals(2, new AfterElementFinder(elementA).findInsertionIndex(sequenceA));
		Assert.assertEquals(3, new AfterElementFinder(elementB).findInsertionIndex(sequenceA));
		Assert.assertEquals(4, new AfterElementFinder(elementC).findInsertionIndex(sequenceA));
		Assert.assertEquals(5, new AfterElementFinder(elementD).findInsertionIndex(sequenceA));
		Assert.assertEquals(6, new AfterElementFinder(elementResourceAEnd).findInsertionIndex(sequenceA));

		Assert.assertEquals(-1, new AfterElementFinder(elementE).findInsertionIndex(sequenceA));
	}

}
