/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

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
