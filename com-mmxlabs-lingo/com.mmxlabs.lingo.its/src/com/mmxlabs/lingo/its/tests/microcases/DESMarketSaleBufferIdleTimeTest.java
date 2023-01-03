/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
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

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final DESSalesMarket desSalesMarket = spotMarketsModelBuilder.makeDESSaleMarket("D1DESSaleMarket", portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), entity, "7")
				.withDaysBuffer(100).build();
		
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				//Replace DES Sale //("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //with:
				.makeMarketDESSale("D1", desSalesMarket, YearMonth.of(2015, 12), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT))
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(charterInMarket1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			validateCargoIdleTime(scenarioRunner, 24 * 100, 0);
		});
	}
}