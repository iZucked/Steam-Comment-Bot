package com.mmxlabs.scheduler.optimiser.schedule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

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

		final ISequenceElement element1 = mock(ISequenceElement.class);
		final ISequenceElement element2 = mock(ISequenceElement.class);
		final ISequenceElement element3 = mock(ISequenceElement.class);
		final ISequenceElement element4 = mock(ISequenceElement.class);
		final ISequenceElement element5 = mock(ISequenceElement.class);

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

		when(portSlotProvider.getElement(portSlot1)).thenReturn(element1);
		when(portSlotProvider.getElement(portSlot2)).thenReturn(element2);
		when(portSlotProvider.getElement(portSlot3)).thenReturn(element3);
		when(portSlotProvider.getElement(portSlot4)).thenReturn(element4);
		when(portSlotProvider.getElement(portSlot5)).thenReturn(element5);

		final ITimeWindow timeWindow = mock(ITimeWindow.class);
		when(timeWindow.getStart()).thenReturn(0);
		when(timeWindow.getEnd()).thenReturn(0);

		when(portSlot1.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot2.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot3.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot4.getTimeWindow()).thenReturn(timeWindow);
		when(portSlot5.getTimeWindow()).thenReturn(timeWindow);

		final ScheduleCalculator scheduleCalculator = new ScheduleCalculator();
		injector.injectMembers(scheduleCalculator);

		scheduleCalculator.calculateMarkToMarketPNL(sequences, annotatedSolution);

	}

}
