/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class LatenessComponentTest {

	@Test
	public void testLatenessComponent() {
		final String name = "name";
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore(null);
		final LatenessComponent c = new LatenessComponent(name, core);

		Assert.assertSame(name, c.getName());
		Assert.assertSame(core, c.getFitnessCore());
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

		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final IStartEndRequirementProvider startEndRequirementProvider = Mockito.mock(IStartEndRequirementProvider.class);

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IStartEndRequirementProvider.class).toInstance(startEndRequirementProvider);
			}
		});
		final LatenessComponent c = new LatenessComponent(name, core);

		injector.injectMembers(c);

		final IResource resource = Mockito.mock(IResource.class);

		final IStartRequirement startRequirement = Mockito.mock(IStartRequirement.class, "Start");
		final IEndRequirement endRequirement = Mockito.mock(IEndRequirement.class, "End");

		Mockito.when(startEndRequirementProvider.getStartRequirement(resource)).thenReturn(startRequirement);
		Mockito.when(startEndRequirementProvider.getEndRequirement(resource)).thenReturn(endRequirement);

		c.init(data);

		// set up time windows from load/discharge end/start times above
		final TimeWindow window1 = new TimeWindow(loadStartTime, loadEndTime);
		final TimeWindow window2 = new TimeWindow(dischargeStartTime, dischargeEndTime);

		final PortDetails startDetails = new PortDetails();
		startDetails.setOptions(new PortOptions());
		final StartPortSlot startSlot = new StartPortSlot(null);
		startDetails.getOptions().setPortSlot(startSlot);
		final PortDetails endDetails = new PortDetails();
		endDetails.setOptions(new PortOptions());
		final EndPortSlot endSlot = new EndPortSlot(null, null, null, false, 0L);
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

		final IDetailsSequenceElement[] routeSequence = new IDetailsSequenceElement[] { startDetails, loadDetails, null, dischargeDetails, endDetails };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);
		final ScheduledSequences scheduledSequences = new ScheduledSequences();

		c.startEvaluation(scheduledSequences);
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
	}
}
