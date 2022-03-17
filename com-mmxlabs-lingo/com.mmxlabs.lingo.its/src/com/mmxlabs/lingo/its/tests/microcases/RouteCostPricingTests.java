/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.ExpectedLongValue;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.validation.ValidationTestUtil;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.SlotNameUniquenessConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.validation.SuezTariffConstraint;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
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

		final Port port1 = portModelBuilder.createPort("FromPort", "L_FROM", "UTC", 0, 0);
		final Port port2 = portModelBuilder.createPort("ToPort", "L_TO", "UTC", 0, 0);

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

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(genericCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Specific override SCNT
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

		final Port port1 = portModelBuilder.createPort("FromPort", "L_FROM", "UTC", 0, 0);
		final Port port2 = portModelBuilder.createPort("ToPort", "L_TO", "UTC", 0, 0);

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

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(genericCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Specific override SCNT
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

		final Port port1 = portModelBuilder.createPort("FromPort", "L_FROM", "UTC", 0, 0);
		final Port port2 = portModelBuilder.createPort("ToPort", "L_TO", "UTC", 0, 0);

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

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			// Never get generic panama cost (unless there is no tariff formula at all..)
			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Specific override SCNT
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

		final Port port1 = portModelBuilder.createPort("FromPort", "L_FROM", "UTC", 0, 0);
		final Port port2 = portModelBuilder.createPort("ToPort", "L_TO", "UTC", 0, 0);

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

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			// Never get generic panama cost (unless there is no tariff formula at all..)
			Assertions.assertEquals(specificCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel1, 0, CostType.Laden)); // Specific cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(panamaCost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel3, 0, CostType.Laden)); // SCNT override generic
			Assertions.assertEquals(specific2Cost.output(), routeCostProvider.getRouteCost(ERouteOption.PANAMA, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Specific override SCNT
		});
	}

	/**
	 * Test suez route rebates (low level)
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSuezRebates() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		final Port port1 = portModelBuilder.createPort("Port1", "L_P1", "UTC", 0, 0);
		final Port port2 = portModelBuilder.createPort("Port2", "L_P2", "UTC", 0, 0);
		final Port port3 = portModelBuilder.createPort("Port3", "L_P3", "UTC", 0, 0);
		final Port port4 = portModelBuilder.createPort("Port4", "L_P4", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue ladenCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue ballastCost = ExpectedLongValue.forFixedCost(8_000);

		final ExpectedLongValue scntCost = ExpectedLongValue.forFixedCost(5_000);

		// Disable SUEZ scnt tariff for vessel 1&2
		vessel1.setScnt(0);
		vessel2.setScnt(0);
		// Enable - but dummy data for simple cost model
		vessel3.setScnt(1);
		vessel4.setScnt(1);

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(scntCost.input());

		// This should also apply to -> from
		final SuezCanalRouteRebate rebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		rebate.getFrom().add(port1);
		rebate.getTo().add(port2);
		rebate.setRebate(0.25);

		suezTariff.getRouteRebates().add(rebate);

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, ladenCost.input(), ballastCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);
			final IVessel oVessel2 = modelEntityMap.getOptimiserObjectNullChecked(vessel2, IVessel.class);
			final IVessel oVessel3 = modelEntityMap.getOptimiserObjectNullChecked(vessel3, IVessel.class);
			final IVessel oVessel4 = modelEntityMap.getOptimiserObjectNullChecked(vessel4, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);
			final IPort oPort3 = modelEntityMap.getOptimiserObjectNullChecked(port3, IPort.class);
			final IPort oPort4 = modelEntityMap.getOptimiserObjectNullChecked(port4, IPort.class);

			// Is 25% of the cost taken off the original value? (Input is treated as bi-directional and applies to both laden and ballast voyages)
			// Test the fixed cost inputs
			Assertions.assertEquals(ladenCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Ballast)); // Generic cost
			Assertions.assertEquals(ladenCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel2, 0, CostType.Ballast)); // Generic cost

			// Test the SCNT based inputs
			Assertions.assertEquals(scntCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Ballast)); // Generic cost
			Assertions.assertEquals(scntCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(scntCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel4, 0, CostType.Ballast)); // Generic cost

			// Verify other ports are not included
			// One related port
			Assertions.assertEquals(ladenCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort2, oPort3, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort2, oPort3, oVessel2, 0, CostType.Ballast)); // Generic cost
			Assertions.assertEquals(ladenCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort3, oPort2, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort3, oPort2, oVessel2, 0, CostType.Ballast)); // Generic cost
			// No related ports
			Assertions.assertEquals(ladenCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort4, oPort3, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort4, oPort3, oVessel2, 0, CostType.Ballast)); // Generic cost
			Assertions.assertEquals(ladenCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort3, oPort4, oVessel2, 0, CostType.Laden)); // Generic cost
			Assertions.assertEquals(ballastCost.output(), routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort3, oPort4, oVessel2, 0, CostType.Ballast)); // Generic cost
		});
	}

	/**
	 * Test suez route rebates (low level). Definition is US -> Group A, US -> Group B and US -> Group C. Make sure the same ports can work on the US side in multiple entries
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSuezRebatesSameLHS() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel1, null);
		final Vessel vessel3 = fleetModelBuilder.createVesselFrom("vessel3", vessel1, null);
		final Vessel vessel4 = fleetModelBuilder.createVesselFrom("vessel4", vessel1, null);
		final VesselGroup vesselGroup = scenarioModelBuilder.getFleetModelBuilder().makeVesselGroup("all-vessels", scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels());

		final Port usPort = portModelBuilder.createPort("usPort", "L_usPort", "UTC", 0, 0);
		final Port groupAPort = portModelBuilder.createPort("groupAPort", "L_groupAPort", "UTC", 0, 0);
		final Port groupBPort = portModelBuilder.createPort("groupBPort", "L_groupBPort", "UTC", 0, 0);
		final Port groupCPort = portModelBuilder.createPort("groupCPort", "L_groupCPort", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final ExpectedLongValue ladenCost = ExpectedLongValue.forFixedCost(1_000);
		final ExpectedLongValue ballastCost = ExpectedLongValue.forFixedCost(8_000);

		final ExpectedLongValue scntCost = ExpectedLongValue.forFixedCost(5_000);

		// Disable SUEZ scnt tariff for vessel 1
		vessel1.setScnt(0);

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(scntCost.input());

		// This should also apply to -> from
		final SuezCanalRouteRebate groupARebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		{
			groupARebate.getFrom().add(usPort);
			groupARebate.getTo().add(groupAPort);
			groupARebate.setRebate(0.25);

			suezTariff.getRouteRebates().add(groupARebate);
		}
		final SuezCanalRouteRebate groupBRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupBRebate.getFrom().add(usPort);
			groupBRebate.getTo().add(groupBPort);
			groupBRebate.setRebate(0.5);

			suezTariff.getRouteRebates().add(groupBRebate);
		}
		final SuezCanalRouteRebate groupCRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupCRebate.getFrom().add(usPort);
			groupCRebate.getTo().add(groupCPort);
			groupCRebate.setRebate(0.75);

			suezTariff.getRouteRebates().add(groupCRebate);
		}

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vesselGroup, RouteOption.SUEZ, ladenCost.input(), ballastCost.input());

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IRouteCostProvider routeCostProvider = dataTransformer.getInjector().getInstance(IRouteCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel1 = modelEntityMap.getOptimiserObjectNullChecked(vessel1, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(usPort, IPort.class);

			// Group A 25% rebate
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(groupAPort, IPort.class);
			Assertions.assertEquals(ladenCost.output() * 3 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort2, oVessel1, 0, CostType.Laden)); // Generic cost

			// Group B 50% rebate
			final IPort oPort3 = modelEntityMap.getOptimiserObjectNullChecked(groupBPort, IPort.class);
			Assertions.assertEquals(ladenCost.output() * 2 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort3, oVessel1, 0, CostType.Laden)); // Generic cost

			// Group C 75% rebate
			final IPort oPort4 = modelEntityMap.getOptimiserObjectNullChecked(groupCPort, IPort.class);
			Assertions.assertEquals(ladenCost.output() * 1 / 4, routeCostProvider.getRouteCost(ERouteOption.SUEZ, oPort1, oPort4, oVessel1, 0, CostType.Laden)); // Generic cost

		});
	}

	/**
	 * Test suez route rebates passes through to an exported ballast leg. This is a simple test using a single ballast leg and fixed canal cost override object.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSuezRebatesExport() throws Exception {

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		// Disable SUEZ scnt tariff for vessel 1
		vessel1.setScnt(0);

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(5_000);

		// This should also apply to -> from
		final SuezCanalRouteRebate rebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		rebate.getFrom().add(portFinder.findPortById(InternalDataConstants.PORT_QALHAT));
		rebate.getTo().add(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN));

		// 25% Discount
		rebate.setRebate(0.25);

		suezTariff.getRouteRebates().add(rebate);

		// Generic flat rate
		scenarioModelBuilder.getCostModelBuilder().createRouteCost(vessel1, RouteOption.SUEZ, 8_000, 8_000);

		VesselAvailability charter1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN)) //
				.withEndPort(portFinder.findPortById(InternalDataConstants.PORT_QALHAT)) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {
		});

		Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		Assertions.assertEquals(1, schedule.getSequences().size());

		Sequence seq = schedule.getSequences().get(0);

		Journey journey = (Journey) seq.getEvents().get(1);

		Assertions.assertEquals(RouteOption.SUEZ, journey.getRouteOption());
		Assertions.assertEquals(8_000 * (1.0 - rebate.getRebate()), journey.getToll());

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testValidationPasses() {

		final Port usPort = portModelBuilder.createPort("usPort", "L_usPort", "UTC", 0, 0);

		final Port groupAPort = portModelBuilder.createPort("groupAPort", "L_groupAPort", "UTC", 0, 0);
		final Port groupBPort = portModelBuilder.createPort("groupBPort", "L_groupBPort", "UTC", 0, 0);
		final Port groupCPort = portModelBuilder.createPort("groupCPort", "L_groupCPort", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(1_000);

		// This should also apply to -> from
		final SuezCanalRouteRebate groupARebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		{
			groupARebate.getFrom().add(usPort);
			groupARebate.getTo().add(groupAPort);
			groupARebate.setRebate(0.25);

			suezTariff.getRouteRebates().add(groupARebate);
		}
		final SuezCanalRouteRebate groupBRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupBRebate.getFrom().add(usPort);
			groupBRebate.getTo().add(groupBPort);
			groupBRebate.setRebate(0.5);

			suezTariff.getRouteRebates().add(groupBRebate);
		}
		final SuezCanalRouteRebate groupCRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupCRebate.getFrom().add(usPort);
			groupCRebate.getTo().add(groupCPort);
			groupCRebate.setRebate(0.75);

			suezTariff.getRouteRebates().add(groupCRebate);
		}

		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);
		// Check there are no relevant validation issues
		{
			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_EMPTY_PORTS);
			Assertions.assertTrue(children.isEmpty());
		}
		{
			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_DUPLICATE_ENTRY);
			Assertions.assertTrue(children.isEmpty());
		}
		{
			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_PORT_ON_BOTH_SIDES);
			Assertions.assertTrue(children.isEmpty());
		}

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testValidationFailsPortBothSides() {

		final Port usPort = portModelBuilder.createPort("usPort", "L_usPort", "UTC", 0, 0);

		final Port groupAPort = portModelBuilder.createPort("groupAPort", "L_groupAPort", "UTC", 0, 0);
		final Port groupBPort = portModelBuilder.createPort("groupBPort", "L_groupBPort", "UTC", 0, 0);
		final Port groupCPort = portModelBuilder.createPort("groupCPort", "L_groupCPort", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(1_000);

		// This should also apply to -> from
		final SuezCanalRouteRebate groupARebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		{
			groupARebate.getFrom().add(usPort);
			groupARebate.getTo().add(usPort);
			groupARebate.setRebate(0.25);

			suezTariff.getRouteRebates().add(groupARebate);
		}
		final SuezCanalRouteRebate groupBRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupBRebate.getFrom().add(usPort);
			groupBRebate.getTo().add(groupBPort);
			groupBRebate.setRebate(0.5);

			suezTariff.getRouteRebates().add(groupBRebate);
		}
		final SuezCanalRouteRebate groupCRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupCRebate.getFrom().add(usPort);
			groupCRebate.getTo().add(groupCPort);
			groupCRebate.setRebate(0.75);

			suezTariff.getRouteRebates().add(groupCRebate);
		}

		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_PORT_ON_BOTH_SIDES);
		Assertions.assertFalse(children.isEmpty());

		boolean foundL1 = false;
		for (final var dscd : children) {
			foundL1 |= dscd.getObjects().contains(groupARebate);
		}

		Assertions.assertTrue(foundL1);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testValidationFailsSamePairTwice() {

		final Port usPort = portModelBuilder.createPort("usPort", "L_usPort", "UTC", 0, 0);

		final Port groupAPort = portModelBuilder.createPort("groupAPort", "L_groupAPort", "UTC", 0, 0);
		final Port groupBPort = portModelBuilder.createPort("groupBPort", "L_groupBPort", "UTC", 0, 0);
		final Port groupCPort = portModelBuilder.createPort("groupCPort", "L_groupCPort", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(1_000);

		// This should also apply to -> from
		final SuezCanalRouteRebate groupARebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		{
			groupARebate.getFrom().add(usPort);
			groupARebate.getTo().add(groupAPort);
			groupARebate.setRebate(0.25);

			suezTariff.getRouteRebates().add(groupARebate);
		}
		final SuezCanalRouteRebate groupBRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupBRebate.getFrom().add(groupAPort);
			groupBRebate.getTo().add(usPort);
			groupBRebate.setRebate(0.5);

			suezTariff.getRouteRebates().add(groupBRebate);
		}
		final SuezCanalRouteRebate groupCRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupCRebate.getFrom().add(usPort);
			groupCRebate.getTo().add(groupCPort);
			groupCRebate.setRebate(0.75);

			suezTariff.getRouteRebates().add(groupCRebate);
		}

		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_DUPLICATE_ENTRY);
		Assertions.assertFalse(children.isEmpty());

		// Check both rebate entries are found
		boolean foundL1 = false;
		boolean foundL2 = false;
		for (final var dscd : children) {
			foundL1 |= dscd.getObjects().contains(groupARebate);
			foundL2 |= dscd.getObjects().contains(groupBRebate);
		}

		Assertions.assertTrue(foundL1);
		Assertions.assertTrue(foundL2);
 
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testValidationFailsBadPercentage() {

		final Port usPort = portModelBuilder.createPort("usPort", "L_usPort", "UTC", 0, 0);

		final Port groupAPort = portModelBuilder.createPort("groupAPort", "L_groupAPort", "UTC", 0, 0);
		final Port groupBPort = portModelBuilder.createPort("groupBPort", "L_groupBPort", "UTC", 0, 0);
		final Port groupCPort = portModelBuilder.createPort("groupCPort", "L_groupCPort", "UTC", 0, 0);

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getRouteCosts().clear();

		final SuezCanalTariff suezTariff = scenarioModelBuilder.getCostModelBuilder().createSimpleSuezCanalTariff(1_000);

		// This should also apply to -> from
		final SuezCanalRouteRebate groupARebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
		{
			groupARebate.getFrom().add(usPort);
			groupARebate.getTo().add(groupAPort);
			groupARebate.setRebate(1.0); // Too high!

			suezTariff.getRouteRebates().add(groupARebate);
		}
		final SuezCanalRouteRebate groupBRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupBRebate.getFrom().add(groupAPort);
			groupBRebate.getTo().add(usPort);
			groupBRebate.setRebate(0.5);

			suezTariff.getRouteRebates().add(groupBRebate);
		}
		final SuezCanalRouteRebate groupCRebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();

		{
			groupCRebate.getFrom().add(usPort);
			groupCRebate.getTo().add(groupCPort);
			groupCRebate.setRebate(0.75);

			suezTariff.getRouteRebates().add(groupCRebate);
		}

		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SuezTariffConstraint.KEY_REBATE_FACTOR);
		Assertions.assertFalse(children.isEmpty());

		// Check both rebate entries are found
		boolean foundL1 = false;
		for (final var dscd : children) {
			foundL1 |= dscd.getObjects().contains(groupARebate);
		}

		Assertions.assertTrue(foundL1);
 
	}
}