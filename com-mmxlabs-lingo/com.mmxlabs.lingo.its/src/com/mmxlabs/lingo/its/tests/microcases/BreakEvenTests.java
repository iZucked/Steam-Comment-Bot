/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
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
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

@RunWith(value = ShiroRunner.class)
public class BreakEvenTests {

	/**
	 * DES Purchase break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_BE_Purchase() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?", null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(7.0, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * DES Purchase break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_BE_Sale() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?") //
				.build() //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(7.0, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * FOB Sale break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_BE_Sale() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.build() //
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?", null) //
				.build() //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(7.0, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * FOB Sale break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_BE_Purchase() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?") //
				.build() //
				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build() //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(7.0, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * Shipped break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testShipped_BE_Purchase() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "?") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(6.558, cargoAllocation.getLoadAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	/**
	 * Shipped break even
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testShipped_BE_Sale() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "?") //
				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		runTest(scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertSame(cargo1, cargoAllocation.getCargoAllocation().getInputCargo());

			Assert.assertEquals(7.454, cargoAllocation.getDischargeAllocation().getPrice(), 0.001);

			assertZeroPNL(cargoAllocation);
		});
	}

	protected void assertZeroPNL(final SimpleCargoAllocation cargoAllocation) {
		Assert.assertEquals(0, (int) PNLDeltaUtils.getElementProfitAndLoss(cargoAllocation.getCargoAllocation()), 1);
	}

	private LNGScenarioModel lngScenarioModel;
	private ScenarioModelFinder scenarioModelFinder;
	private ScenarioModelBuilder scenarioModelBuilder;
	private CommercialModelFinder commercialModelFinder;
	private FleetModelFinder fleetModelFinder;
	private PortModelFinder portFinder;
	private CargoModelBuilder cargoModelBuilder;
	private FleetModelBuilder fleetModelBuilder;
	private SpotMarketsModelBuilder spotMarketsModelBuilder;
	private PricingModelBuilder pricingModelBuilder;
	private BaseLegalEntity entity;

	@Before
	public void constructor() throws MalformedURLException {

		lngScenarioModel = importReferenceData();

		scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portFinder = scenarioModelFinder.getPortModelFinder();

		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

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
		spotMarketsModelBuilder = null;
		pricingModelBuilder = null;
		entity = null;
	}

	private void runTest(final Consumer<LNGScenarioRunner> checker) {
		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false);
			scenarioRunner.evaluateInitialState();
			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
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
}