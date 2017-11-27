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
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
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
public class CooldownTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testStartEventCooldown() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setWarmingTime(0);
		vesselClass.setCoolingVolume(1000);
		Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		BaseFuelIndex idx = scenarioModelBuilder.getPricingModelBuilder().createBaseFuelExpressionIndex("cost", 0.0);

		costModelBuilder.createBaseFuelCost(vesselClass.getBaseFuel(), idx);
		costModelBuilder.setAllExistingCooldownCosts(false, "5");

		Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 2), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Sequence sequence = optimiserScenario.getScheduleModel().getSchedule().getSequences().get(0);
			StartEvent event1 = (StartEvent) sequence.getEvents().get(0);

			// = 1000m3 * 22.8 * $5.0
			Assert.assertEquals(-114_000, ScheduleModelKPIUtils.getElementPNL(event1));
			Assert.assertEquals(114_000, ScheduleModelKPIUtils.calculateEventShippingCost(event1, false, true, ShippingCostType.ALL).longValue());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testStartEventCooldownLumpsum() throws Exception {

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setWarmingTime(0);
		Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		@NonNull
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 1, 0, 0, 0), LocalDateTime.of(2017, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.withStartHeel(0, 0, 22.8, "0") //
				.withEndHeel(50, 50, EVesselTankState.MUST_BE_COLD, null)//
				.build();

		BaseFuelIndex idx = scenarioModelBuilder.getPricingModelBuilder().createBaseFuelExpressionIndex("cost", 0.0);

		costModelBuilder.createBaseFuelCost(vesselClass.getBaseFuel(), idx);
		costModelBuilder.setAllExistingCooldownCosts(true, "75000");

		Cargo cargo = cargoModelBuilder.makeCargo() //
				//
				.makeFOBPurchase("L2", LocalDate.of(2017, 12, 2), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //
				//
				.makeDESSale("D2", LocalDate.of(2017, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Sequence sequence = optimiserScenario.getScheduleModel().getSchedule().getSequences().get(0);
			StartEvent event1 = (StartEvent) sequence.getEvents().get(0);

			Assert.assertEquals(-75_000, ScheduleModelKPIUtils.getElementPNL(event1));
			Assert.assertEquals(75_000, ScheduleModelKPIUtils.calculateEventShippingCost(event1, false, true, ShippingCostType.ALL).longValue());

		});
	}
}
