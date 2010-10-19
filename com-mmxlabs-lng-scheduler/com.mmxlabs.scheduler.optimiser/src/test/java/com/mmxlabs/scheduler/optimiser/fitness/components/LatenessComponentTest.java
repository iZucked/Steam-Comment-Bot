/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.OptimiserTestUtil;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class LatenessComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLatenessComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = new CargoSchedulerFitnessCore<Object>();
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
	}

	@Test
	public void testInit() {

		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);

		@SuppressWarnings("unchecked")
		final IOptimisationData<Object> data = context
				.mock(IOptimisationData.class);

		context.checking(new Expectations() {
			{
				// Expect nothing
			}
		});

		c.init(data);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequence() {
		final String name = "name";
		final CargoSchedulerFitnessCore<Object> core = null;
		final LatenessComponent<Object> c = new LatenessComponent<Object>(name,
				core);
		c.init(null);

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final TimeWindow window1 = new TimeWindow(10, 11);
		final TimeWindow window2 = new TimeWindow(20, 21);

		final LoadSlot loadSlot = new LoadSlot();
		loadSlot.setTimeWindow(window1);

		final DischargeSlot dischargeSlot = new DischargeSlot();
		dischargeSlot.setTimeWindow(window2);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setPortSlot(loadSlot);
		

		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setPortSlot(dischargeSlot);
		

		final Object[] routeSequence = new Object[] { loadDetails, null,
				dischargeDetails };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);

		final IResource resource = context.mock(IResource.class);
		final IModifiableSequence<Object> sequence = OptimiserTestUtil
				.makeSequence(obj1, obj2);

		c.prepare();

		c.beginIterating(resource);
		c.evaluateNextObject(loadDetails, 15);
		c.evaluateNextObject(dischargeDetails, 20);
		c.endIterating();
		
		c.evaluateSequence(resource, sequence,
				CollectionsUtil.makeArrayList(voyagePlan),false,0);

		c.complete();

		// 4 hours lateness * hardcoded weight
		Assert.assertEquals(4 * 1000000, c.getFitness());

		context.assertIsSatisfied();
	}
}
