/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;

/**
 * 
 * @author Simon Goodall
 *
 */
public class NoSpotCargoMarketsOptiTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void spotCargoMarketPermitted() throws Exception {

		final Slot load2 = cargoModelBuilder.makeDESPurchase("DP", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "1", 22.6, null) //
				.withOptional(true) //
				.build();

		final SpotMarket market = spotMarketsModelBuilder.makeDESSaleMarket("IoGMarket", portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), entity, "5") //
				.withAvailabilityConstant(1) //
				.build();

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setWithSpotCargoMarkets(true), null, scenarioRunner -> {

			Assertions.assertNotNull(load2.getCargo());
			// Cargo should have been created
			Assertions.assertNotNull(load2.getCargo());
			Assertions.assertSame(market, ((SpotSlot) load2.getCargo().getSortedSlots().get(1)).getMarket());
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void spotCargoMarket_NotPermitted() throws Exception {

		final Slot load1 = cargoModelBuilder.makeDESPurchase("DP", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "1", 22.8, null) //
				.withOptional(true) //
				.build();

		spotMarketsModelBuilder.makeDESSaleMarket("IoGMarket", portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), entity, "5") //
				.withAvailabilityConstant(1) //
				.build();

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setWithSpotCargoMarkets(false), null, scenarioRunner -> {
			// Cargo should not have been created
			Assertions.assertNull(load1.getCargo());
		}, null);
	}
}
