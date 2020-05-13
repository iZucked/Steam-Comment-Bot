/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Injector;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.data.distances.DataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCore;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_OPTIMISATION_NO_NOMINALS_IN_PROMPT, KnownFeatures.FEATURE_OPTIMISATION_ACTIONSET })
public class ThirdPartyEntityTests extends AbstractMicroTestCase {

	// Which scenario data to import
	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final IScenarioDataProvider scenarioDataProvider = super.importReferenceData();
		//
		 updateDistanceData(scenarioDataProvider, DataConstants.DISTANCES_LATEST_JSON);
		 updatePortsData(scenarioDataProvider, DataConstants.PORTS_LATEST_JSON);

		return scenarioDataProvider;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNonShippedCargo() throws Exception {

		LegalEntity entity = commercialModelBuilder.createEntity("Third");
		entity.setThirdParty(true);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPort("Futtsu"), null, entity, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPort("Futtsu"), null, entity, "7") //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());

		});
	}

}