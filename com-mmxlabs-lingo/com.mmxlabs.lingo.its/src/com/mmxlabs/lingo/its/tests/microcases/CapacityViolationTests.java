/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(value = ShiroRunner.class)
public class CapacityViolationTests extends AbstractLegacyMicroTestCase {

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
				return Collections.<@NonNull Module>singletonList(new AbstractModule() {
					@Override
					protected void configure() {
						if (minMaxVolumeAllocator)
							bind(IVolumeAllocator.class).to(MinMaxUnconstrainedVolumeAllocator.class);
					}

					@Provides
					@Named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY)
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
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 10), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null).build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 6), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null).build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null)
				.withVolumeLimits(100000, 150000, VolumeUnits.M3)//
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;

					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtEndInM3());
					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5 * 22.5 * 6600 * 1000, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(-6600 * 22.5 * 5 * 1000, vpr.getProfitAndLoss(), 0.0001);
				}

				{

					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;

					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(500 * 22.5 * 10 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(500 * 22.5 * 10 * 1000.0, vpr.getProfitAndLoss(), 0.0001);
				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToEndHeel_OK_SellAtStartPrice() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(0, 8000.0, 22.5, "5.0") //
				.withEndHeel(500, 5000, EVesselTankState.MUST_BE_COLD, true) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;

					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(6600_000, heelRecord.getHeelAtEndInM3());
					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5 * 22.5 * 6600 * 1000, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(-6600 * 22.5 * 5 * 1000, vpr.getProfitAndLoss(), 0.0001);
				}

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;

					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(500 * 22.5 * 5 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(500 * 22.5 * 5 * 1000.0, vpr.getProfitAndLoss(), 0.0001);

				}
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
				// Start heel large enough to cover BOG (~6100m3) and some (or alternatively use
				// no heel at all)...
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertEquals(1, endPortViolations.size());
					Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.MIN_HEEL));
				}

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
		for (final BaseFuel bf : scenarioModelFinder.getFleetModelFinder().getFleetModel().getBaseFuels()) {
			scenarioModelBuilder.getCostModelBuilder().createOrUpdateBaseFuelCost(bf, "100");
		}

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				// Must end cold, not enough heel to cover BOG. Violate both start and end
				// heels.
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);
				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);

					Assertions.assertEquals(6000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(6000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);

					final double a = 6000 * 22.5 * 5.0 * 1000.0;
					final long b = heelValueRecord.getHeelCost();
					Assertions.assertEquals(a, b, 10.0);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost + bunkers and hire cost is zero
					final double heelCost = 6000.0 * 22.5 * 5.0 * 1000.0; // 60 days of boil-off
					final double fuel = 100 * 50 * 1000; // One day of bunkers

					Assertions.assertEquals(-heelCost - fuel, vpr.getProfitAndLoss(), 1.0);
				}

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);

					Assertions.assertEquals(1, endPortViolations.size());
					Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.MIN_HEEL));

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);

					// No heel value
					Assertions.assertEquals(0.0, vpr.getProfitAndLoss(), 0.0001);
				}

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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				boolean checkedStart = false;
				boolean checkedEnd = false;
				int idx = 0;
				for (final VoyagePlanRecord vpr : seq.getVoyagePlanRecords()) {
					if (idx == 0) {
						final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
						final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
						Assertions.assertEquals(0, startPortViolations.size());
						checkedStart = true;
					} else if (idx == 1) {
						final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
						final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
						Assertions.assertEquals(1, endPortViolations.size());
						Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.LOST_HEEL));
						checkedEnd = true;

					}
					++idx;
				}
				Assertions.assertTrue(checkedStart);
				Assertions.assertTrue(checkedEnd);
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

		cargoModelBuilder.makeDryDockEvent("DD", LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot dryDockPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> dryDockViolations = vpr.getCapacityViolations(dryDockPortSlot);
					Assertions.assertTrue(dryDockViolations.isEmpty());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());
				}
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

		cargoModelBuilder.makeDryDockEvent("DD", LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot dryDockPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> dryDockViolations = vpr.getCapacityViolations(dryDockPortSlot);
					Assertions.assertEquals(1, dryDockViolations.size());
					Assertions.assertTrue(dryDockViolations.contains(CapacityViolationType.LOST_HEEL));
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertEquals(1, endPortViolations.size());
					Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));
				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCharterOutToEndHeel_OK() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 600, 22.8, "5.0") //
				.withEndHeel(1000, 1000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 7, 0, 0, 0), LocalDateTime.of(2017, 3, 7, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(500 * 22.8 * 5.0 * 1000, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(-500 * 22.8 * 5 * 1000, vpr.getProfitAndLoss(), 0.0001);

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot charterOutPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> charterOutViolations = vpr.getCapacityViolations(charterOutPortSlot);
					Assertions.assertTrue(charterOutViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(charterOutPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(charterOutPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(500 * 22.8 * 6 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);
					Assertions.assertEquals(6100.0 * 22.8 * 7.5 * 1000.0, heelValueRecord.getHeelCost(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(-6100 * 22.8 * 7.5 * 1000 + 500 * 22.8 * 6 * 1000, vpr.getProfitAndLoss(), 0.0001);
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000.0 * 22.8 * 10.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

					// P&L is just heel cost and hire cost is zero
					Assertions.assertEquals(1000 * 22.8 * 10 * 1000.0, vpr.getProfitAndLoss(), 0.0001);
				}

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
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(1245.833 * 5.0 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(5_261_666, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

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
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				// NOTE: Old code did not check violations, just heel levels.

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);

					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCargoToEndHeelNoBallast_OK_SellAtSalesPrice() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 4, 0, 0, 0), LocalDateTime.of(2017, 4, 6, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, true) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);
				// NOTE: Old code did not check violations, only heel levels
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);

					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 7.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStartToCharterOutToEndHeel_MixedMode() throws Exception {

		portModelBuilder.setAllExistingPortsToUTC();

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 5, 8, 0, 0, 0)) //
				.withStartHeel(500, 600, 22.8, "5.0") //
				.withEndHeel(1000, 1000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5 * 22.8 * 500 * 1000, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot charterOutPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> charterOutViolations = vpr.getCapacityViolations(charterOutPortSlot);
					Assertions.assertTrue(charterOutViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(charterOutPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(charterOutPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);
					Assertions.assertEquals(1_000 * 22.8 * 7.5 * 1000.0, heelValueRecord.getHeelCost(), 0.0001);

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					// We won't meet our end obligation either...
					Assertions.assertEquals(1, endPortViolations.size());
					Assertions.assertTrue(endPortViolations.contains(CapacityViolationType.FORCED_COOLDOWN));

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
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
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.build();

		cargoModelBuilder.makeCharterOutEvent("DD", LocalDateTime.of(2017, 3, 7, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withRelocatePort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)).withDurationInDays(10) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5 * 22.8 * 500 * 1000, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);
					{
						final IPortSlot charterOutPortSlot1 = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> charterOutViolations = vpr.getCapacityViolations(charterOutPortSlot1);
						Assertions.assertTrue(charterOutViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(charterOutPortSlot1).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(charterOutPortSlot1);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(500 * 22.8 * 6.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);
					}
					{
						final IPortSlot charterOutPortSlot2 = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> charterOutViolations = vpr.getCapacityViolations(charterOutPortSlot2);
						Assertions.assertTrue(charterOutViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(charterOutPortSlot2).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						Assertions.assertEquals(500_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(charterOutPortSlot2);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
					}

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);
					{
						final IPortSlot charterOutPortSlot3 = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> charterOutViolations = vpr.getCapacityViolations(charterOutPortSlot3);
						Assertions.assertTrue(charterOutViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(charterOutPortSlot3).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(6_100_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(charterOutPortSlot3);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(7.5 * 22.8 * 6100 * 1000, heelValueRecord.getHeelCost(), 0.0001);
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
					}

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(3);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertEquals(0, endPortViolations.size());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost(), 0.0001);
					Assertions.assertEquals(10 * 22.8 * 1000 * 1000, heelValueRecord.getHeelRevenue(), 0.0001);
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
		final CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPortById(InternalDataConstants.PORT_SAKAI));

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, "10") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(17_038_196, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot gcoSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> gcoViolations = vpr.getCapacityViolations(gcoSlot);
					Assertions.assertTrue(gcoViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(gcoSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(8_124_748, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(8_124_748, heelRecord.getHeelAtEndInM3());
					
					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(gcoSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(8_124.748 * 22.5 * 7.0, heelValueRecord.getHeelCost() / 1000L, 1.0);
					Assertions.assertEquals(8_124.748 * 22.5 * 7.0, heelValueRecord.getHeelRevenue() / 1000L, 1.0);

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(3);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10 * 1000, heelValueRecord.getHeelRevenue(), 0.0001);

				}
			});
		}, null);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGCOAfterCargo_OK_UseLastPrice() throws Exception {

		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		@NonNull
		final CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPortById(InternalDataConstants.PORT_SAKAI));

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(1_000, 1_000, EVesselTankState.MUST_BE_COLD, true) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(17_038_196, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot gcoSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> gcoViolations = vpr.getCapacityViolations(gcoSlot);
					Assertions.assertTrue(gcoViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(gcoSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(8_124_748, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(8_124_748, heelRecord.getHeelAtEndInM3());
					
					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(gcoSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(8_124.748 * 22.5 * 7.0, heelValueRecord.getHeelCost() / 1000L, 1.0);
					Assertions.assertEquals(8_124.748 * 22.5 * 7.0, heelValueRecord.getHeelRevenue() / 1000L, 1.0);

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(3);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 7.0 * 1000, heelValueRecord.getHeelRevenue(), 0.0001);
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
		final CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		// charterOutMarket.getAvailablePorts().add(portFinder.findPortById(InternalDataConstants.PORT_SAKAI));

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "10") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_SAKAI)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelValueRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot gcoSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> gcoViolations = vpr.getCapacityViolations(gcoSlot);
					Assertions.assertTrue(gcoViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(gcoSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(gcoSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);
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
		final CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("CharterMarket", vessel, "50000", 10);
		charterOutMarket.getAvailablePorts().add(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN));

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 3, 8, 0, 0, 0), LocalDateTime.of(2017, 3, 8, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 6, 8, 0, 0, 0)) //
				.withStartHeel(500, 5_000, 22.5, "5.0") //
				.withEndHeel(0, 0, EVesselTankState.MUST_BE_WARM, "10") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_SAKAI)) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5000_000 * 5.0 * 22.5, heelValueRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					final IPortSlot gcoSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> gcoViolations = vpr.getCapacityViolations(gcoSlot);
					Assertions.assertTrue(gcoViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(gcoSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtStartInM3());
					// Includes boil-off
					Assertions.assertEquals(5000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(gcoSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelValueRecord.getHeelCost(), 10.0);
					Assertions.assertEquals(5000_000 * 5 * 22.5, heelValueRecord.getHeelRevenue(), 10.0);

				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(0, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(0, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(0, heelValueRecord.getHeelRevenue(), 0.0001);
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
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 3, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(120_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 5, 15), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.5) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 145_000, VolumeUnits.M3) //
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2017, 6, 5), portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
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
				final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(volumeAllocatedSequences);

				final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
				Assertions.assertNotNull(profitAndLossSequences);

				// There should only be one resource...
				Assertions.assertEquals(1, initialRawSequences.getResources().size());
				final IResource resource = initialRawSequences.getResources().get(0);

				final VolumeAllocatedSequence seq = volumeAllocatedSequences.getScheduledSequenceForResource(resource);

				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(0);

					final IPortSlot startPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> startPortViolations = vpr.getCapacityViolations(startPortSlot);
					Assertions.assertTrue(startPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(startPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1245_833, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(startPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(1245.833 * 5 * 22.5 * 1000.0, heelValueRecord.getHeelCost(), 10.0);
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(1);

					{
						final IPortSlot loadPortSlot1 = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot1);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot1).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot1);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot1 = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot1);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot1).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(5_507_500, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot1);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(2);

					{
						final IPortSlot loadPortSlot2 = vpr.getPortTimesRecord().getSlots().get(0);
						final List<@NonNull CapacityViolationType> loadPortViolations = vpr.getCapacityViolations(loadPortSlot2);
						Assertions.assertTrue(loadPortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(loadPortSlot2).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(500_000, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(145_000_000, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(loadPortSlot2);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
					{

						final IPortSlot dischargePortSlot2 = vpr.getPortTimesRecord().getSlots().get(1);
						final List<@NonNull CapacityViolationType> dischargePortViolations = vpr.getCapacityViolations(dischargePortSlot2);
						Assertions.assertTrue(dischargePortViolations.isEmpty());

						final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(dischargePortSlot2).portHeelRecord;
						Assertions.assertNotNull(heelRecord);
						Assertions.assertEquals(141_172_500, heelRecord.getHeelAtStartInM3());
						// Includes boil-off
						Assertions.assertEquals(4_126_471, heelRecord.getHeelAtEndInM3());

						final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(dischargePortSlot2);
						Assertions.assertNotNull(heelValueRecord);
						Assertions.assertEquals(0, heelValueRecord.getHeelCost());
						Assertions.assertEquals(0, heelValueRecord.getHeelRevenue());

					}
				}
				{
					final VoyagePlanRecord vpr = seq.getVoyagePlanRecords().get(3);

					final IPortSlot endPortSlot = vpr.getPortTimesRecord().getFirstSlot();
					final List<@NonNull CapacityViolationType> endPortViolations = vpr.getCapacityViolations(endPortSlot);
					Assertions.assertTrue(endPortViolations.isEmpty());

					final HeelRecord heelRecord = vpr.getHeelVolumeRecords().get(endPortSlot).portHeelRecord;
					Assertions.assertNotNull(heelRecord);
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtStartInM3());
					Assertions.assertEquals(1_000_000, heelRecord.getHeelAtEndInM3());

					final HeelValueRecord heelValueRecord = vpr.getHeelValueRecord(endPortSlot);
					Assertions.assertNotNull(heelValueRecord);
					Assertions.assertEquals(0, heelValueRecord.getHeelCost());
					Assertions.assertEquals(1000 * 22.5 * 10.0 * 1000.0, heelValueRecord.getHeelRevenue(), 0.0001);

				}
			});
		});
	}

	/**
	 * If we have a min/max load/discharge violation on a nominal cargo in the
	 * prompt, we will often remove this from the initial solution. Make sure we can
	 * place it back on the fleet. 8
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

			final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

			// Create a nominal cargo in the prompt
			final Cargo cargo1 = cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("L1", LocalDate.of(2017, 9, 2), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
					.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.M3) //
					.build() //
					.makeDESSale("D1", LocalDate.of(2017, 10, 2), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
					.withVolumeLimits(0, 140_000, VolumeUnits.M3) //
					.build() //
					.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
					.withAssignmentFlags(false, false) //
					.build();

			optimiseWithLSOTest(scenarioRunner -> {

				Assertions.assertEquals(vesselAvailability1, cargo1.getVesselAssignmentType());

				// Should be the same as the updateScenario as we have only called
				// ScenarioRunner#init()
				final Schedule schedule = scenarioRunner.getSchedule();
				Assertions.assertNotNull(schedule);

				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);
				Assertions.assertNotNull(cargoAllocation);

				boolean foundViolation = false;
				// Is there is min/max load or discharge violation?
				// At the time of writing this is a MIN_LOAD violation. It could be something
				// else along these lines if the volume allocator changes.
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
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
