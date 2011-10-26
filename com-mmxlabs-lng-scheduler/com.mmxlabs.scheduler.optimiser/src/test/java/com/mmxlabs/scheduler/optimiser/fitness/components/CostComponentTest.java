/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public class CostComponentTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testInit() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent<Object> c = new CostComponent<Object>(name, fuelComponents,
				core);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context.mock(IOptimisationData.class);

		final String key = "provider-discount-curve";
		final String componentName = "name";

		final Class<IDiscountCurveProvider> classDiscountCurveProvider = IDiscountCurveProvider.class;
		final IDiscountCurveProvider discountCurveProvider = context.mock(IDiscountCurveProvider.class);
		final ICurve curve = context.mock(ICurve.class);

		context.checking(new Expectations() {
			{
				exactly(1).of(data).getDataComponentProvider(key, classDiscountCurveProvider);
				will(returnValue(discountCurveProvider));

				exactly(1).of(discountCurveProvider).getDiscountCurve(componentName);
				will(returnValue(curve));
			}
		});

		c.init(data);

		context.assertIsSatisfied();
	}

	@Ignore
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
