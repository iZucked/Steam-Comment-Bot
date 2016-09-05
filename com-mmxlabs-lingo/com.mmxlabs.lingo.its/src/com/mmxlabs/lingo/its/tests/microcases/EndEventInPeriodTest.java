/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
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
@RunWith(value = ShiroRunner.class)
public class EndEventInPeriodTest extends AbstractMicroTestCase {

	@SuppressWarnings("null")
	@Test
	@Category({ MicroTest.class })
	public void testFlagIsSet() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "150000", 0);

		// Create cargo 1, cargo 2
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 20), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 31), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		// Evaluate initial state and grab end date
		final ZonedDateTime[] endDate = new ZonedDateTime[1];
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final LNGScenarioModel periodScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final VesselAvailability vesselAvailability = periodScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertFalse(vesselAvailability.isForceHireCostOnlyEndRule());

			LOOP_SEARCH: for (final Sequence s : periodScenario.getScheduleModel().getSchedule().getSequences()) {
				if (s.getVesselAvailability() == vesselAvailability) {
					for (final Event e : s.getEvents()) {
						if (e instanceof EndEvent) {
							endDate[0] = e.getEnd();
							// Expect some sort of duration here
							Assert.assertNotEquals(e.getStart(), e.getEnd());
							break LOOP_SEARCH;
						}
					}
				}
			}
		});
		Assert.assertNotNull(endDate[0]);

		// Check the period scenario has been updated correctly.
		evaluateWithLSOTest(false, plan -> {
			plan.getUserSettings().setPeriodStart(YearMonth.of(2015, 12));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2016, 6));

			ScenarioUtils.setLSOStageIterations(plan, 1_000);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final LNGScenarioModel periodScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final VesselAvailability vesselAvailability = periodScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertTrue(vesselAvailability.isForceHireCostOnlyEndRule());

			// Vessel availability is in UTC
			Assert.assertEquals(endDate[0].withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), vesselAvailability.getEndAfter());
			Assert.assertEquals(endDate[0].withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), vesselAvailability.getEndBy());

			// Now check optimisation can utilise the space
			Cargo l_cargo2 = periodScenario.getCargoModel().getCargoes().get(1);
			// Check correct cargo lookup
			Assert.assertEquals("L2", l_cargo2.getLoadName());

			Assert.assertNotSame(vesselAvailability, l_cargo2.getVesselAssignmentType());

			scenarioRunner.run();

			Assert.assertSame(vesselAvailability, l_cargo2.getVesselAssignmentType());

		}, null);
	}

}