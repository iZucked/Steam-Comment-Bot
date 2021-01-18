/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import javax.inject.Singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.BaseFuel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VoyagePlanStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;
import com.mmxlabs.scheduler.optimiser.shared.SharedPortDistanceDataBuilder;

public class SchedulerBuilderTest {

	public static final boolean DEFAULT_VOLUME_LIMIT_IS_M3 = true;

	@Disabled
	@Test
	public void testSchedulerBuilder() {

		/**
		 * How to test builder? -- No access to state until we get the finished product? Perhaps a second constructor passing in DCP objects and ensure they are called correctly.
		 */

		Assertions.fail("Not yet implemented");
	}

	@Disabled
	@Test
	public void testCreateVessel() {
		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final SchedulerBuilder builder = createScheduleBuilder();

		final int minSpeed = 1;
		final int maxSpeed = 2;
		final long capacity = 3L;
		final int safetyHeel = 4;

		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "test");
		baseFuel.setEquivalenceFactor(1000);
		final IVessel vessel = builder.createVessel("name", minSpeed, maxSpeed, capacity, safetyHeel, baseFuel, baseFuel, baseFuel, baseFuel, 0, 0, 35353, 10101, 0, false);
		// createVesselClass("name", minSpeed,
		// maxSpeed, capacity, safetyHeel, 700;

		Assertions.assertEquals(minSpeed, vessel.getMinSpeed());
		Assertions.assertEquals(maxSpeed, vessel.getMaxSpeed());
		Assertions.assertEquals(capacity, vessel.getCargoCapacity());
		Assertions.assertEquals(safetyHeel, vessel.getSafetyHeel());
		Assertions.assertEquals(baseFuel, vessel.getTravelBaseFuel());
		Assertions.assertEquals(1000, vessel.getTravelBaseFuel().getEquivalenceFactor());

		Assertions.assertEquals(35353, vessel.getWarmupTime());
		Assertions.assertEquals(10101, vessel.getCooldownVolume());

		Assertions.fail("Not yet implemented - Internal state checks");
	}

	@Test
	public void testMultipleFuelVessel() {
		final SimpleIndexingContext indexingContext = new SimpleIndexingContext();
		indexingContext.registerType(IBaseFuel.class);
		indexingContext.assignIndex(IBaseFuel.LNG);

		final IBaseFuel baseFuel = new BaseFuel(indexingContext, "BASE");
		baseFuel.setEquivalenceFactor(1_000);
		final IBaseFuel idleBaseFuel = new BaseFuel(indexingContext, "IDLE");
		idleBaseFuel.setEquivalenceFactor(2_000);
		final IBaseFuel inPortBaseFuel = new BaseFuel(indexingContext, "PORT");
		inPortBaseFuel.setEquivalenceFactor(3_000);
		final IBaseFuel pilotLightBaseFuel = new BaseFuel(indexingContext, "PILOT");
		pilotLightBaseFuel.setEquivalenceFactor(4_000);

		final int minSpeed = 1;
		final int maxSpeed = 2;
		final long capacity = 3L;
		final int safetyHeel = 4;

		final SchedulerBuilder builder = createScheduleBuilder();
		final IVessel vessel = builder.createVessel("name", minSpeed, maxSpeed, capacity, safetyHeel, baseFuel, idleBaseFuel, inPortBaseFuel, pilotLightBaseFuel, 0, 0, 35353, 10101, 0, false);
		Assertions.assertEquals(baseFuel, vessel.getTravelBaseFuel());
		Assertions.assertEquals(idleBaseFuel, vessel.getIdleBaseFuel());
		Assertions.assertEquals(inPortBaseFuel, vessel.getInPortBaseFuel());
		Assertions.assertEquals(pilotLightBaseFuel, vessel.getPilotLightBaseFuel());

		Assertions.assertEquals(1_000, vessel.getTravelBaseFuel().getEquivalenceFactor());
		Assertions.assertEquals(2_000, vessel.getIdleBaseFuel().getEquivalenceFactor());
		Assertions.assertEquals(3_000, vessel.getInPortBaseFuel().getEquivalenceFactor());
		Assertions.assertEquals(4_000, vessel.getPilotLightBaseFuel().getEquivalenceFactor());

	}

	private SchedulerBuilder createScheduleBuilder() {
		// final SchedulerBuilder builder = new SchedulerBuilder();
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new SharedDataModule(), new AbstractModule() {

			@Override
			protected void configure() {
				bind(CalendarMonthMapper.class).toInstance(Mockito.mock(CalendarMonthMapper.class));
				bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
				bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);
			}
		});

		final SharedPortDistanceDataBuilder portBuilder = injector.getInstance(SharedPortDistanceDataBuilder.class);

		final SchedulerBuilder builder = injector.getInstance(SchedulerBuilder.class);

		// injector.injectMembers(builder);
		return builder;
	}
}
