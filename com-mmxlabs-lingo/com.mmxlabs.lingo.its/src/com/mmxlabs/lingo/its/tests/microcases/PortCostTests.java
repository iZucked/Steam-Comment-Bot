/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class PortCostTests extends AbstractMicroTestCase {

	/**
	 * Test the port cost implicit/explicit pricing. Testing order does not matter. In this test generic case then specific
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testPortCostOrder1() throws Exception {

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getPortCosts().clear();

		// Generic flat rate
		final PortGroup portGroup = scenarioModelBuilder.getPortModelBuilder().makePortGroup("all-ports", scenarioModelBuilder.getPortModelBuilder().getPortModel().getPorts());
		scenarioModelBuilder.getCostModelBuilder().createPortCost(Collections.singleton(portGroup), Collections.singleton(PortCapability.LOAD), 100_000);

		// Set specific rate
		final Port port1 = portFinder.findPort("Point Fortin");
		scenarioModelBuilder.getCostModelBuilder().createPortCost(Collections.singleton(port1), Collections.singleton(PortCapability.LOAD), 200_000);
		final Port port2 = portFinder.findPort("Sakai");

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IPortCostProvider portCostProvider = dataTransformer.getInjector().getInstance(IPortCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel = modelEntityMap.getOptimiserObjectNullChecked(vessel, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assert.assertEquals(200_000_000L, portCostProvider.getPortCost(oPort1, oVessel, PortType.Load));
			Assert.assertEquals(100_000_000L, portCostProvider.getPortCost(oPort2, oVessel, PortType.Load));
		});
	}

	/**
	 * Test the port cost implicit/explicit pricing. Testing order does not matter. In this test specific then generic case
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testPortCostOrder2() throws Exception {

		// Remove all existing costs
		scenarioModelBuilder.getCostModelBuilder().getCostModel().getPortCosts().clear();

		// Set specific rate
		final Port port1 = portFinder.findPort("Point Fortin");
		scenarioModelBuilder.getCostModelBuilder().createPortCost(Collections.singleton(port1), Collections.singleton(PortCapability.LOAD), 200_000);
		// Generic flat rate
		final PortGroup portGroup = scenarioModelBuilder.getPortModelBuilder().makePortGroup("all-ports", scenarioModelBuilder.getPortModelBuilder().getPortModel().getPorts());
		scenarioModelBuilder.getCostModelBuilder().createPortCost(Collections.singleton(portGroup), Collections.singleton(PortCapability.LOAD), 100_000);

		final Port port2 = portFinder.findPort("Sakai");

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final IPortCostProvider portCostProvider = dataTransformer.getInjector().getInstance(IPortCostProvider.class);

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IVessel oVessel = modelEntityMap.getOptimiserObjectNullChecked(vessel, IVessel.class);

			final IPort oPort1 = modelEntityMap.getOptimiserObjectNullChecked(port1, IPort.class);
			final IPort oPort2 = modelEntityMap.getOptimiserObjectNullChecked(port2, IPort.class);

			Assert.assertEquals(200_000_000L, portCostProvider.getPortCost(oPort1, oVessel, PortType.Load));
			Assert.assertEquals(100_000_000L, portCostProvider.getPortCost(oPort2, oVessel, PortType.Load));
		});
	}

}