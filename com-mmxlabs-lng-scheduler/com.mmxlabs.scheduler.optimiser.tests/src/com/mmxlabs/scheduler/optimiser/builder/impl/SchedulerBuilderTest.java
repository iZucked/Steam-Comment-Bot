/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class SchedulerBuilderTest {

	public static final boolean DEFAULT_VOLUME_LIMIT_IS_M3 = true;

	@Ignore
	@Test
	public void testSchedulerBuilder() {

		/**
		 * How to test builder? -- No access to state until we get the finished product? Perhaps a second constructor passing in DCP objects and ensure they are called correctly.
		 */

		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testCreateLoadSlot() {
		fail("Not yet implemented");
	}

	// @Test(expected = IllegalArgumentException.class)
	// public void testCreateLoadSlot2() {
	//
	// final SchedulerBuilder builder = createScheduleBuilder();
	//
	// final IPort port = Mockito.mock(IPort.class);
	// final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false);
	//
	// final ILoadPriceCalculator contract = Mockito.mock(ILoadPriceCalculator.class);
	//
	// builder.createLoadSlot("id", port, window, 0, 0, contract, 0, 0, false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
	// }

	@Ignore
	@Test
	public void testCreateDischargeSlot() {
		fail("Not yet implemented");
	}

	// @Test(expected = IllegalArgumentException.class)
	// public void testCreateDischargeSlot2() {
	//
	// final SchedulerBuilder builder = createScheduleBuilder();
	//
	// final IPort port = Mockito.mock(IPort.class);
	// final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false);
	//
	// final ISalesPriceCalculator curve = Mockito.mock(ISalesPriceCalculator.class);
	//
	// builder.createDischargeSlot("id", port, window, 0, 0, 0, Long.MAX_VALUE, curve, 0, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
	// DEFAULT_VOLUME_LIMIT_IS_M3);
	// }

	@Ignore
	@Test
	public void testCreateVesselClass() {

		final SchedulerBuilder builder = createScheduleBuilder();

		final int minSpeed = 1;
		final int maxSpeed = 2;
		final long capacity = 3L;
		final int safetyHeel = 4;

		final IBaseFuel baseFuel = new BaseFuel("test");
		baseFuel.setEquivalenceFactor(1000);
		final IVessel vessel = builder.createVessel("name", minSpeed, maxSpeed, capacity, safetyHeel, baseFuel, 0, 35353, 10101, 0, false);
		// createVesselClass("name", minSpeed,
		// maxSpeed, capacity, safetyHeel, 700;

		Assert.assertEquals(minSpeed, vessel.getMinSpeed());
		Assert.assertEquals(maxSpeed, vessel.getMaxSpeed());
		Assert.assertEquals(capacity, vessel.getCargoCapacity());
		Assert.assertEquals(safetyHeel, vessel.getSafetyHeel());
		Assert.assertEquals(baseFuel, vessel.getBaseFuel());
		Assert.assertEquals(1000, vessel.getBaseFuel().getEquivalenceFactor());

		Assert.assertEquals(35353, vessel.getWarmupTime());
		Assert.assertEquals(10101, vessel.getCooldownVolume());

		fail("Not yet implemented - Internal state checks");
	}

	private SchedulerBuilder createScheduleBuilder() {
		final SchedulerBuilder builder = new SchedulerBuilder();
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new SharedDataModule(), new AbstractModule() {

			@Override
			protected void configure() {
				// TODO Auto-generated method stub

			}

		});
		injector.injectMembers(builder);
		return builder;
	}
}
