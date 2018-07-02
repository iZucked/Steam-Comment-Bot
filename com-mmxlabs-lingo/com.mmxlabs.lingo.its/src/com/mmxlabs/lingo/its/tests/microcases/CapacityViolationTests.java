/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(value = ShiroRunner.class)
public class CapacityViolationTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("optimisation-charter-out-generation");
	private static List<String> addedFeatures = new LinkedList<>();

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

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
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
		vessel = fleetModelFinder.findVessel("STEAM-145");

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

	@Disabled("Uncertain behaviour regarding heel violations. Unable to produce multiple valid violations on one cargo.")
	@Test
	@Tag(TestCategories.MICRO_TEST)
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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check single cargo
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assertions.assertNull(failedConstraintCheckers);

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testViableCapacityConstraintSwap() throws Exception {
		initCommonData1();
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 6), portFinder.findPort("Point Fortin"), null, entity, "5", null).build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check single cargo
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assertions.assertNull(failedConstraintCheckers);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// try {
			// MicroCaseUtils.storeToFile(optimiserScenario,"X");
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Check single cargo
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			// Evaluate modified Sequence in the context of the first.
			final ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, loadSlot, dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is a singular constraint checker
			Assertions.assertEquals(1, failedConstraintCheckers.size());
			// Check that it is CapacityEvaluatedStateChecker
			final IEvaluatedStateConstraintChecker con = failedConstraintCheckers.get(0);
			Assertions.assertTrue(con instanceof CapacityEvaluatedStateChecker);
			// Cast to CESC
			final CapacityEvaluatedStateChecker castedCon = (CapacityEvaluatedStateChecker) con;
			// Check that there was a single initial violation
			Assertions.assertEquals(1, castedCon.getInitialSoftViolations());

			final Set<IPortSlot> slots = castedCon.getCurrentViolatedSlots();
			final long softViolations = castedCon.getCurrentSoftViolations();
			// Check that there is only a singular violated slot and no soft violations.
			Assertions.assertEquals(0, softViolations);
			Assertions.assertEquals(1, slots.size());
			// Check that the violation is a MIN_DISCHARGE
			Assertions.assertEquals(CapacityViolationType.MIN_DISCHARGE, castedCon.getTriggeredViolations().get(0));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(0, 8000.0, 22.5, "5.0") //
				.withEndHeel(500, 5000, EVesselTankState.MUST_BE_COLD, "10") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);
				Assertions.assertTrue(startPortViolations.isEmpty());
				Assertions.assertTrue(endPortViolations.isEmpty());

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5 * 22.5 * 6600 * 1000, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue(), 0.0001);
				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(500 * 22.5 * 10 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}

				// P&L is just heel cost and hire cost is zero
				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertEquals(-6600 * 22.5 * 5 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 0.0001);
				Assertions.assertEquals(500 * 22.5 * 10 * 1000.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0001);

			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToEndHeel_Fail_CannotMeetMinEndHeel() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);
				Assertions.assertTrue(startPortViolations.isEmpty());
				Assertions.assertEquals(1, endPortViolations.size());
				Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.MIN_HEEL));
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToEndHeel_Fail_CannotMeetMaxStartHeel_MixedMode() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vessel.getBallastAttributes().setIdleNBORate(100);
		vessel.getBallastAttributes().setIdleBaseRate(50);

		// Build new pricing model with known prices
		scenarioModelFinder.getCostModelFinder().getCostModel().getBaseFuelCosts().clear();
		for (BaseFuel bf : scenarioModelFinder.getFleetModelFinder().getFleetModel().getBaseFuels()) {
			scenarioModelBuilder.getCostModelBuilder().createOrUpdateBaseFuelCost(bf, "100");
		}

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());

				Assertions.assertEquals(1, endPortViolations.size());
				Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.MIN_HEEL));

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(6000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(6000_000, heelRecord.getHeelAtEndInM3());
					double a = 6000 * 22.5 * 5.0 * 1000.0;
					long b = heelRecord.getHeelCost();
					Assertions.assertEquals(a, b, 10.0);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue(), 0.0001);
				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue(), 0.0001);
				}

				// P&L is just heel cost + bunkers and hire cost is zero
				double heelCost = 6000.0 * 22.5 * 5.0 * 1000.0; // 60 days of boil-off
				double fuel = 100 * 50 * 1000; // One day of bunkers

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertEquals(-heelCost - fuel, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 1.0);
				Assertions.assertEquals(0.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0001);

			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToEndHeel_Fail_NeedToUseLessThanMinHeel() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(2, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(1);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());

				Assertions.assertEquals(1, endPortViolations.size());
				Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.LOST_HEEL));
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToDrydockToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot dryDockPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> dryDockViolations = seq.getCapacityViolations(dryDockPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());

				Assertions.assertEquals(0, dryDockViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToDrydockToEndHeel_Fail_InitialHeelLost_CannotEndCold() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot dryDockPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> dryDockViolations = seq.getCapacityViolations(dryDockPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());

				Assertions.assertEquals(1, dryDockViolations.size());
				Assertions.assertTrue(dryDockViolations.contains(CapacityViolationType.LOST_HEEL));

				Assertions.assertEquals(1, endPortViolations.size());
				Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCharterOutToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot charterOutPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> charterOutViolations = seq.getCapacityViolations(charterOutPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, charterOutViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(500 * 22.8 * 5.0 * 1000, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(500 * 22.8 * 6 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);
					Assertions.assertEquals(6100.0 * 22.8 * 7.5 * 1000.0, heelRecord.getHeelCost(), 0.0001);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(1000.0 * 22.8 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}

				// P&L is just heel cost and hire cost is zero
				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertEquals(-500 * 22.8 * 5 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(startPortSlot)), 0.0001);
				Assertions.assertEquals(-6100 * 22.8 * 7.5 * 1000 + 500 * 22.8 * 6 * 1000, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(charterOutPortSlot)), 0.0001);
				Assertions.assertEquals(1000 * 22.8 * 10 * 1000.0, profitAndLossSequences.getVoyagePlanGroupValue(seq.getVoyagePlan(endPortSlot)), 0.0001);

			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCargoToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(4, seq.getSequenceSlots().size());
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

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, loadPortViolations.size());
				Assertions.assertEquals(0, dischargePortViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(1245.833 * 5.0 * 22.5 * 1000.0, heelRecord.getHeelCost(), 10.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(141_173_334, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(5_268_333, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCargoToEndHeelNoBallast_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(4, seq.getSequenceSlots().size());
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

				// Assertions.assertEquals(0, startPortViolations.size());
				// Assertions.assertEquals(0, loadPortViolations.size());
				// Assertions.assertEquals(0, dischargePortViolations.size());
				// Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 10.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(141_173_334, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCharterOutToEndHeel_MixedMode() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot charterOutPortSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> charterOutViolations = seq.getCapacityViolations(charterOutPortSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, charterOutViolations.size());

				// We won't meet our end obligation either...
				Assertions.assertEquals(1, endPortViolations.size());
				Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5 * 22.8 * 500 * 1000, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(1_000 * 22.8 * 7.5 * 1000.0, heelRecord.getHeelCost(), 0.0001);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCharterOutRedirectToEndHeel_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(5, seq.getSequenceSlots().size());
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

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, charterOutViolations1.size());
				Assertions.assertEquals(0, charterOutViolations2.size());
				Assertions.assertEquals(0, charterOutViolations3.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5 * 22.8 * 500 * 1000, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot1);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot2);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(charterOutPortSlot3);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(7.5 * 22.8 * 6100 * 1000, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(10 * 22.8 * 1000 * 1000, heelRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGCOAfterCargo_OK() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPort("Sakai"));

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(5, seq.getSequenceSlots().size());
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

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, loadPortViolations.size());
				Assertions.assertEquals(0, dischargePortViolations.size());
				Assertions.assertEquals(0, gcoViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(141_173_334, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(12_874_577, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(7_568_595, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(7_568_595, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(7_568.595 * 22.5 * 7.0, heelRecord.getHeelCost() / 1000L, 1.0);
					Assertions.assertEquals(7_568.595 * 22.5 * 7.0, heelRecord.getHeelRevenue() / 1000L, 1.0);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10 * 1000, heelRecord.getHeelRevenue(), 0.0001);
				}
			});
		}, null);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGCOAfterStart_OK_TravelThenGCO() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		// charterOutMarket.getAvailablePorts().add(portFinder.findPort("Sakai"));

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot gcoSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> gcoViolations = seq.getCapacityViolations(gcoSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, gcoViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue(), 0.0001);
				}
			});
		}, null);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGCOAfterStart_OK_GCOThenTravel() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		@NonNull
		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPort("Point Fortin"));

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(3, seq.getSequenceSlots().size());
				@NonNull
				final IPortSlot startPortSlot = seq.getSequenceSlots().get(0);
				@NonNull
				final IPortSlot gcoSlot = seq.getSequenceSlots().get(1);
				@NonNull
				final IPortSlot endPortSlot = seq.getSequenceSlots().get(2);

				final List<@NonNull CapacityViolationType> startPortViolations = seq.getCapacityViolations(startPortSlot);
				final List<@NonNull CapacityViolationType> gcoViolations = seq.getCapacityViolations(gcoSlot);
				final List<@NonNull CapacityViolationType> endPortViolations = seq.getCapacityViolations(endPortSlot);

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, gcoViolations.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5000_000 * 5.0 * 22.5, heelRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}

				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(gcoSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelRecord.getHeelRevenue(), 10.0);
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue(), 0.0001);
				}
			});
		}, null);

	}

	@Disabled
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShortLoad_OK() throws Exception {
		// Test short loading works ok
		Assertions.fail("Not yet implemented");
	}

	@Disabled
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMinVolume_OK() throws Exception {
		// Test min volume allocator works ok
		Assertions.fail("Not yet implemented");
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoToCargo_OK() throws Exception {
		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

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
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, (injector, annotatedSolution) -> {
				@NonNull
				final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

				@NonNull
				final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// Only expect start and end slot here
				Assertions.assertEquals(6, seq.getSequenceSlots().size());
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

				Assertions.assertEquals(0, startPortViolations.size());
				Assertions.assertEquals(0, loadPortViolations1.size());
				Assertions.assertEquals(0, dischargePortViolations1.size());
				Assertions.assertEquals(0, loadPortViolations2.size());
				Assertions.assertEquals(0, dischargePortViolations2.size());
				Assertions.assertEquals(0, endPortViolations.size());
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(startPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelRecord.getHeelCost(), 10.0);

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot1);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot1);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(141_173_334, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(5_514_166, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());
				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(loadPortSlot2);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(dischargePortSlot2);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(141_173_334, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(4_141_502, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(0, heelRecord.getHeelRevenue());

				}
				{
					final HeelValueRecord heelRecord = seq.getPortHeelRecord(endPortSlot);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());
					Assertions.assertEquals(0, heelRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	/**
	 * If we have a min/max load/discharge violation on a nominal cargo in the prompt, we will often remove this from the initial solution. Make sure we can place it back on the fleet. 8
	 * 
	 * @throws Exception
	 */

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNominalCargoViolationCanMoveToVessel() throws Exception {
		LicenseFeatures.addFeatureEnablements("no-nominal-in-prompt");
		try {

			scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 9, 1), LocalDate.of(2018, 1, 1));
			// Create the required basic elements
			vessel = fleetModelFinder.findVessel("STEAM-145");

			vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
					.withStartWindow(LocalDateTime.of(2017, 9, 1, 0, 0, 0)) //
					.withEndWindow(LocalDateTime.of(2018, 1, 1, 0, 0, 0)) //
					.withStartHeel(0, 10_000, 22.5, "0") //
					.withEndHeel(0, 10_000, EVesselTankState.EITHER, null) //
					.build();

			final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

			// Create a nominal cargo in the prompt
			final Cargo cargo1 = cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("L1", LocalDate.of(2017, 9, 2), portFinder.findPort("Point Fortin"), null, entity, "5") //
					.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.M3) //
					.build() //
					.makeDESSale("D1", LocalDate.of(2017, 10, 2), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
					.withVolumeLimits(0, 140_000, VolumeUnits.M3) //
					.build() //
					.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
					.withAssignmentFlags(false, false) //
					.build();

			optimiseWithLSOTest(scenarioRunner -> {

				Assertions.assertEquals(vesselAvailability1, cargo1.getVesselAssignmentType());

				// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
				final Schedule schedule = scenarioRunner.getSchedule();
				Assertions.assertNotNull(schedule);

				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);
				Assertions.assertNotNull(cargoAllocation);

				boolean foundViolation = false;
				// Is there is min/max load or discharge violation?
				// At the time of writing this is a MIN_LOAD violation. It could be something else along these lines if the volume allocator changes.
				for (SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
						foundViolation |= allocation.getSlotVisit().getViolations().containsKey(com.mmxlabs.models.lng.schedule.CapacityViolationType.MIN_LOAD);
					}

				}
				Assertions.assertTrue(foundViolation);
			});

		} finally {
			LicenseFeatures.removeFeatureEnablements("no-nominal-in-prompt");
		}
	}

}
