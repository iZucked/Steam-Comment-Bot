package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;

public class MoveHandlerHelperTest {

	@Test
	public void testCargoExtractionFromCargoSequence() {
		final ISequenceElement elementResourceAStart = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementA = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementB = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementC = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementD = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementE = Mockito.mock(ISequenceElement.class);
		final ISequenceElement elementF = Mockito.mock(ISequenceElement.class);

		final ISequenceElement elementResourceAEnd = Mockito.mock(ISequenceElement.class);

		final IResource resourceA = Mockito.mock(IResource.class);

		final ListModifiableSequence sequenceA = new ListModifiableSequence(Lists.newArrayList(elementResourceAStart, elementA, elementB, elementC, elementD, elementE, elementF, elementResourceAEnd));

		IGuidedMoveHelper helper = Mockito.mock(IGuidedMoveHelper.class);
		
		Mockito.when(helper.isLoadSlot(elementA)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isLoadSlot(elementC)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isLoadSlot(elementE)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isFOBPurchase(elementA)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isFOBPurchase(elementC)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isFOBPurchase(elementE)).thenReturn(Boolean.TRUE);

		Mockito.when(helper.isDischargeSlot(elementB)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isDischargeSlot(elementD)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isDischargeSlot(elementF)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isDESSale(elementB)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isDESSale(elementD)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isDESSale(elementF)).thenReturn(Boolean.TRUE);

		Mockito.when(helper.isStartOrEndSlot(elementResourceAStart)).thenReturn(Boolean.TRUE);
		Mockito.when(helper.isStartOrEndSlot(elementResourceAEnd)).thenReturn(Boolean.TRUE);
		MoveHandlerHelper moveHandlerHelper = createInstance(helper);
		
		// Check sequence extracts
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementA);
			Assert.assertSame(elementA, segment.get(0));
			Assert.assertSame(elementB, segment.get(1));
		}
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementB);
			Assert.assertSame(elementA, segment.get(0));
			Assert.assertSame(elementB, segment.get(1));
		}
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementC);
			Assert.assertSame(elementC, segment.get(0));
			Assert.assertSame(elementD, segment.get(1));
		}
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementD);
			Assert.assertSame(elementC, segment.get(0));
			Assert.assertSame(elementD, segment.get(1));
		}
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementE);
			Assert.assertSame(elementE, segment.get(0));
			Assert.assertSame(elementF, segment.get(1));
		}
		{
			List<ISequenceElement> segment = moveHandlerHelper.extractSegment(sequenceA, elementF);
			Assert.assertSame(elementE, segment.get(0));
			Assert.assertSame(elementF, segment.get(1));
		}
	}

	private MoveHandlerHelper createInstance(final @NonNull IGuidedMoveHelper helper) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IGuidedMoveHelper.class).toInstance(helper);
			}
		});
		return injector.getInstance(MoveHandlerHelper.class);
	}
}
