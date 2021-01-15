/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

/**
 * Test case to make sure a period optimisation is able to utilise the time in an end event crossing the period boundary.
 * 
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class EndEventInPeriodTest extends AbstractMicroTestCase {

	@SuppressWarnings("null")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFlagIsSet() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "150000", 0);

		// Create cargo 1, cargo 2
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 20), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 31), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		// Evaluate initial state and grab end date
		final ZonedDateTime[] endDate = new ZonedDateTime[1];
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final LNGScenarioModel periodScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final VesselAvailability vesselAvailability = periodScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assertions.assertFalse(vesselAvailability.isForceHireCostOnlyEndRule());

			LOOP_SEARCH: for (final Sequence s : periodScenario.getScheduleModel().getSchedule().getSequences()) {
				if (s.getVesselAvailability() == vesselAvailability) {
					for (final Event e : s.getEvents()) {
						if (e instanceof EndEvent) {
							endDate[0] = e.getEnd();
							// No longer expect some sort of duration here as the hire cost only rule is not in the user scenario
							Assertions.assertEquals(e.getStart(), e.getEnd());
							break LOOP_SEARCH;
						}
					}
				}
			}
		});
		Assertions.assertNotNull(endDate[0]);

		// Check the period scenario has been updated correctly.
		evaluateWithLSOTest(false, plan -> {
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2015, 12, 1));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2016, 6));

			ScenarioUtils.setLSOStageIterations(plan, 1_000);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final LNGScenarioModel periodScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final VesselAvailability vesselAvailability = periodScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assertions.assertTrue(vesselAvailability.isForceHireCostOnlyEndRule());

			// Vessel availability is in UTC
			Assertions.assertEquals(endDate[0].withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), vesselAvailability.getEndAfter());
			Assertions.assertEquals(endDate[0].withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), vesselAvailability.getEndBy());

			// Now check optimisation can utilise the space
			Cargo l_cargo2 = periodScenario.getCargoModel().getCargoes().get(1);
			// Check correct cargo lookup
			Assertions.assertEquals("L2", l_cargo2.getLoadName());

			Assertions.assertNotSame(vesselAvailability, l_cargo2.getVesselAssignmentType());

			Assertions.assertNotSame(vesselAvailability1, cargo2.getVesselAssignmentType());
			scenarioRunner.runAndApplyBest();
			Assertions.assertSame(vesselAvailability1, cargo2.getVesselAssignmentType());
			// Cannot check period result as we do not generate any Schedule models in the period schedule any more

		}, null);
	}

}