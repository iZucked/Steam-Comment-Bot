/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;

/**
 * FB1918 - Pre-created FOB Sale spot market slots ignored the valid port set and instead were permitted to bind to any load port.
 *
 */
@ExtendWith(ShiroRunner.class)
public class FOBSaleSpotMarketTests extends AbstractMicroTestCase {

	/**
	 * This should fail as bad port match
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Tag(TestCategories.REGRESSION_TEST)
	public void testBadPortMatch() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_IDKU)), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> s.setFullCargoLot(true)) //
				.build() //

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPortById(InternalDataConstants.PORT_IDKU)) //
				.build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are invalid
			final List<IConstraintChecker> validateConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(validateConstraintCheckers);
			Assertions.assertTrue(validateConstraintCheckers.get(0) instanceof FOBDESCompatibilityConstraintChecker);
		});
	}

	/**
	 * FB 1918 - this would rewire cargoes as in #testUnrestrictedOptimisation_ExistingSlot()
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Tag(TestCategories.REGRESSION_TEST)
	public void testRestrictedOptimisation_ExistingSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		// This cargo can be optimised out
		final Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5.1") //
				.with(s -> s.setFullCargoLot(true))
				.withOptional(true)//
				.build()

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		final DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "4") //
				.with(s -> s.setFullCargoLot(true))
				.build() //

				.makeFOBSale("D2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.runAndApplyBest();
			Assertions.assertNotNull(result);

			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assertions.assertEquals(2, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo one should still be present
			Assertions.assertNull(cargo1.eContainer());
			// Cargo two should still be present
			Assertions.assertNotNull(cargo2.eContainer());

			// Check the no wiring changes
			// Assertions.assertSame(cargo1, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assertions.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load1));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			// Assertions.assertTrue(cargo1.getSlots().contains(load1));
			// Assertions.assertTrue(cargo1.getSlots().contains(discharge2));

			Assertions.assertTrue(cargo2.getSlots().contains(load2));
			Assertions.assertTrue(cargo2.getSlots().contains(discharge2));

			//
			// // Check locked flags
			// Assertions.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
			// Assertions.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
			// Assertions.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

			// Assert initial state can be evaluated
			// final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			// Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Assertions.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testUnrestrictedOptimisation_ExistingSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_IDKU)), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "5.1") //
				.with(s -> s.setFullCargoLot(true)) //
				.withOptional(true)//
				.build()

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPortById(InternalDataConstants.PORT_IDKU)) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load1 = (LoadSlot) cargo1.getSlots().get(0);
		final DischargeSlot discharge1 = (DischargeSlot) cargo1.getSlots().get(1);

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "4") //
				.with(s -> s.setFullCargoLot(true)) //
				.build() //

				.makeFOBSale("D2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.runAndApplyBest();
			Assertions.assertNotNull(result);

			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assertions.assertEquals(2, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(2, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo one should be removed
			Assertions.assertNull(cargo1.eContainer());
			// Cargo two should still be present
			Assertions.assertNotNull(cargo2.eContainer());

			// Check the spot market slot has been removed and cargo re-wired
			Assertions.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load1));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge1));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			Assertions.assertTrue(cargo2.getSlots().contains(load2));
			Assertions.assertTrue(cargo2.getSlots().contains(discharge1));

			//
			// // Check locked flags
			// Assertions.assertFalse(optimiserScenario.getCargoModel().getCargoes().get(0).isLocked());
			// Assertions.assertFalse(optimiserScenario.getCargoModel().getLoadSlots().get(0).isLocked());
			// Assertions.assertTrue(optimiserScenario.getCargoModel().getDischargeSlots().get(0).isLocked());

			// Assert initial state can be evaluated
			// final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			// Assertions.assertTrue(MicroTestUtils.evaluateLSOSequences(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Assertions.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestrictedOptimisation_GeneratedSlot() throws Exception {

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)), entity, "5") //
				.withAvailabilityConstant(1)//
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "4") //
				.with(s -> s.setFullCargoLot(true)) //
				.build() //

				.makeFOBSale("D2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.runAndApplyBest();
			Assertions.assertNotNull(result);

			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, lngScenarioModel.getCargoModel().getDischargeSlots().size());

			// Cargo two should still be present
			Assertions.assertNotNull(cargo2.eContainer());

			// Check the spot market slot has been removed and cargo re-wired
			Assertions.assertSame(cargo2, lngScenarioModel.getCargoModel().getCargoes().get(0));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getLoadSlots().contains(load2));
			Assertions.assertTrue(lngScenarioModel.getCargoModel().getDischargeSlots().contains(discharge2));

			Assertions.assertTrue(cargo2.getSlots().contains(load2));
			Assertions.assertTrue(cargo2.getSlots().contains(discharge2));

			// Assertions.fail("Should not pass *new* constraint checker");

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testUnrestrictedOptimisation_GeneratedSlot() throws Exception {

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2015, 6, 1));

		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_IDKU)), entity, "5") //
				.withAvailabilityConstant(1)//
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "4") //
				.with(s -> s.setFullCargoLot(true)) //
				.build() //

				.makeFOBSale("D2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 6, 2), portFinder.findPortById(InternalDataConstants.PORT_IDKU), null, entity, "3.9", null) //
				.withOptional(true) //
				.build() //

				.withAssignmentFlags(true, false)//
				.build();

		final LoadSlot load2 = (LoadSlot) cargo2.getSlots().get(0);
		final DischargeSlot discharge2 = (DischargeSlot) cargo2.getSlots().get(1);
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final IMultiStateResult result = scenarioRunner.runAndApplyBest();
			Assertions.assertNotNull(result);

			// Cargo should have been created
			Assertions.assertNotNull(cargo2.eContainer());
			Assertions.assertSame(load2, cargo2.getSortedSlots().get(0));
			Assertions.assertSame(market, ((SpotSlot) cargo2.getSortedSlots().get(1)).getMarket());
		});
	}

}