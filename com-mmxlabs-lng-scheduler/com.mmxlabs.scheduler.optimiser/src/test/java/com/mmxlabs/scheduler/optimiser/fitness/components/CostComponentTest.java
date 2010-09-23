package com.mmxlabs.scheduler.optimiser.fitness.components;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public class CostComponentTest {

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testEvaluateSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testCostComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent<Object> c = new CostComponent<Object>(name, fuelComponents,
				core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
		Assert.assertEquals(fuelComponents, c.getFuelComponents());
	}
}
