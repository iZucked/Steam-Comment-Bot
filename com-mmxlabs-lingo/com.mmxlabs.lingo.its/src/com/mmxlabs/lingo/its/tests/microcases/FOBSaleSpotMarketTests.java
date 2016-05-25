/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.RegressionTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintChecker;

/**
 * FB1918 - Pre-created FOB Sale spot market slots ignored the valid port set and instead were permitted to bind to any load port.
 *
 */
@RunWith(value = ShiroRunner.class)
public class FOBSaleSpotMarketTests {

	private LNGScenarioModel lngScenarioModel;
	private ScenarioModelBuilder scenarioModelBuilder;
	private ScenarioModelFinder scenarioModelFinder;
	private CommercialModelFinder commercialModelFinder;
	private FleetModelFinder fleetModelFinder;
	private PortModelFinder portFinder;
	private CargoModelBuilder cargoModelBuilder;
	private FleetModelBuilder fleetModelBuilder;
	private SpotMarketsModelBuilder spotMarketsBuilder;
	private BaseLegalEntity entity;

	@Before
	public void constructor() throws MalformedURLException {

		// Load in the basic scenario from CSV
		lngScenarioModel = importReferenceData();

		// Create finder and builder
		scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portFinder = scenarioModelFinder.getPortModelFinder();

		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		spotMarketsBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// Create the required basic elements
		entity = commercialModelFinder.findEntity("Shipping");
	}

	@After
	public void destructor() {
		lngScenarioModel = null;
		scenarioModelFinder = null;
		scenarioModelBuilder = null;
		commercialModelFinder = null;
		fleetModelFinder = null;
		portFinder = null;
		cargoModelBuilder = null;
		fleetModelBuilder = null;
		entity = null;
	}

	/**
	 * This should fail as bad port match
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ RegressionTest.class, MicroTest.class })
	public void testBadPortMatch() throws Exception {

		final FOBSalesMarket market = spotMarketsBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //

				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPort("Idku LNG")) //
				.build() //

				.build();

		runTest(scenarioRunner -> {
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

		final FOBSalesMarket market = spotMarketsBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Point Fortin")), entity, "5") //
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

		runTest(scenarioRunner -> {
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

		final FOBSalesMarket market = spotMarketsBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
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
		runTest(scenarioRunner -> {
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

		final FOBSalesMarket market = spotMarketsBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Point Fortin")), entity, "5") //
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
		runTest(scenarioRunner -> {
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

		final FOBSalesMarket market = spotMarketsBuilder.makeFOBSaleMarket("FOBSaleMarket", PortModelBuilder.makePortSet(portFinder.findPort("Idku LNG")), entity, "5") //
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
		runTest(scenarioRunner -> {
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

	@NonNull
	public LNGScenarioModel importReferenceData() throws MalformedURLException {
		return importReferenceData("/referencedata/reference-data-1/");
	}

	@NonNull
	public LNGScenarioModel importReferenceData(final String url) throws MalformedURLException {

		final @NonNull String urlRoot = getClass().getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importMarketData(urlRoot);

		return importer.doImport();
	}

	private void runTest(final Consumer<LNGScenarioRunner> checker) {
		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		// Limit optimisation run time
		optimiserSettings.getAnnealingSettings().setIterations(10_000);
		optimiserSettings.getSolutionImprovementSettings().setImprovingSolutions(false);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();

			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}

}