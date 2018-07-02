/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProvider;

/**
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class CooldownPricingTests extends AbstractMicroTestCase {

	/**
	 * Test the port cost implicit/explicit pricing. Testing order does not matter. In this test generic case then specific
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCooldownPriceOrder1() throws Exception {

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getCooldownCosts().clear();

		// Generic flat rate
		final PortGroup portGroup = scenarioModelBuilder.getPortModelBuilder().makePortGroup("all-ports", scenarioModelBuilder.getPortModelBuilder().getPortModel().getPorts());
		scenarioModelBuilder.getCostModelBuilder().createCooldownPrice("100000", true, Collections.singleton(portGroup));

		// Set specific rate
		final Port port1 = portFinder.findPort("Point Fortin");
		scenarioModelBuilder.getCostModelBuilder().createCooldownPrice("200000", true, Collections.singleton(port1));
		final Port port2 = portFinder.findPort("Sakai");

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IPortCooldownDataProvider portCooldownDataProvider = dataTransformer.getInjector().getInstance(IPortCooldownDataProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel = modelEntityMap.getOptimiserObjectNullChecked(vessel, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assertions.assertEquals(200_000_000L, portCooldownDataProvider.getCooldownCalculator(oPort1).calculateCooldownCost(oVessel, oPort1, 0, 0));
			Assertions.assertEquals(100_000_000L, portCooldownDataProvider.getCooldownCalculator(oPort2).calculateCooldownCost(oVessel, oPort2, 0, 0));
		});
	}

	/**
	 * Test the port cost implicit/explicit pricing. Testing order does not matter. In this test specific then generic case
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCooldownPriceOrder2() throws Exception {

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getCooldownCosts().clear();

		// Set specific rate
		final Port port1 = portFinder.findPort("Point Fortin");
		scenarioModelBuilder.getCostModelBuilder().createCooldownPrice("200000", true, Collections.singleton(port1));
		// Generic flat rate
		final PortGroup portGroup = scenarioModelBuilder.getPortModelBuilder().makePortGroup("all-ports", scenarioModelBuilder.getPortModelBuilder().getPortModel().getPorts());
		scenarioModelBuilder.getCostModelBuilder().createCooldownPrice("100000", true, Collections.singleton(portGroup));

		final Port port2 = portFinder.findPort("Sakai");

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IPortCooldownDataProvider portCooldownDataProvider = dataTransformer.getInjector().getInstance(IPortCooldownDataProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel = modelEntityMap.getOptimiserObjectNullChecked(vessel, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assertions.assertEquals(200_000_000L, portCooldownDataProvider.getCooldownCalculator(oPort1).calculateCooldownCost(oVessel, oPort1, 0, 0));
			Assertions.assertEquals(100_000_000L, portCooldownDataProvider.getCooldownCalculator(oPort2).calculateCooldownCost(oVessel, oPort2, 0, 0));
		});
	}

}