/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.mmxlabs.lingo.its.tests.microcases.period.AbstractPeriodTestCase;
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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/**
 * Test cases to ensure "standard" (not-divertable) FOB or DES cargoes are scheduled with the compatible time window.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class FOBDESTimeWindowsTest extends AbstractPeriodTestCase {

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

	@Test
	@Category({ MicroTest.class })
	public void testExactDates() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		runTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ca.getInputCargo() == cargo1) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testOverlappingDates1() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 10), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.withWindowSize(48) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(48) //

				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		runTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ca.getInputCargo() == cargo1) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testOverlappingDates2() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.withWindowSize(48) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 10), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(48) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		runTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ca.getInputCargo() == cargo1) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	/**
	 * In this test the start of the sales window is the end (exclusive!) of the purchase window.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDateOutByOneError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
				.withWindowStartTime(0) //
				.withWindowSize(24) //
				.build();
		//
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2015, 12, 12), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0) //
				.build();

		runTest(scenarioRunner -> {
			LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			ISequences rawSequences = createFOBDESSequences(scenarioToOptimiserBridge, loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
	}

	/**
	 * In this test the start of the sales window is just after the end (exclusive!) of the purchase window.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDateOutByTwoError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
				.withWindowStartTime(0) //
				.withWindowSize(24) //
				.build();
		//
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2015, 12, 12), dischargePort, null, entity, "7") //
				.withWindowStartTime(1) //
				.withWindowSize(0) //
				.build();

		runTest(scenarioRunner -> {
			LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			ISequences rawSequences = createFOBDESSequences(scenarioToOptimiserBridge, loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
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

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}

	private @NonNull ISequences createFOBDESSequences(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final LoadSlot loadSlot, DischargeSlot dischargeSlot) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		@NonNull
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = dataTransformer.getInjector().getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		IResource o_resource = null;
		if (loadSlot.isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			o_resource = vesselProvider.getResource(vesselAvailability);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			o_resource = vesselProvider.getResource(vesselAvailability);
		}

		Assert.assertNotNull(o_resource);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		{
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			modifiableSequence.add(element);
		}
		{
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			modifiableSequence.add(element);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return sequences;
	}
}