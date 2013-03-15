/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class LatenessComponentTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testLatenessComponent() {
		final String name = "name";
		final String dcp = "dcp";
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore();
		final LatenessComponent c = new LatenessComponent(name, dcp, core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
	}

	@Test
	public void testInit() {

		final String name = "name";
		final String dcp = "dcp";
		final CargoSchedulerFitnessCore core = null;
		final LatenessComponent c = new LatenessComponent(name, dcp, core);

		final IOptimisationData data = context.mock(IOptimisationData.class);
		
		final IStartEndRequirementProvider startEndRequirementProvider = context.mock(IStartEndRequirementProvider.class);
		
		context.checking(new Expectations() {
			{
				exactly(1).of(data).getDataComponentProvider(dcp, IStartEndRequirementProvider.class);
				will(returnValue(startEndRequirementProvider));
			}
		});

		c.init(data);

		context.assertIsSatisfied();
	}

	@Test
	public void testEvaluateSequence() {

		// the penalty per 1 unit (hour?) of late. As set in LatenessComponent.
		final int penalty = 1;

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
		final String dcp = "dcp";
		final CargoSchedulerFitnessCore core = null;
		final LatenessComponent c = new LatenessComponent(name, dcp, core);
		
		final IOptimisationData data = context.mock(IOptimisationData.class);
		
		final IStartEndRequirementProvider startEndRequirementProvider = context.mock(IStartEndRequirementProvider.class);
		
		final IResource resource = context.mock(IResource.class);

		final IStartEndRequirement startRequirement = context.mock(IStartEndRequirement.class, "Start");
		final IStartEndRequirement endRequirement = context.mock(IStartEndRequirement.class, "End");
		
		context.checking(new Expectations() {
			{
				
				exactly(1).of(data).getDataComponentProvider(dcp, IStartEndRequirementProvider.class);
				will(returnValue(startEndRequirementProvider));
			
				exactly(1).of(startEndRequirementProvider).getStartRequirement(resource);
				will(returnValue(startRequirement));
				exactly(1).of(startEndRequirementProvider).getEndRequirement(resource);
				will(returnValue(endRequirement));
				
				allowing(startRequirement).getTimeWindow();
				allowing(endRequirement).getTimeWindow();
				
			}
		});

		c.init(data);

		// set up time windows from load/discharge end/start times above
		final TimeWindow window1 = new TimeWindow(loadStartTime, loadEndTime);
		final TimeWindow window2 = new TimeWindow(dischargeStartTime, dischargeEndTime);

		final PortDetails startDetails = new PortDetails();
		startDetails.setOptions(new PortOptions());
		final StartPortSlot startSlot = new StartPortSlot(0, 0, 0);
		startDetails.getOptions().setPortSlot(startSlot);
		final PortDetails endDetails = new PortDetails();
		endDetails.setOptions(new PortOptions());
		final EndPortSlot endSlot = new EndPortSlot();
		endDetails.getOptions().setPortSlot(endSlot);

		
		final LoadSlot loadSlot = new LoadSlot();
		loadSlot.setTimeWindow(window1);

		final DischargeSlot dischargeSlot = new DischargeSlot();
		dischargeSlot.setTimeWindow(window2);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setOptions(new PortOptions());
		loadDetails.getOptions().setPortSlot(loadSlot);

		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setOptions(new PortOptions());
		dischargeDetails.getOptions().setPortSlot(dischargeSlot);

		final Object[] routeSequence = new Object[] { startDetails, loadDetails, null, dischargeDetails, endDetails };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);


		c.startEvaluation();
		c.startSequence(resource);
		c.nextVoyagePlan(voyagePlan, voyageStartTime);
		c.nextObject(startDetails, 0);
		c.nextObject(loadDetails, loadEndTime + loadLateTime);
		c.nextObject(dischargeDetails, dischargeEndTime + dischargeLateTime);
		c.nextObject(endDetails, 0);

		c.endSequence();

		final long cost = c.endEvaluationAndGetCost();

		final long expectedCost = (dischargeLateTime + loadLateTime) * penalty;
		Assert.assertEquals("Expected cost equals calculated cost.", expectedCost, cost);

		context.assertIsSatisfied();

	}
}
