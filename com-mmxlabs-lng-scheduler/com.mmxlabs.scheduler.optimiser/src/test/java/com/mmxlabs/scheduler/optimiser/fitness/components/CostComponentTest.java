/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CostComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testInit() {
		final String name = "name";
		final CargoSchedulerFitnessCore core = null;
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent c = new CostComponent(name, fuelComponents, core);

		final IOptimisationData data = context.mock(IOptimisationData.class);

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
		final long voyageBaseConsumption = OptimiserUnitConvertor.convertToInternalVolume(1000);
		final long voyageFBOConsumption = OptimiserUnitConvertor.convertToInternalVolume(0);
		final long voyageNBOConsumption = OptimiserUnitConvertor.convertToInternalVolume(500);
		final long portBaseConsumption = OptimiserUnitConvertor.convertToInternalVolume(200);

		// define the fuel unit prices
		// multiply by scale factor (i.e. convert to internal unit)
		final int baseUnit = OptimiserUnitConvertor.convertToInternalPrice(13);
		final int NBOUnit = OptimiserUnitConvertor.convertToInternalPrice(25);
		final int FBOUnit = OptimiserUnitConvertor.convertToInternalPrice(7);

		final String name = "name";
		final CargoSchedulerFitnessCore core = null;
		// the list of fuels that will be used.
		final List<FuelComponent> fuelComponents = new ArrayList<FuelComponent>();
		fuelComponents.add(FuelComponent.Base);
		fuelComponents.add(FuelComponent.NBO);
		fuelComponents.add(FuelComponent.FBO);
		final CostComponent c = new CostComponent(name, fuelComponents, core);
		c.init(null);

		final VoyageDetails voyage = new VoyageDetails();
		// set consumptions
		voyage.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), voyageBaseConsumption);
		voyage.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), voyageNBOConsumption);
		voyage.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), voyageFBOConsumption);
		// set unit prices
		voyage.setFuelUnitPrice(FuelComponent.Base, baseUnit);
		voyage.setFuelUnitPrice(FuelComponent.NBO, NBOUnit);
		voyage.setFuelUnitPrice(FuelComponent.FBO, FBOUnit);

		final PortDetails port = new PortDetails();
		port.setFuelConsumption(FuelComponent.Base, portBaseConsumption);
		port.setFuelUnitPrice(FuelComponent.Base, baseUnit);

		// note: the value of this voyage sequence is ignored during this test
		// any reason to have it here?
		final Object[] voyageSequence = new Object[] { voyage, port };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(voyageSequence);

		final IResource resource = context.mock(IResource.class);

		c.startEvaluation();
		c.startSequence(resource, true);
		// note, no voyage plan needs to be passed to the CostComponent for this test
		c.nextVoyagePlan(voyagePlan, 0);
		c.nextObject(voyage, 5);
		c.nextObject(port, 5);
		c.endSequence();

		final long cost = c.endEvaluationAndGetCost();

		// divide by scale factor to convert from internal unit to external (1000$ to $)
		final long expectedVoyageBaseCost = Calculator.costFromConsumption(voyageBaseConsumption, baseUnit);
		final long expectedVoyageNBOCost = Calculator.costFromConsumption(voyageNBOConsumption, NBOUnit);
		final long expectedVoyageFBOCost = Calculator.costFromConsumption(voyageFBOConsumption, FBOUnit);
		final long expectedPortBaseCost = Calculator.costFromConsumption(portBaseConsumption, baseUnit);

		final long expectedTotalCost = (expectedVoyageBaseCost + expectedVoyageFBOCost + expectedVoyageNBOCost + expectedPortBaseCost) / Calculator.ScaleFactor;

		Assert.assertEquals("Expected cost equals calculated cost.", expectedTotalCost, cost);

		context.assertIsSatisfied();
	}

	@Test
	public void testCostComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore();
		final List<FuelComponent> fuelComponents = Collections.emptyList();
		final CostComponent c = new CostComponent(name, fuelComponents, core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
		Assert.assertEquals(fuelComponents, c.getFuelComponents());
	}
}
