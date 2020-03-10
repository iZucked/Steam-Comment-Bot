/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.ExpectedLongValue;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;

/**
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class RouteCostPricingTests extends AbstractMicroTestCase {

	/**
	 * Test the port cost implicit/explicit pricing. Testing order does not matter. In this test generic case then specific
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSuezRouteCostOrder1() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue genericCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue specificCost = ExpectedLongValue.forFixedCost(3_000);
		final ExpectedLongValue specific2Cost = ExpectedLongValue.forFixedCost(4_000);
		final ExpectedLongValue scntCost = ExpectedLongValue.forFixedCost(5_000);
		final ExpectedLongValue unexpectedCost = ExpectedLongValue.forFixedCost(8_000);

		// Disable SUEZ for vessel 1&2
		vessel1.setScnt(0);
		vessel2.setScnt(0);
		// Enable - but dummy data for simple cost model
		vessel3.setScnt(1);
		vessel4.setScnt(1);

		scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(scntCost.input());

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, genericCost.input(), unexpectedCost.input());

		// Set specific rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel1, RouteOption.SUEZ, specificCost.input(), unexpectedCost.input());
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel4, RouteOption.SUEZ, specific2Cost.input(), unexpectedCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);
			final IVessel oVessel2 = modelEntityMap.getOptimiserObjectNullChecked(vessel2, IVessel.class);
			final IVessel oVessel3 = modelEntityMap.getOptimiserObjectNullChecked(vessel3, IVessel.class);
			final IVessel oVessel4 = modelEntityMap.getOptimiserObjectNullChecked(vessel4, IVessel.class);

			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(genericCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel4, 0, CostType.Laden)); // Specific override SCNT
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSuezRouteCostOrder2() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue genericCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue specificCost = ExpectedLongValue.forFixedCost(3_000);
		final ExpectedLongValue specific2Cost = ExpectedLongValue.forFixedCost(4_000);
		final ExpectedLongValue scntCost = ExpectedLongValue.forFixedCost(5_000);
		final ExpectedLongValue unexpectedCost = ExpectedLongValue.forFixedCost(8_000);

		// Disable SUEZ for vessel 1&2
		vessel1.setScnt(0);
		vessel2.setScnt(0);
		// Enable - but dummy data for simple cost model
		vessel3.setScnt(1);
		vessel4.setScnt(1);

		scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(scntCost.input());

		// Set specific rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel1, RouteOption.SUEZ, specificCost.input(), unexpectedCost.input());
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel4, RouteOption.SUEZ, specific2Cost.input(), unexpectedCost.input());

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, genericCost.input(), unexpectedCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);
			final IVessel oVessel2 = modelEntityMap.getOptimiserObjectNullChecked(vessel2, IVessel.class);
			final IVessel oVessel3 = modelEntityMap.getOptimiserObjectNullChecked(vessel3, IVessel.class);
			final IVessel oVessel4 = modelEntityMap.getOptimiserObjectNullChecked(vessel4, IVessel.class);

			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(genericCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oVessel4, 0, CostType.Laden)); // Specific override SCNT
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPanamaRouteCostOrder1() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue genericCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue specificCost = ExpectedLongValue.forFixedCost(3_000);
		final ExpectedLongValue specific2Cost = ExpectedLongValue.forFixedCost(4_000);
		final ExpectedLongValue panamaCost = ExpectedLongValue.forFixedCost(5_000);
		final ExpectedLongValue unexpectedCost = ExpectedLongValue.forFixedCost(8_000);

		scenarioModelBuilder.getCostModelBuilder().createSimplePanamaCanalTariff(panamaCost.input());

		// Set specific rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel1, RouteOption.PANAMA, specificCost.input(), unexpectedCost.input());
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel4, RouteOption.PANAMA, specific2Cost.input(), unexpectedCost.input());

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, genericCost.input(), unexpectedCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);
			final IVessel oVessel2 = modelEntityMap.getOptimiserObjectNullChecked(vessel2, IVessel.class);
			final IVessel oVessel3 = modelEntityMap.getOptimiserObjectNullChecked(vessel3, IVessel.class);
			final IVessel oVessel4 = modelEntityMap.getOptimiserObjectNullChecked(vessel4, IVessel.class);

			// Never get generic panama cost (unless there is no tariff formula at all..)
			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel4, 0, CostType.Laden)); // Specific override SCNT
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPanamaRouteCostOrder2() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue genericCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue specificCost = ExpectedLongValue.forFixedCost(3_000);
		final ExpectedLongValue specific2Cost = ExpectedLongValue.forFixedCost(4_000);
		final ExpectedLongValue panamaCost = ExpectedLongValue.forFixedCost(5_000);
		final ExpectedLongValue unexpectedCost = ExpectedLongValue.forFixedCost(8_000);

		scenarioModelBuilder.getCostModelBuilder().createSimplePanamaCanalTariff(panamaCost.input());

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, genericCost.input(), unexpectedCost.input());

		// Set specific rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel1, RouteOption.PANAMA, specificCost.input(), unexpectedCost.input());
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel4, RouteOption.PANAMA, specific2Cost.input(), unexpectedCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);
			final IVessel oVessel2 = modelEntityMap.getOptimiserObjectNullChecked(vessel2, IVessel.class);
			final IVessel oVessel3 = modelEntityMap.getOptimiserObjectNullChecked(vessel3, IVessel.class);
			final IVessel oVessel4 = modelEntityMap.getOptimiserObjectNullChecked(vessel4, IVessel.class);

			// Never get generic panama cost (unless there is no tariff formula at all..)
			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oVessel4, 0, CostType.Laden)); // Specific override SCNT
		});
	}
}