/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class LatenessComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLatenessComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore();
		final LatenessComponent c = new LatenessComponent(name, core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
	}

	@Test
	public void testInit() {

		final String name = "name";
		final CargoSchedulerFitnessCore core = null;
		final LatenessComponent c = new LatenessComponent(name, core);

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
		
		// the penalty per 1 unit (hour?) of late. As set in LatenessComponent. 
		final int penalty = 1000000;

		// the expected times
		final int voyageStartTime = 0;
		final int loadStartTime = 10;
		final int loadEndTime = 11;
		final int dischargeStartTime = 20;
		final int dischargeEndTime = 21;
		
		// the amounts to be late by
		final int loadLateTime = 1;
		final int dischargeLateTime = 1;
		
		final String name = "name";
		final CargoSchedulerFitnessCore core = null;
		final LatenessComponent c = new LatenessComponent(name, core);
		c.init(null);
		
		// set up time windows from load/discharge end/start times above
		final TimeWindow window1 = new TimeWindow(loadStartTime, loadEndTime);
		final TimeWindow window2 = new TimeWindow(dischargeStartTime, dischargeEndTime);

		final LoadSlot loadSlot = new LoadSlot();
		loadSlot.setTimeWindow(window1);

		final DischargeSlot dischargeSlot = new DischargeSlot();
		dischargeSlot.setTimeWindow(window2);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setPortSlot(loadSlot);

		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setPortSlot(dischargeSlot);

		final Object[] routeSequence = new Object[] { loadDetails, null, dischargeDetails };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);

		final IResource resource = context.mock(IResource.class);

		c.startEvaluation();
		c.startSequence(resource, true);
		c.nextVoyagePlan(voyagePlan, voyageStartTime);
		c.nextObject(loadDetails, loadEndTime + loadLateTime);
		c.nextObject(dischargeDetails, dischargeEndTime + dischargeLateTime);
		
		c.endSequence();
		
		final long cost = c.endEvaluationAndGetCost();
		
		final long expectedCost = (dischargeLateTime  + loadLateTime) * penalty;
		Assert.assertEquals("Expected cost equals calculated cost.", expectedCost, cost);
		
		context.assertIsSatisfied();
		
	}
}
