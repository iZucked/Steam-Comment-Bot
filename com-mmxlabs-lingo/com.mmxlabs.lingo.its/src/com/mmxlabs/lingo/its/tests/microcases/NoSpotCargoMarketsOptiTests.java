/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 * 
 * @author Simon Goodall
 *
 */
public class NoSpotCargoMarketsOptiTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void spotCargoMarketPermitted() throws Exception {

		final Slot load2 = cargoModelBuilder.makeDESPurchase("DP", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "1", null, null) //
				.withOptional(true) //
				.build();

		final SpotMarket market = spotMarketsModelBuilder.makeDESSaleMarket("IoGMarket", portFinder.findPort("Isle of Grain"), entity, "5") //
				.withAvailabilityConstant(1) //
				.build();

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setWithSpotCargoMarkets(true), null, scenarioRunner -> {

			Assert.assertNotNull(load2.getCargo());
			// Cargo should have been created
			Assert.assertNotNull(load2.getCargo());
			Assert.assertSame(market, ((SpotSlot) load2.getCargo().getSortedSlots().get(1)).getMarket());
		}, null);
	}

	@Test
	@Category(MicroTest.class)
	public void spotCargoMarket_NotPermitted() throws Exception {

		final Slot load1 = cargoModelBuilder.makeDESPurchase("DP", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "1", null, null) //
				.withOptional(true) //
				.build();

		spotMarketsModelBuilder.makeDESSaleMarket("IoGMarket", portFinder.findPort("Isle of Grain"), entity, "5") //
				.withAvailabilityConstant(1) //
				.build();

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setWithSpotCargoMarkets(false), null, scenarioRunner -> {
			// Cargo should not have been created
			Assert.assertNull(load1.getCargo());
		}, null);
	}
}
