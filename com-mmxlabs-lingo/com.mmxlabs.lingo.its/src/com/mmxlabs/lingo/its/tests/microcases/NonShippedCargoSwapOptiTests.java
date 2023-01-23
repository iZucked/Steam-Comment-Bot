/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;

public class NonShippedCargoSwapOptiTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void desPurchaseSwapTest() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "5", 22.8, null) //
				.withOptional(true) //

				.build() //

				.makeDESSale("D1", LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.build() //

				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Slot load2 = cargoModelBuilder
				.makeDESPurchase("L2", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "1", 22.8, null) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			scenarioRunner.runAndApplyBest();

			// Wiring should have changed
			Assertions.assertSame(load2, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assertions.assertSame(discharge1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void fobSaleSwapTest() throws Exception {

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "5") //
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "7", null) //
				.withOptional(true) //
				.build() //

				.build();

		Slot load1 = cargo1.getSlots().get(0);
		Slot discharge1 = cargo1.getSlots().get(1);

		final Slot discharge2 = cargoModelBuilder
				.makeFOBSale("L2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2016, 2, 18), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "14", null) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			scenarioRunner.runAndApplyBest();

			// Wiring should have changed
			Assertions.assertSame(load1, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(0));
			Assertions.assertSame(discharge2, lngScenarioModel.getCargoModel().getCargoes().get(0).getSortedSlots().get(1));

		});
	}

}
