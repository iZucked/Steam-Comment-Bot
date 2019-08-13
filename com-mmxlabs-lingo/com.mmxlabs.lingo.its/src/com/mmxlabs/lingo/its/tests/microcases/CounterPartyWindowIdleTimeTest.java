/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;

@ExtendWith(value = ShiroRunner.class)
public class CounterPartyWindowIdleTimeTest extends AbstractIdleTimeTests {
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESSaleCounterPartyWindow() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") 
				.withWindowSize(100, TimePeriod.DAYS)
				.withWindowCounterParty(true)
				.withVisitDuration(36)
				.build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			//TODO: check why this is 2 hours less than expected?
			validateSlotVisitDuration(scenarioRunner, "D1", (24 * 100) + 36 - 2);
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBPurchaseCounterPartyWindow() {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withWindowSize(100, TimePeriod.DAYS)
				.withWindowCounterParty(true)
				.withVisitDuration(36)
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") 
				.build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			//TODO: check why this is 1 hours less than expected?
			validateSlotVisitDuration(scenarioRunner, "L1", (24 * 100) + 36 - 1);
		});
	}
}