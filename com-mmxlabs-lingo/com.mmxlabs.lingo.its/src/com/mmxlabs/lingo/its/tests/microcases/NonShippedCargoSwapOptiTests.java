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

public class NonShippedCargoSwapOptiTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void desPurchaseSwapTest() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "5", null) //
				.withOptional(true) //

				.build() //

				.makeDESSale("D1", LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.build() //

				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Slot load2 = cargoModelBuilder.makeDESPurchase("L2", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "1", null, null) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			scenarioRunner.runAndApplyBest();

			// Wiring should have changed
			Assert.assertSame(load2, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assert.assertSame(discharge1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));

		});
	}

	@Test
	@Category(MicroTest.class)
	public void fobSaleSwapTest() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 18), portFinder.findPort("Bonny Nigeria"), null, entity, "5") //
				.build() //

				.makeFOBSale("D1", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Bonny Nigeria"), null, entity, "7", null) //
				.withOptional(true) //
				.build() //

				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Slot discharge2 = cargoModelBuilder.makeFOBSale("L2", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Bonny Nigeria"), null, entity, "14", null) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			scenarioRunner.runAndApplyBest();

			// Wiring should have changed
			Assert.assertSame(load1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assert.assertSame(discharge2, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));

		});
	}

}
