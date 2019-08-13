/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;

@ExtendWith(value = ShiroRunner.class)
public class DESMarketSaleBufferIdleTimeTest extends AbstractIdleTimeTests {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESSaleMarketDaysBufferIdleTimeIncrease() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		final DESSalesMarket desSalesMarket = spotMarketsModelBuilder.makeDESSaleMarket("D1DESSaleMarket", portFinder.findPort("Dominion Cove Point LNG"), entity, "7")
				.withDaysBuffer(100).build();
		
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				//Replace DES Sale //("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //with:
				.makeMarketDESSale("D1", desSalesMarket, YearMonth.of(2015, 12), portFinder.findPort("Dominion Cove Point LNG"))
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			validateIdleTime(scenarioRunner, 24 * 100, 0);
		});
	}
}