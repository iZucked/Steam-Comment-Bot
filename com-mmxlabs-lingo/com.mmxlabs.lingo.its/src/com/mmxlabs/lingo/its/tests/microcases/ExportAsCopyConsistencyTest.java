/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.tests.category.RegressionTest;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

@RunWith(value = ShiroRunner.class)
public class ExportAsCopyConsistencyTest extends AbstractMicroTestCase {

	/**
	 * See BugzId: 1893
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, RegressionTest.class })
	public void testExportAsCopyDoesNotDuplicateReferenceListItems() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final BaseLegalEntity entity = commercialModelFinder.findEntity("Shipping");

		final Vessel vessel_1 = fleetModelBuilder.createVessel("Vessel-1", vesselClass);

		final VesselAvailability vesselAvailability_1 = cargoModelBuilder.makeVesselAvailability(vessel_1, entity) //
				.withStartPort(portFinder.findPort("Point Fortin")) //
				.withStartWindow(LocalDateTime.of(2015, 01, 01, 0, 0, 0), LocalDateTime.of(2015, 01, 01, 0, 0, 0)) //
				.withEndPort(portFinder.findPort("Point Fortin")) //
				.withEndWindow(LocalDateTime.of(2015, 06, 01, 0, 0, 0), LocalDateTime.of(2015, 06, 01, 0, 0, 0)) //
				.build();

		// Create a single charter out event
		@SuppressWarnings("unused")
		final CharterOutEvent charterOutEvent = cargoModelBuilder
				.makeCharterOutEvent("charter-1", LocalDateTime.of(2015, 5, 1, 0, 0, 0), LocalDateTime.of(2015, 5, 1, 0, 0, 0), portFinder.findPort("Ras Laffan")) //
				.withRelocatePort(portFinder.findPort("Isle of Grain")) //
				.withVesselAssignment(vesselAvailability_1, 1) //
				.withAllowedVessels(vessel_1) //
				.build(); //

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel copy = scenarioToOptimiserBridge.exportAsCopy(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null);

			Assert.assertEquals(1, copy.getCargoModel().getVesselEvents().size());

			final VesselEvent event = copy.getCargoModel().getVesselEvents().get(0);
			Assert.assertEquals(1, event.getAllowedVessels().size());
		});
	}

	/**
	 * See BugzId: 2091
	 * 
	 */
	@Test
	@Category({ QuickTest.class, RegressionTest.class })
	public void testExportAsCopyExportsBaseFuelCostsCorrectlyInPeriod() throws Exception {

		evaluateWithLSOTest(false, settings -> {
			OptimisationRange range = settings.getRange();
			if (range == null) {
				range = ParametersFactory.eINSTANCE.createOptimisationRange();
				settings.setRange(range);
			}
			range.setOptimiseAfter(YearMonth.of(2016, 5));
			range.setOptimiseBefore(YearMonth.of(2016, 6));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// FB2091 - will fail at this point
			final LNGScenarioModel copy = scenarioToOptimiserBridge.exportAsCopy(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null);

			final CostModel costModel = ScenarioModelUtil.getCostModel(copy);
			for (final BaseFuelCost bfc : costModel.getBaseFuelCosts()) {
				Assert.assertNotNull(bfc.getFuel());
				Assert.assertNotNull(bfc.getIndex());
			}
		}, null);
	}

	/**
	 * See BugzId: 2091 -- This bug was not present in a non-period optimisation
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ QuickTest.class, RegressionTest.class })
	public void testExportAsCopyExportsBaseFuelCostsCorrectly() throws Exception {

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel copy = scenarioToOptimiserBridge.exportAsCopy(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null);

			final CostModel costModel = ScenarioModelUtil.getCostModel(copy);
			for (final BaseFuelCost bfc : costModel.getBaseFuelCosts()) {
				Assert.assertNotNull(bfc.getFuel());
				Assert.assertNotNull(bfc.getIndex());
			}
		});
	}
}