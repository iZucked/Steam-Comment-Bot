/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

@SuppressWarnings("null")
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

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLoadSlot2() {

		final SchedulerBuilder builder = createScheduleBuilder();

		final IPort port = Mockito.mock(IPort.class);
		final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false);

		final ILoadPriceCalculator contract = Mockito.mock(ILoadPriceCalculator.class);

		builder.createLoadSlot("id", port, window, 0, 0, contract, 0, 0, false, false, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_LOAD, false, false, false, DEFAULT_VOLUME_LIMIT_IS_M3);
	}




	@Ignore
	@Test
	public void testCreateDischargeSlot() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateDischargeSlot2() {

		final SchedulerBuilder builder = createScheduleBuilder();

		final IPort port = Mockito.mock(IPort.class);
		final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false);

		final ISalesPriceCalculator curve = Mockito.mock(ISalesPriceCalculator.class);

		builder.createDischargeSlot("id", port, window, 0, 0, 0, Long.MAX_VALUE, curve, 0, IPortSlot.NO_PRICING_DATE, PricingEventType.START_OF_DISCHARGE, false, false, false,
				DEFAULT_VOLUME_LIMIT_IS_M3);
	}



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
		final IVesselClass vesselClass = builder.createVesselClass("name", minSpeed, maxSpeed, capacity, safetyHeel, baseFuel, 0, 35353, 10101, 0, false);
		// createVesselClass("name", minSpeed,
		// maxSpeed, capacity, safetyHeel, 700;

		Assert.assertEquals(minSpeed, vesselClass.getMinSpeed());
		Assert.assertEquals(maxSpeed, vesselClass.getMaxSpeed());
		Assert.assertEquals(capacity, vesselClass.getCargoCapacity());
		Assert.assertEquals(safetyHeel, vesselClass.getSafetyHeel());
		Assert.assertEquals(baseFuel, vesselClass.getBaseFuel());
		Assert.assertEquals(1000, vesselClass.getBaseFuel().getEquivalenceFactor());

		Assert.assertEquals(35353, vesselClass.getWarmupTime());
		Assert.assertEquals(10101, vesselClass.getCooldownVolume());

		fail("Not yet implemented - Internal state checks");
	}

	private SchedulerBuilder createScheduleBuilder() {
		final SchedulerBuilder builder = new SchedulerBuilder();
		final Injector injector = Guice.createInjector(new DataComponentProviderModule());
		injector.injectMembers(builder);
		return builder;
	}
}
