/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class LoadDischargeCostPairingTests extends AbstractMicroTestCase {
	@Override
	protected int getThreadCount() {
		return 4;
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Disabled("Proof of concept, no need to run generally")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicCase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);
		final LoadSlot load_DES1 = cargoModelBuilder.makeDESPurchase("DES1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null).build();
		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB1", LocalDate.of(2015, 11, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB2", LocalDate.of(2015, 11, 4), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final LoadSlot load_FOB3 = cargoModelBuilder.makeFOBPurchase("FOB2", LocalDate.of(2015, 10, 4), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8).build();
		final LoadSlot load_FOB4 = cargoModelBuilder.makeFOBPurchase("FOB2", LocalDate.of(2015, 11, 4), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "6", 22.8).build();
		for (int i = 0; i < 100; ++i) {
			cargoModelBuilder.makeFOBPurchase("FOBi" + i, LocalDate.of(2015, 11, 4).plusDays(5 * i), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "6", 22.8).build();
			cargoModelBuilder.makeFOBPurchase("FOBj" + i, LocalDate.of(2015, 11, 4).plusDays(5 * i), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "6", 22.8).build();
			cargoModelBuilder.makeFOBSale("FOBsi" + i, FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 11, 4).plusDays(5 * i), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null).build();
			cargoModelBuilder.makeDESPurchase("DESpi" + i, DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 1, 5).plusDays(5 * i), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null).build();
			cargoModelBuilder.makeDESSale("DESi" + i, LocalDate.of(2016, 1, 5).plusDays(5 * i), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		}
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES2", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder.makeFOBSale("FOB1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 11, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null).build();

		evaluateWithLSOTest(true, (plan) -> {
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			@NonNull
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
			// TODO: Filter
			final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
			while (iterator.hasNext()) {
				final Constraint constraint = iterator.next();
				if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}
				if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
					iterator.remove();
				}
			}
			final LoadDischargePairValueCalculatorStep calculator = new LoadDischargePairValueCalculatorStep(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(),
					constraintAndFitnessSettings, dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
			final AtomicLong counter = new AtomicLong();
			IVesselAvailability defaultVessel = SequenceHelper.findVesselAvailability(dataTransformer.getInjector(), charterInMarket_1, -1);
			calculator.run(defaultVessel, new ProfitAndLossExtractor((load, discharge, value) -> {
				System.out.printf("%s -> %s == %,d\n", load.getId(), discharge.getId(), value);
				counter.getAndIncrement();
			}), scenarioRunner.getExecutorService(), new NullProgressMonitor());
			System.out.printf("Found %d values\n", counter.get());

		}, null);
	}

}