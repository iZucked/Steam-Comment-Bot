/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.schedule.CapacityViolationChecker;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class CapacityViolationTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("optimisation-charter-out-generation");
	private static List<String> addedFeatures = new LinkedList<>();

	private VesselClass vesselClass;
	private Vessel vessel;
	private VesselAvailability vesselAvailability1;
	private Cargo cargo1;

	public class boilOffOverride implements IOptimiserInjectorService {

		private boolean activateOverride = false;
		private boolean minMaxVolumeAllocator = false;

		public boilOffOverride(final boolean activateOverride, final boolean minMaxVolumeAllocator) {
			this.activateOverride = activateOverride;
			this.minMaxVolumeAllocator = minMaxVolumeAllocator;
		}

		@Override
		public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			return null;
		}

		@Override
		public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_LNGTransformerModule) {
				return Collections.<@NonNull Module> singletonList(new AbstractModule() {
					@Override
					protected void configure() {
						if (minMaxVolumeAllocator)
							bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(MinMaxUnconstrainedVolumeAllocator.class);
					}

					@Provides
					@Named(LNGTransformerModule.COMMERCIAL_VOLUME_OVERCAPACITY)
					private boolean commercialVolumeOverCapacity() {
						return activateOverride;
					}
				});
			}
			return null;
		}
	}

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	public void initCommonData1() {

		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0))

				.build();

		// Create cargo 1
		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 10), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();
	}

	@Ignore("Uncertain behaviour regarding heel violations. Unable to produce multiple valid violations on one cargo.")
	@Test
	@Category({ MicroTest.class })
	public void testMultipleCapacityConstraints() throws Exception {
		initCommonData1();

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPort("Point Fortin"), null, entity, "5", null).build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		@NonNull
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 50.0, 22.8, "1") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assert.assertNull(failedConstraintCheckers);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testViableCapacityConstraintSwap() throws Exception {
		initCommonData1();
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 6), portFinder.findPort("Point Fortin"), null, entity, "5", null).build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assert.assertNull(failedConstraintCheckers);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testInvalidCapacityConstraint() throws Exception {
		initCommonData1();
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", null)
				.withVolumeLimits(100000, 150000, VolumeUnits.M3)//
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(200000, 250000, VolumeUnits.M3).build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// try {
			// MicroCaseUtils.storeToFile(optimiserScenario,"X");
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is a singular constraint checker
			Assert.assertEquals(1, failedConstraintCheckers.size());
			// Check that it is CapacityEvaluatedStateChecker
			final IEvaluatedStateConstraintChecker con = failedConstraintCheckers.get(0);
			Assert.assertTrue(con instanceof CapacityEvaluatedStateChecker);
			// Cast to CESC
			final CapacityEvaluatedStateChecker castedCon = (CapacityEvaluatedStateChecker) con;
			// Check that there was a single initial violation
			Assert.assertEquals(1, castedCon.getInitialViolations());

			final Set<IPortSlot> slots = castedCon.getCurrentViolatedSlots();
			final long violations = castedCon.getTotalViolations();
			// Check that there is only a singular violated slot and a singular violation.
			Assert.assertEquals(1, violations);
			Assert.assertEquals(1, slots.size());
			// Check that the violation is a MIN_DISCHARGE
			Assert.assertEquals(CapacityViolationType.MIN_DISCHARGE, castedCon.getTriggeredViolations().get(0));

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(0, 8000.0, 22.5, "5.0") //
				.withEndHeel(500, 5000, EVesselTankState.MUST_BE_COLD, "10") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);
				Assert.assertTrue(startPortViolations.isEmpty());
				Assert.assertTrue(endPortViolations.isEmpty());

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(6600_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(6600_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(5 * 22.5 * 6600 * 1000, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue(), 0.0);
				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(500 * 22.5 * 10 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}

				// P&L is just heel cost and hire cost is zero
				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assert.assertEquals(-6600 * 22.5 * 5 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 0.0);
				Assert.assertEquals(500 * 22.5 * 10 * 1000.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0);

			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToEndHeel_Fail_CannotMeetMinEndHeel() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Start heel large enough to cover BOG (~6100m3) and some (or alternatively use no heel at all)...
				.withStartHeel(0, 7000.0, 22.5, "5.0") //
				// .. but not enough for the end heel
				.withEndHeel(5000, 5000, EVesselTankState.MUST_BE_COLD, "10") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);
				Assert.assertTrue(startPortViolations.isEmpty());
				Assert.assertEquals(1, endPortViolations.size());
				Assert.assertTrue(endPortViolations.contains(CapacityViolationType.MIN_HEEL));
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToEndHeel_Fail_CannotMeetMaxStartHeel() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vesselClass.getBallastAttributes().setIdleBaseRate(50);

		// Build new pricing model with known prices
		scenarioModelFinder.getCostModelFinder().getCostModel().getBaseFuelCosts().clear();
		for (BaseFuel bf : scenarioModelFinder.getFleetModelFinder().getFleetModel().getBaseFuels()) {
			BaseFuelIndex idx = scenarioModelBuilder.getPricingModelBuilder().createBaseFuelExpressionIndex("cost-" + bf.getName(), 100.0);
			scenarioModelBuilder.getCostModelBuilder().createBaseFuelCost(bf, idx);
		}

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Must end cold, not enough heel to cover BOG. Violate both start and end heels.
				.withStartHeel(6000, 6000, 22.5, "5.0") //
				// .. but not enough for the end heel
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "10") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(1, startPortViolations.size());
				Assert.assertTrue(startPortViolations.contains(CapacityViolationType.MIN_HEEL));

				Assert.assertEquals(1, endPortViolations.size());
				Assert.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(00, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue(), 0.0);
				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue(), 0.0);
				}

				// P&L is just heel cost and hire cost is zero
				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assert.assertEquals(-100.0 * 50.0 * 61.0 * 1000.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 0.0);
				Assert.assertEquals(0.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0);

			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToEndHeel_Fail_NeedToUseLessThanMinHeel() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Must end warm, but more gas than covered by BOG (~6100m3).
				.withStartHeel(6500, 6500, 22.5, "5.0") //
				// End state expects warm. Excess gas will be lost
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, null) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());

				Assert.assertEquals(1, endPortViolations.size());
				Assert.assertTrue(endPortViolations.contains(CapacityViolationType.LOST_HEEL));
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToDrydockToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Must end warm, but more gas than covered by BOG (~6100m3).
				.withStartHeel(0, 0, 0, "0") //
				// End state expects warm. Excess gas will be lost
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, null) //
				.build();

		cargoModelBuilder.makeDryDockEvent("DD", LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot dryDockPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> dryDockViolations = seq.getCapacityViolations(dryDockPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());

				Assert.assertEquals(0, dryDockViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToDrydockToEndHeel_Fail_InitialHeelLost_CannotEndCold() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Must end warm, but more gas than covered by BOG (~6100m3).
				.withStartHeel(5_000, 5_000, 22.8, "5.0") //
				// End state expects warm. Excess gas will be lost
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "10") //
				.build();

		cargoModelBuilder.makeDryDockEvent("DD", LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withDurationInDays(10) //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot dryDockPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> dryDockViolations = seq.getCapacityViolations(dryDockPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());

				Assert.assertEquals(1, dryDockViolations.size());
				Assert.assertTrue(dryDockViolations.contains(CapacityViolationType.LOST_HEEL));

				Assert.assertEquals(1, endPortViolations.size());
				Assert.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToCharterOutToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 600, 22.8, "5.0") //
				.withEndHeel(1000, 1000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 7, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withDurationInDays(10) //
				.withRequiredHeelOptions(500, 500, EVesselTankState.MUST_BE_COLD, "6") //
				.withAvailableHeelOptions(1000, 7000, 22.8, "7.5") //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot charterOutPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> charterOutViolations = seq.getCapacityViolations(charterOutPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, charterOutViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(500 * 22.8 * 5.0 * 1000, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(500 * 22.8 * 6 * 1000.0, heelRecord.getHeelRevenue(), 0.0);
					Assert.assertEquals(6100.0 * 22.8 * 7.5 * 1000.0, heelRecord.getHeelCost(), 0.0);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(1000.0 * 22.8 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}

				// P&L is just heel cost and hire cost is zero
				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assert.assertEquals(-500 * 22.8 * 5 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 0.0);
				Assert.assertEquals(-6100 * 22.8 * 7.5 * 1000 + 500 * 22.8 * 6 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(charterOutPortSlot)), 0.0);
				Assert.assertEquals(1000 * 22.8 * 10 * 1000.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0);

			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToCargoToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.build();

		Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPort("Point Fortin"), null, entity, "5", 22.5) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(4, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot loadPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot dischargePortSlot = seq.getSequenceSlots().get(2);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(3);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> loadPortViolations = seq.getCapacityViolations(loadPortSlot);
				final List<@NonNull CapacityViolationType> dischargePortViolations = seq.getCapacityViolations(dischargePortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, loadPortViolations.size());
				Assert.assertEquals(0, dischargePortViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1266_666, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1266_666, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(1266.666 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 0.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(141_165_834, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(5_243_333, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToCargoToEndHeelNoBallast_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 4, 0, 0, 0), LocalDateTime.of(2017, 4, 6, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Isle of Grain")) //
				.build();

		Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPort("Point Fortin"), null, entity, "5", 22.5) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(4, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot loadPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot dischargePortSlot = seq.getSequenceSlots().get(2);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(3);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> loadPortViolations = seq.getCapacityViolations(loadPortSlot);
				final List<@NonNull CapacityViolationType> dischargePortViolations = seq.getCapacityViolations(dischargePortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				// Assert.assertEquals(0, startPortViolations.size());
				// Assert.assertEquals(0, loadPortViolations.size());
				// Assert.assertEquals(0, dischargePortViolations.size());
				// Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1266_666, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1266_666, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(1266.666 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 0.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(141_210_834, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToCharterOutToEndHeel_FAIL_AssumeNoHeel() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 600, 22.8, "5.0") //
				.withEndHeel(1000, 1000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 7, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withDurationInDays(10) //
				.withRequiredHeelOptions(500, 500, EVesselTankState.MUST_BE_COLD, "6") //
				.withAvailableHeelOptions(1000, 1000, 22.8, "7.5") //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot charterOutPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> charterOutViolations = seq.getCapacityViolations(charterOutPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				// Need to purchase more to cover boil-off
				Assert.assertEquals(1, charterOutViolations.size());
				Assert.assertTrue(charterOutViolations.contains(CapacityViolationType.MIN_HEEL));
				// We won't meet our end obligation either...
				Assert.assertEquals(1, endPortViolations.size());
				Assert.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(5 * 22.8 * 500 * 1000, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);
					// Includes boil-off and over purchase of heel quantity. No end heel
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost(), 0.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartToCharterOutRedirectToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 600, 22.8, "5.0") //
				.withEndHeel(1000, 1000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Isle of Grain")) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 7, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPort("Point Fortin")) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")).withDurationInDays(10) //
				.withRequiredHeelOptions(500, 500, EVesselTankState.MUST_BE_COLD, "6") //
				.withAvailableHeelOptions(1000, 7000, 22.8, "7.5") //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(5, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot charterOutPortSlot1 = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot charterOutPortSlot2 = seq.getSequenceSlots().get(2);
				@NonNull
				final IPortSlot charterOutPortSlot3 = seq.getSequenceSlots().get(3);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(4);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> charterOutViolations1 = seq.getCapacityViolations(charterOutPortSlot1);
				final List<@NonNull CapacityViolationType> charterOutViolations2 = seq.getCapacityViolations(charterOutPortSlot2);
				final List<@NonNull CapacityViolationType> charterOutViolations3 = seq.getCapacityViolations(charterOutPortSlot3);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, charterOutViolations1.size());
				Assert.assertEquals(0, charterOutViolations2.size());
				Assert.assertEquals(0, charterOutViolations3.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(5 * 22.8 * 500 * 1000, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot1);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot2);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot3);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(7.5 * 22.8 * 6100 * 1000, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(10 * 22.8 * 1000 * 1000, heelRecord.getHeelRevenue(), 0.0);

				}
			});
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testGCOAfterCargo_OK() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vesselClass, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPort("Sakai"));

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.build();

		Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPort("Point Fortin"), null, entity, "5", 22.5) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		evaluateWithLSOTest(true, p -> p.getUserSettings().setGenerateCharterOuts(true), null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(5, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot loadPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot dischargePortSlot = seq.getSequenceSlots().get(2);
				@NonNull
				final IPortSlot gcoSlot = seq.getSequenceSlots().get(3);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(4);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> loadPortViolations = seq.getCapacityViolations(loadPortSlot);
				final List<@NonNull CapacityViolationType> dischargePortViolations = seq.getCapacityViolations(dischargePortSlot);
				final List<@NonNull CapacityViolationType> gcoViolations = seq.getCapacityViolations(gcoSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, loadPortViolations.size());
				Assert.assertEquals(0, dischargePortViolations.size());
				Assert.assertEquals(0, gcoViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1266_666, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1266_666, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(1266.666 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(141_165_834, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(12_874_577, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(7_568_595, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(7_568_595, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(7_568.595 * 22.5 * 7.0, heelRecord.getHeelCost() / 1000L, 1.0);
					Assert.assertEquals(7_568.595 * 22.5 * 7.0, heelRecord.getHeelRevenue() / 1000L, 1.0);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(1000 * 22.5 * 10 * 1000, heelRecord.getHeelRevenue(), 0.0);
				}
			});
		}, null);

	}

	@Test
	@Category({ MicroTest.class })
	public void testGCOAfterStart_OK_TravelThenGCO() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vesselClass, "50000", 10);
		// charterOutMarket.getAvailablePorts().add(portFinder.findPort("Sakai"));

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Sakai")) //
				.build();

		evaluateWithLSOTest(true, p -> p.getUserSettings().setGenerateCharterOuts(true), null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot gcoSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> gcoViolations = seq.getCapacityViolations(gcoSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, gcoViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(3277_500, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(3277_500, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(3277_500 * 5 * 22.5, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue(), 0.0);
				}
			});
		}, null);

	}

	@Test
	@Category({ MicroTest.class })
	public void testGCOAfterStart_OK_GCOThenTravel() throws Exception {

		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vesselClass, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPort("Point Fortin"));

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Sakai")) //
				.build();

		evaluateWithLSOTest(true, p -> p.getUserSettings().setGenerateCharterOuts(true), null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot gcoSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> gcoViolations = seq.getCapacityViolations(gcoSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, gcoViolations.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(3277_500, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(3277_500, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(3277_500 * 5 * 22.5, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(3277_500, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(3277_500, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(3277_500 * 5 * 22.5, heelRecord.getHeelCost(), 0.0);
					Assert.assertEquals(3277_500 * 5 * 22.5, heelRecord.getHeelRevenue(), 0.0);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue(), 0.0);
				}
			});
		}, null);

	}

	@Ignore
	@Test
	@Category({ MicroTest.class })
	public void testShortLoad_OK() throws Exception {
		// Test short loading works ok
		Assert.fail("Not yet implemented");
	}

	@Ignore
	@Test
	@Category({ MicroTest.class })
	public void testMinVolume_OK() throws Exception {
		// Test min volume allocator works ok
		Assert.fail("Not yet implemented");
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargoToCargo_OK() throws Exception {
		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.build();

		Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPort("Point Fortin"), null, entity, "5", 22.5) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 5, 15), portFinder.findPort("Point Fortin"), null, entity, "5", 22.5) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 6, 5), portFinder.findPort("Isle of Grain"), null, entity, "7") //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 2) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assert.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assert.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assert.assertEquals(6, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot loadPortSlot1 = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot dischargePortSlot1 = seq.getSequenceSlots().get(2);
				@NonNull
				final IPortSlot loadPortSlot2 = seq.getSequenceSlots().get(3);
				@NonNull
				final IPortSlot dischargePortSlot2 = seq.getSequenceSlots().get(4);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(5);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> loadPortViolations1 = seq.getCapacityViolations(loadPortSlot1);
				final List<@NonNull CapacityViolationType> dischargePortViolations1 = seq.getCapacityViolations(dischargePortSlot1);
				final List<@NonNull CapacityViolationType> loadPortViolations2 = seq.getCapacityViolations(loadPortSlot2);
				final List<@NonNull CapacityViolationType> dischargePortViolations2 = seq.getCapacityViolations(dischargePortSlot2);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assert.assertEquals(0, startPortViolations.size());
				Assert.assertEquals(0, loadPortViolations1.size());
				Assert.assertEquals(0, dischargePortViolations1.size());
				Assert.assertEquals(0, loadPortViolations2.size());
				Assert.assertEquals(0, dischargePortViolations2.size());
				Assert.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1266_666, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1266_666, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(1266.666 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 0.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot1);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot1);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(141_165_834, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(5_493_333, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot2);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot2);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(141_180_834, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assert.assertEquals(4_141_502, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assert.assertNotNull(heelRecord);
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assert.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assert.assertEquals(0, heelRecord.getHeelCost());
					Assert.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0);

				}
			});
		});
	}

}
