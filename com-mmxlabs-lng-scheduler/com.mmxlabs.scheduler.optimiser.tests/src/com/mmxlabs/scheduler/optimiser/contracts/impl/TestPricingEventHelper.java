/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class TestPricingEventHelper {

	@Test
	public void testLoadSlotTimes() {

		final ILoadOption loadOption = mock(ILoadOption.class);
		final IDischargeOption dischargeOption = mock(IDischargeOption.class);
		final IPortTimesRecord portTimesRecord = mock(IPortTimesRecord.class);

		final ITimeZoneToUtcOffsetProvider tzProvider = mock(ITimeZoneToUtcOffsetProvider.class);
		// TZ Shift, +1 for load, +2 for discharge
		when(tzProvider.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPortSlot p = (IPortSlot) args[1];
				if (p == loadOption) {
					return (Integer) args[0] + 1;
				} else {
					return (Integer) args[0] + 2;

				}
			}
		});

		final PricingEventHelper pricingEventHelper = createInstance(tzProvider);

		when(portTimesRecord.getSlots()).thenReturn(Lists.newArrayList(loadOption, dischargeOption));

		when(portTimesRecord.getSlotTime(loadOption)).thenReturn(10);
		when(portTimesRecord.getSlotTime(dischargeOption)).thenReturn(20);

		when(portTimesRecord.getSlotDuration(loadOption)).thenReturn(5);
		when(portTimesRecord.getSlotDuration(dischargeOption)).thenReturn(5);

		when(loadOption.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);

		when(loadOption.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		Assert.assertEquals(10 + 1, pricingEventHelper.getLoadPricingDate(loadOption, dischargeOption, portTimesRecord));

		when(loadOption.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		Assert.assertEquals(15 + 1, pricingEventHelper.getLoadPricingDate(loadOption, dischargeOption, portTimesRecord));

		when(loadOption.getPricingEvent()).thenReturn(PricingEventType.START_OF_DISCHARGE);
		Assert.assertEquals(20 + 2, pricingEventHelper.getLoadPricingDate(loadOption, dischargeOption, portTimesRecord));

		when(loadOption.getPricingEvent()).thenReturn(PricingEventType.END_OF_DISCHARGE);
		Assert.assertEquals(25 + 2, pricingEventHelper.getLoadPricingDate(loadOption, dischargeOption, portTimesRecord));

		when(loadOption.getPricingDate()).thenReturn(30);
		Assert.assertEquals(30 + 1, pricingEventHelper.getLoadPricingDate(loadOption, dischargeOption, portTimesRecord));
	}

	@Test
	public void testDischargeSlotTimes() {

		final ILoadOption loadOption = mock(ILoadOption.class);
		final IDischargeOption dischargeOption = mock(IDischargeOption.class);
		final IPortTimesRecord portTimesRecord = mock(IPortTimesRecord.class);

		// TZ Shift, +1 for load, +2 for discharge
		final ITimeZoneToUtcOffsetProvider tzProvider = mock(ITimeZoneToUtcOffsetProvider.class);
		when(tzProvider.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPortSlot p = (IPortSlot) args[1];
				if (p == loadOption) {
					return (Integer) args[0] + 1;
				} else {
					return (Integer) args[0] + 2;

				}
			}
		});

		final PricingEventHelper pricingEventHelper = createInstance(tzProvider);
		when(portTimesRecord.getSlots()).thenReturn(Lists.newArrayList(loadOption, dischargeOption));

		when(portTimesRecord.getSlotTime(loadOption)).thenReturn(10);
		when(portTimesRecord.getSlotTime(dischargeOption)).thenReturn(20);

		when(portTimesRecord.getSlotDuration(loadOption)).thenReturn(5);
		when(portTimesRecord.getSlotDuration(dischargeOption)).thenReturn(5);

		when(dischargeOption.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);

		when(dischargeOption.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		Assert.assertEquals(10 + 1, pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord));

		when(dischargeOption.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		Assert.assertEquals(15 + 1, pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord));

		when(dischargeOption.getPricingEvent()).thenReturn(PricingEventType.START_OF_DISCHARGE);
		Assert.assertEquals(20 + 2, pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord));

		when(dischargeOption.getPricingEvent()).thenReturn(PricingEventType.END_OF_DISCHARGE);
		Assert.assertEquals(25 + 2, pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord));

		when(dischargeOption.getPricingDate()).thenReturn(30);
		Assert.assertEquals(30 + 2, pricingEventHelper.getDischargePricingDate(dischargeOption, portTimesRecord));
	}

	private PricingEventHelper createInstance(final ITimeZoneToUtcOffsetProvider tzProvider) {

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(tzProvider);
			}
		});
		return injector.getInstance(PricingEventHelper.class);
	}
}
