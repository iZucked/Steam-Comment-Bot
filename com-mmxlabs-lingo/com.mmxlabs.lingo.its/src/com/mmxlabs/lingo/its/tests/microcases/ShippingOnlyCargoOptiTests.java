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
public class ShippingOnlyCargoOptiTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void shippingOnly_SlotSwap_Permitted() throws Exception {

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

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setShippingOnly(false), null, scenarioRunner -> {

			// Wiring should have changed
			Assert.assertSame(load2, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assert.assertSame(discharge1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));

		}, null);
	}

	@Test
	@Category(MicroTest.class)
	public void shippingOnly_SlotSwap_NotPermitted() throws Exception {

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
		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setShippingOnly(true), null, scenarioRunner -> {

			// Wiring should have changed
			Assert.assertSame(load1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assert.assertSame(discharge1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));
		}, null);
	}

	@Test
	@Category(MicroTest.class)
	public void shippingOnly_OptionalSlots_Permitted() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "5", null, null) //
				.build(); //

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.build(); //

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setShippingOnly(false), null, scenarioRunner -> {

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());

		}, null);
	}

	@Test
	@Category(MicroTest.class)
	public void shippingOnly_OptionalSlots_NotPermitted() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "5", null, null) //
				.build(); //

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 2, 18), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.build(); //

		evaluateWithLSOTest(true, plan -> plan.getUserSettings().setShippingOnly(true), null, scenarioRunner -> {

			Assert.assertEquals(0, lngScenarioModel.getCargoModel().getCargoes().size());

		}, null);
	}
}
