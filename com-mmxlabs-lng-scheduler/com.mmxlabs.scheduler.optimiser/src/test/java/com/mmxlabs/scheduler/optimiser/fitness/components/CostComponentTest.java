/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CostComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testInit() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent<Object> c = new CostComponent<Object>(name, fuelComponents, core);

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

	@Test
	public void testEvaluateSequence() {

		// define the fuel consumptions
		// multiply by scale factor (i.e. convert to internal unit)
		final int baseConsumption = 1000 * Calculator.ScaleFactor;
		final int FBOConsumption = 0 * Calculator.ScaleFactor;
		final int NBOConsumption = 500 * Calculator.ScaleFactor;

		// define the fuel unit prices
		// multiply by scale factor (i.e. convert to internal unit)
		final int baseUnit = 13 * Calculator.ScaleFactor;
		final int NBOUnit = 25 * Calculator.ScaleFactor;
		final int FBOUnit = 7 * Calculator.ScaleFactor;

		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		// the list of fuels that will be used.
		final List<FuelComponent> fuelComponents = new ArrayList<FuelComponent>();
		fuelComponents.add(FuelComponent.Base);
		fuelComponents.add(FuelComponent.NBO);
		fuelComponents.add(FuelComponent.FBO);
		final CostComponent<Object> c = new CostComponent<Object>(name, fuelComponents, core);
		c.init(null);

		final VoyageDetails<Object> voyage = new VoyageDetails<Object>();
		// set consumptions
		voyage.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), baseConsumption);
		voyage.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), NBOConsumption);
		voyage.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), FBOConsumption);
		// set unit prices
		voyage.setFuelUnitPrice(FuelComponent.Base, baseUnit);
		voyage.setFuelUnitPrice(FuelComponent.NBO, NBOUnit);
		voyage.setFuelUnitPrice(FuelComponent.FBO, FBOUnit);

		final Object[] voyageSequence = new Object[] { voyage };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(voyageSequence);

		final IResource resource = context.mock(IResource.class);

		c.startEvaluation();
		c.startSequence(resource, true);
		c.nextVoyagePlan(voyagePlan, 0);
		c.nextObject(voyage, 5);
		c.endSequence();

		final long cost = c.endEvaluationAndGetCost();

		// divide by scale factor to convert from internal unit to external (1000$ to $)
		final int expectedBaseCost = (baseConsumption / Calculator.ScaleFactor) * (baseUnit / Calculator.ScaleFactor);
		final int expectedNBOCost = (NBOConsumption / Calculator.ScaleFactor) * (NBOUnit / Calculator.ScaleFactor);
		final int expectedFBOCost = (FBOConsumption / Calculator.ScaleFactor) * (FBOUnit / Calculator.ScaleFactor);

		final int expectedTotalCost = expectedBaseCost + expectedFBOCost + expectedNBOCost;

		Assert.assertEquals("Expected cost equals calculated cost.", expectedTotalCost, cost);

		context.assertIsSatisfied();
	}

	@Test
	public void testCostComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent<Object> c = new CostComponent<Object>(name, fuelComponents, core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
		Assert.assertEquals(fuelComponents, c.getFuelComponents());
	}
}
