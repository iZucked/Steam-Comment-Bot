/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.RegressionTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;

/**
 * FB1918 - Pre-created FOB Sale spot market slots ignored the valid port set and instead were permitted to bind to any load port.
 *
 */
@RunWith(value = ShiroRunner.class)
public class FOBSaleSpotMarketTests extends AbstractMicroTestCase {

	/**
	 * This should fail as bad port match
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ RegressionTest.class, MicroTest.class })
	public void testBadPortMatch() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPort("Idku LNG")) //
				.build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are invalid
			final List<IConstraintChecker> validateConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(validateConstraintCheckers);
			Assert.assertTrue(validateConstraintCheckers.get(0) instanceof FOBDESCompatibilityConstraintChecker);
		});
	}

	/**
	 * FB 1918 - this would rewire cargoes as in #testUnrestrictedOptimisation_ExistingSlot()
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class, RegressionTest.class })
	public void testRestrictedOptimisation_ExistingSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Point Fortin")), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		// This cargo can be optimised out
		final Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 2), portFinder.findPort("Point Fortin"), null, entity, "5.1") //
				.withOptional(true)//
				.build()

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPort("Point Fortin")) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		final DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "4") //
				.build() //

				.makeFOBSale("D2", false, LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.run();
			Assert.assertNotNull(result);

			scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assert.assertEquals(2, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo one should still be present
			Assert.assertNull(cargo1.eContainer());
			// Cargo two should still be present
			Assert.assertNotNull(cargo2.eContainer());

			// Check the no wiring changes
			// Assert.assertSame(cargo1, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assert.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load1));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			// Assert.assertTrue(cargo1.getSlots().contains(load1));
			// Assert.assertTrue(cargo1.getSlots().contains(discharge2));

			Assert.assertTrue(cargo2.getSlots().contains(load2));
			Assert.assertTrue(cargo2.getSlots().contains(discharge2));

			//
			// // Check locked flags
			// Assert.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
			// Assert.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
			// Assert.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

			// Assert initial state can be evaluated
			// final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			// Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Assert.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testUnrestrictedOptimisation_ExistingSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "5.1") //
				.withOptional(true)//
				.build()

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPort("Idku LNG")) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		final DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "4") //
				.build() //

				.makeFOBSale("D2", false, LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.run();
			Assert.assertNotNull(result);

			scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assert.assertEquals(2, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(2, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo one should be removed
			Assert.assertNull(cargo1.eContainer());
			// Cargo two should still be present
			Assert.assertNotNull(cargo2.eContainer());

			// Check the spot market slot has been removed and cargo re-wired
			Assert.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load1));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge1));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			Assert.assertTrue(cargo2.getSlots().contains(load2));
			Assert.assertTrue(cargo2.getSlots().contains(discharge1));

			//
			// // Check locked flags
			// Assert.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
			// Assert.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
			// Assert.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

			// Assert initial state can be evaluated
			// final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			// Assert.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Assert.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestrictedOptimisation_GeneratedSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Point Fortin")), entity, "5") //
				.withAvailabilityConstant(1)//
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "4") //
				.build() //

				.makeFOBSale("D2", false, LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.run();
			Assert.assertNotNull(result);

			scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo two should still be present
			Assert.assertNotNull(cargo2.eContainer());

			// Check the spot market slot has been removed and cargo re-wired
			Assert.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			Assert.assertTrue(cargo2.getSlots().contains(load2));
			Assert.assertTrue(cargo2.getSlots().contains(discharge2));

			// Assert.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testUnrestrictedOptimisation_GeneratedSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
				.withAvailabilityConstant(1)//
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "4") //
				.build() //

				.makeFOBSale("D2", false, LocalDate.of(2015, 6, 2), portFinder.findPort("Idku LNG"), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.run();
			Assert.assertNotNull(result);

			scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());

			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(2, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo two should still be present
			Assert.assertNotNull(cargo2.eContainer());

			// Check the spot market slot has been removed and cargo re-wired
			Assert.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assert.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			Assert.assertTrue(cargo2.getSlots().contains(load2));
			Assert.assertFalse(cargo2.getSlots().contains(discharge2));

			// TODO: Check for new spot slot

			// Assert.fail("Should not pass *new* constraint checker");

		});
	}

}