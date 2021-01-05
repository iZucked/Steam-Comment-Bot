/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.microcases.pricebasedwindows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.tests.microcases.MicroCaseUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

@ExtendWith(value = ShiroRunner.class)
@RequireFeature({ KnownFeatures.FEATURE_OPTIMISATION_NO_NOMINALS_IN_PROMPT, KnownFeatures.FEATURE_OPTIMISATION_ACTIONSET })
public class PriceBasedIdleTimeWindowsTests extends AbstractMicroTestCase {

	private static String loadName = "load";
	private static String dischargeName = "discharge";

	@Override
	protected void setPromptDates() {
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	private VesselAvailability createTestVesselAvailability_ForNBOLaden(final LocalDateTime startStart, final LocalDateTime startEnd, final LocalDateTime endStart) {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "70000", 0);

		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("70000") //
				.withStartWindow(startStart, startEnd) //
				.withEndWindow(endStart) //
				.build();
	}

	public IDischargeSlot getDefaultOptimiserDischargeSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final IDischargeSlot discharge = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName),
				IDischargeSlot.class);
		return discharge;
	}

	public ILoadSlot getDefaultOptimiserLoadSlot(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final ILoadSlot load = MicroCaseUtils.getOptimiserObjectFromEMF(scenarioToOptimiserBridge, scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName), ILoadSlot.class);
		return load;
	}

	public @NonNull DischargeSlot getDefaultEMFDischargeSlot() {
		return scenarioModelFinder.getCargoModelFinder().findDischargeSlot(dischargeName);
	}

	public @NonNull LoadSlot getDefaultEMFLoadSlot() {
		return scenarioModelFinder.getCargoModelFinder().findLoadSlot(loadName);
	}

	/**
	 * Create a case when we must not have an idle when Laden
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testScheduleProviderCreatesIdleTime_whenLadenOnNBO() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability_ForNBOLaden(LocalDateTime.of(2017, 1, 1, 0, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0, 0),
				LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2017, 1, 7), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5.32", 23.47) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_DRAGON), null, entity, "7.46") //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 3, 1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			Idle idle = null;
			// check that we have cargo allocations
			Assertions.assertTrue(scenarioRunner.getSchedule().getCargoAllocations().size() > 0);

			for (final CargoAllocation cargoA : scenarioRunner.getSchedule().getCargoAllocations()) {
				SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoA);
				idle = simpleCargoAllocation.getLadenIdle();
				// check that idle time is 0ß
				Assertions.assertEquals(0, idle.getDuration());
			}

		});

	}

	/**
	 * Create a case with high CV = 30.00
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testScheduleProviderCreatesIdleTime_whenLadenOnNBO_HighPortCV() throws Exception {
		// Create the required basic elements
		final VesselAvailability vesselAvailability1 = createTestVesselAvailability_ForNBOLaden(LocalDateTime.of(2017, 1, 1, 0, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0, 0),
				LocalDateTime.of(2018, 8, 1, 0, 0, 0));

		// Construct the cargo scenario
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(loadName, LocalDate.of(2017, 1, 7), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5.32", 30.00) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS).build() //
				.makeDESSale(dischargeName, LocalDate.of(2016, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_DRAGON), null, entity, "7.46") //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS).build() //
				.withVesselAssignment(vesselAvailability1, 1).build();
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 3, 1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			Idle idle = null;
			// check that we have cargo allocations
			Assertions.assertTrue(scenarioRunner.getSchedule().getCargoAllocations().size() > 0);

			for (final CargoAllocation cargoA : scenarioRunner.getSchedule().getCargoAllocations()) {
				SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoA);
				idle = simpleCargoAllocation.getLadenIdle();
				// check that existing idle is 0
				Assertions.assertEquals(0, idle.getDuration());
			}

		});

	}

}
