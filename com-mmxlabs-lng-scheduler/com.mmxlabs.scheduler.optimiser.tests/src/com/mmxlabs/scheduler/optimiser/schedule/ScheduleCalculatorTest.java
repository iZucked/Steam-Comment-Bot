package com.mmxlabs.scheduler.optimiser.schedule;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Matchers;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class ScheduleCalculatorTest {

	@Test
	public void calculateMarkToMarketPNLTest() {

		final IMarkToMarketProvider markToMarketProvider = mock(IMarkToMarketProvider.class);
		final IPortSlotProvider portSlotProvider = mock(IPortSlotProvider.class);
		final IVolumeAllocator volumeAllocator = mock(IVolumeAllocator.class);
		final IEntityValueCalculator entityValueCalculator = mock(IEntityValueCalculator.class);
		final ICalculatorProvider calculatorProvider = mock(ICalculatorProvider.class);
		final IVesselProvider vesselProvider = mock(IVesselProvider.class);
		final IPortCostProvider portCostProvider = mock(IPortCostProvider.class);

		class TestModule extends AbstractModule {
			@Override
			protected void configure() {
				bind(IMarkToMarketProvider.class).toInstance(markToMarketProvider);
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
				bind(IVolumeAllocator.class).toInstance(volumeAllocator);
				bind(IEntityValueCalculator.class).toInstance(entityValueCalculator);
				bind(ICalculatorProvider.class).toInstance(calculatorProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IPortCostProvider.class).toInstance(portCostProvider);
			}
		}
		final Injector injector = Guice.createInjector(new TestModule());

		final ISequences sequences = mock(ISequences.class);

		final IAnnotatedSolution annotatedSolution = mock(IAnnotatedSolution.class);
		final IAnnotations annotations = mock(IAnnotations.class);
		when(annotatedSolution.getElementAnnotations()).thenReturn(annotations);

		final ISequenceElement element1 = mock(ISequenceElement.class, "element1");
		final ISequenceElement element2 = mock(ISequenceElement.class, "element2");
		final ISequenceElement element3 = mock(ISequenceElement.class, "element3");
		final ISequenceElement element4 = mock(ISequenceElement.class, "element4");
		final ISequenceElement element5 = mock(ISequenceElement.class, "element5");

		when(element1.getName()).thenReturn("element1");
		when(element2.getName()).thenReturn("element2");
		when(element3.getName()).thenReturn("element3");
		when(element4.getName()).thenReturn("element4");
		when(element5.getName()).thenReturn("element5");

		final List<ISequenceElement> unusedElements = Lists.newArrayList(element1, element2, element3, element4, element5);
		when(sequences.getUnusedElements()).thenReturn(unusedElements);

		final ILoadOption portSlot1 = mock(ILoadOption.class);
		final ILoadSlot portSlot2 = mock(ILoadSlot.class);
		final IDischargeOption portSlot3 = mock(IDischargeOption.class);
		final IDischargeSlot portSlot4 = mock(IDischargeSlot.class);
		final IPortSlot portSlot5 = mock(IPortSlot.class);

		when(portSlotProvider.getPortSlot(element1)).thenReturn(portSlot1);
		when(portSlotProvider.getPortSlot(element2)).thenReturn(portSlot2);
		when(portSlotProvider.getPortSlot(element3)).thenReturn(portSlot3);
		when(portSlotProvider.getPortSlot(element4)).thenReturn(portSlot4);
		when(portSlotProvider.getPortSlot(element5)).thenReturn(portSlot5);

		final int windowStart = 10;
		final int windowEnd = 20;
		final ITimeWindow timeWindow = mock(ITimeWindow.class);
		when(timeWindow.getStart()).thenReturn(windowStart);
		when(timeWindow.getEnd()).thenReturn(windowEnd);

		when(portSlot1.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot2.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot3.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot4.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot5.getTimeWindow()).thenReturn(timeWindow);

		final ScheduleCalculator scheduleCalculator = new ScheduleCalculator();
		injector.injectMembers(scheduleCalculator);

		final IMarkToMarket market1 = mock(IMarkToMarket.class);
		when(markToMarketProvider.getMarketForElement(element1)).thenReturn(market1);

		final IMarkToMarket market2 = mock(IMarkToMarket.class);
		when(markToMarketProvider.getMarketForElement(element2)).thenReturn(market2);

		final IMarkToMarket market3 = mock(IMarkToMarket.class);
		when(markToMarketProvider.getMarketForElement(element3)).thenReturn(market3);

		final IMarkToMarket market4 = mock(IMarkToMarket.class);
		when(markToMarketProvider.getMarketForElement(element4)).thenReturn(market4);

		final IMarkToMarket market5 = mock(IMarkToMarket.class);
		when(markToMarketProvider.getMarketForElement(element5)).thenReturn(market5);

		final IAllocationAnnotation allocationAnnotation = mock(IAllocationAnnotation.class);
		when(volumeAllocator.allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot1)), anyListOf(Integer.class))).thenReturn(allocationAnnotation);
		when(volumeAllocator.allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot2)), anyListOf(Integer.class))).thenReturn(allocationAnnotation);
		when(volumeAllocator.allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot3)), anyListOf(Integer.class))).thenReturn(allocationAnnotation);
		when(volumeAllocator.allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot4)), anyListOf(Integer.class))).thenReturn(allocationAnnotation);

		scheduleCalculator.calculateMarkToMarketPNL(sequences, annotatedSolution);

		List<Integer> expectedList = Lists.newArrayList(Integer.valueOf(10), Integer.valueOf(10));

		// Verify that our slots were correctly matched against MTM slots
		verify(volumeAllocator, times(1)).allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot1)), eq(expectedList));
		verify(volumeAllocator, times(1)).allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot2)), eq(expectedList));
		verify(volumeAllocator, times(1)).allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot3)), eq(expectedList));
		verify(volumeAllocator, times(1)).allocate(Matchers.<IVessel> any(), argThat(new VoyagePlanMatcher(portSlot4)), eq(expectedList));

		verify(annotations, times(1)).setAnnotation(eq(element1), eq(SchedulerConstants.AI_volumeAllocationInfo), anyObject());
		verify(annotations, times(1)).setAnnotation(eq(element2), eq(SchedulerConstants.AI_volumeAllocationInfo), anyObject());
		verify(annotations, times(1)).setAnnotation(eq(element3), eq(SchedulerConstants.AI_volumeAllocationInfo), anyObject());
		verify(annotations, times(1)).setAnnotation(eq(element4), eq(SchedulerConstants.AI_volumeAllocationInfo), anyObject());
		verify(annotations, never()).setAnnotation(eq(element5), eq(SchedulerConstants.AI_volumeAllocationInfo), anyObject());
	}

	/**
	 * Matcher implementation to check that the real and generated MTM slots are as expected in the VoyagePlan.
	 * 
	 */
	class VoyagePlanMatcher extends org.hamcrest.BaseMatcher<VoyagePlan> {

		private final IPortSlot slot;

		VoyagePlanMatcher(final IPortSlot slot) {
			this.slot = slot;
		}

		@Override
		public boolean matches(final Object item) {

			if (item instanceof VoyagePlan) {
				final VoyagePlan voyagePlan = (VoyagePlan) item;
				final Object[] sequence = voyagePlan.getSequence();
				if (sequence != null) {
					if (sequence.length == 2) {
						final PortDetails details0 = (PortDetails) sequence[0];
						final PortDetails details1 = (PortDetails) sequence[1];

						if (slot instanceof ILoadSlot) {
							if (details0.getOptions().getPortSlot() != slot) {
								return false;
							}
							if (details1.getOptions().getPortSlot() instanceof MarkToMarketDischargeOption) {
								return true;
							}
						}

						else if (slot instanceof ILoadOption) {
							if (details0.getOptions().getPortSlot() != slot) {
								return false;
							}
							if (details1.getOptions().getPortSlot() instanceof MarkToMarketDischargeSlot) {
								return true;
							}
						}

						else if (slot instanceof IDischargeSlot) {
							if (details1.getOptions().getPortSlot() != slot) {
								return false;
							}
							if (details0.getOptions().getPortSlot() instanceof MarkToMarketLoadOption) {
								return true;
							}
						}

						else if (slot instanceof IDischargeOption) {
							if (details1.getOptions().getPortSlot() != slot) {
								return false;
							}
							if (details0.getOptions().getPortSlot() instanceof MarkToMarketLoadSlot) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		@Override
		public void describeTo(final Description description) {
		}

	}
}
