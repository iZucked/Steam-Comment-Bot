/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;


public class PriceIntervalProviderHelperLowLevelTest {

	@Test
	public void testIsFeasible_1() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		Assert.assertTrue(helper.isFeasibleTravelTime(new IntervalData(0, 10, 0), new IntervalData(20, 100, 0), 0, 30));
	}
	
	@Test
	public void testIsFeasible_2() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		Assert.assertTrue(helper.isFeasibleTravelTime(new IntervalData(0, 10, 0), new IntervalData(20, 100, 0), 0, 5));
	}

	@Test
	public void testIsFeasible_3() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		Assert.assertTrue(helper.isFeasibleTravelTime(new IntervalData(0, 10, 0), new IntervalData(20, 100, 0), 0, 50));
	}

	@Test
	public void testIsFeasible_4() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		Assert.assertTrue(helper.isFeasibleTravelTime(new IntervalData(0, 10, 0), new IntervalData(20, 100, 0), 0, 100));
	}

	@Test
	public void testIsFeasible_5() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		Assert.assertFalse(helper.isFeasibleTravelTime(new IntervalData(0, 10, 0), new IntervalData(20, 100, 0), 0, 101));
	}
	
	@Test
	public void testIdealLoadAndDischargeTimes_LoadLate() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(0, 20, 50, 80, 5, 5, 10);
		Assert.assertArrayEquals(new int[]{20, 50}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_LoadEarlier() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(0, 20, 50, 80, 5, 30, 40);
		Assert.assertArrayEquals(new int[]{5, 50}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_DischargeLater() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(20, 20, 50, 80, 5, 30, 40);
		Assert.assertArrayEquals(new int[]{20, 65}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_LoadEarlierDischargeLater() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(15, 20, 50, 80, 5, 30, 40);
		Assert.assertArrayEquals(new int[]{15, 60}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_MaxInterval() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(10, 20, 50, 80, 5, 60, 90);
		Assert.assertArrayEquals(new int[]{10, 80}, times);
	}
	
	@Test
	public void testIdealLoadAndDischargeTimes_AlmostMaxSpeed() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(10, 20, 50, 80, 5, 60, 60);
		Assert.assertArrayEquals(new int[]{10, 75}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_MaxSpeed1() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(10, 20, 75, 80, 5, 60, 60);
		Assert.assertArrayEquals(new int[]{10, 75}, times);
	}

	@Test
	public void testIdealLoadAndDischargeTimes_MaxSpeed2() {
		PriceIntervalProviderHelper helper = createPriceIntervalProviderHelper(0);
		int[] times = helper.getIdealLoadAndDischargeTimesGivenCanal(10, 20, 85, 100, 5, 60, 60);
		Assert.assertArrayEquals(new int[]{20, 85}, times);
	}

	private PriceIntervalProviderHelper createPriceIntervalProviderHelper(final int timeDiff) {
		PriceIntervalProviderHelper ppih = new PriceIntervalProviderHelper();
		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]+timeDiff;
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]-timeDiff;
				return input;
			}
		});
		when(t.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]-2;
				return input;
			}
		});
		
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(IVesselProvider.class).to(HashMapVesselEditor.class);
			}
		});

		injector.injectMembers(ppih);
		return ppih;
	}

}
