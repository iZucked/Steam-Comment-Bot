/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;

/**
 * Test cases to make sure "shipping only" mode does not cause wiring changes.
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

		spotMarketsModelBuilder.makeDESSaleMarket("IoGMarket", portFinder.findPort("Isle of Grain"), entity, "5") //
				.withAvailabilityConstant(1) //
				.build();

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setWithSpotCargoMarkets(true), null, scenarioRunner -> {

			// Cargo should have been created
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getDischargeSlots().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getLoadSlots().size());
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
			Assert.assertTrue(lngScenarioModel.getCargoModel().getCargoes().isEmpty());
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().isEmpty());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getLoadSlots().size());
		}, null);
	}
}
