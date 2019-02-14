/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.moves.AbstractMoveHandlerTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
@RunWith(value = ShiroRunner.class)
public class SpotDESPurchaseTimezoneTests extends AbstractMoveHandlerTest {

	@Test
	@Category({ MicroTest.class })
	public void testSwapCargoLoadWithUnusedLoad() throws Exception {

		// Make a shipped cargo to define the time range.
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("100000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2018, 06, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2018, 07, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(true, false) //
				.build();

		final DischargeSlot discharge = cargoModelBuilder //
				.makeDESSale("Discharge", LocalDate.of(2018, 07, 1), portFinder.findPort("Dahej"), null, entity, "5") //
				.withWindowSize(1, TimePeriod.MONTHS) //
//				.withWindowSize(31, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.build();

		SpotMarket market = spotMarketsModelBuilder.makeDESPurchaseMarket("INDIA MKT", Collections.<APortSet<Port>> singletonList(portFinder.findPort("Dahej")), entity, "8", 22.3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.withVolumeLimits(0, 4_000_000, VolumeUnits.MMBTU) //
				.build();

		runTest((injector, scenarioRunner) -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ModelEntityMap modelEntityMap = scenarioToOptimiserBridge.getDataTransformer().getModelEntityMap();

			ISpotMarket o_market = modelEntityMap.getOptimiserObjectNullChecked(market, ISpotMarket.class);

			ISpotMarketSlotsProvider spotMarketsSlotsProvider = injector.getInstance(ISpotMarketSlotsProvider.class);
			List<@NonNull IPortSlot> marketSlots = spotMarketsSlotsProvider.getPortSlotsFor(o_market);

			IPortSlot spotM = null;
			IPortSlot spotMminus1 = null;
			for (IPortSlot marketSlot : marketSlots) {
				String key = spotMarketsSlotsProvider.getMarketDateKey(marketSlot);
				if ("2018-06".equals(key)) {
					spotMminus1 = marketSlot;
				} else if ("2018-07".equals(key)) {
					spotM = marketSlot;

				}
			}

			Assert.assertNotNull(spotMminus1);
			Assert.assertNotNull(spotM);

			LoadSlot loadM = modelEntityMap.getModelObjectNullChecked(spotM, LoadSlot.class);
			LoadSlot loadMminus1 = modelEntityMap.getModelObjectNullChecked(spotMminus1, LoadSlot.class);

			ISequences sequencesM = SequenceHelper.createJustFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), loadM, discharge);
			ISequences sequencesMinus1 = SequenceHelper.createJustFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), loadMminus1, discharge);

			List<IConstraintChecker> a = MicroTestUtils.validateConstraintCheckersWithoutNewUnit(injector, sequencesM);
			Assert.assertNull(a);

			List<IConstraintChecker> b = MicroTestUtils.validateConstraintCheckersWithoutNewUnit(injector, sequencesMinus1);
			Assert.assertNotNull(b);
			int ii = 0;

		});
	}
}