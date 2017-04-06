/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Arrays;
import java.util.LinkedList;

import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.NotionalEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.schedule.LatenessChecker;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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
		final CargoSchedulerFitnessCore core = new CargoSchedulerFitnessCore(null);

		final IOptimisationData data = Mockito.mock(IOptimisationData.class);

		final IStartEndRequirementProvider startEndRequirementProvider = Mockito.mock(IStartEndRequirementProvider.class);
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IPromptPeriodProvider promptProvider = Mockito.mock(IPromptPeriodProvider.class);

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IStartEndRequirementProvider.class).toInstance(startEndRequirementProvider);
				bind(IPromptPeriodProvider.class).toInstance(promptProvider);
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
			}

			@Provides
			@Singleton
			private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
				final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
				int highPeriodInDays = 15;
				int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
				idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
				idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
				idleParams.setEndWeight(10_000);

				return idleParams;
			}

			@Provides
			@Singleton
			private ILatenessComponentParameters provideLatenessParameters() {
				final LatenessComponentParameters lcp = new LatenessComponentParameters();

				lcp.setThreshold(Interval.PROMPT, 24);
				lcp.setLowWeight(Interval.PROMPT, 1);
				lcp.setHighWeight(Interval.PROMPT, 1);

				lcp.setThreshold(Interval.MID_TERM, 24);
				lcp.setLowWeight(Interval.MID_TERM, 1);
				lcp.setHighWeight(Interval.MID_TERM, 1);

				lcp.setThreshold(Interval.BEYOND, 24);
				lcp.setLowWeight(Interval.BEYOND, 1);
				lcp.setHighWeight(Interval.BEYOND, 1);
				return lcp;
			}
		});

		final LatenessComponent c = new LatenessComponent(name, core);
		final LatenessChecker checker = new LatenessChecker();
		injector.injectMembers(c);

		final IResource resource = Mockito.mock(IResource.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);

		final IStartRequirement startRequirement = Mockito.mock(IStartRequirement.class, "Start");
		final IEndRequirement endRequirement = Mockito.mock(IEndRequirement.class, "End");

		Mockito.when(startEndRequirementProvider.getStartRequirement(resource)).thenReturn(startRequirement);
		Mockito.when(startEndRequirementProvider.getEndRequirement(resource)).thenReturn(endRequirement);

		c.init(data);

		injector.injectMembers(checker);
		// set up time windows from load/discharge end/start times above
		final TimeWindow window1 = new TimeWindow(loadStartTime, loadEndTime);
		final TimeWindow window2 = new TimeWindow(dischargeStartTime, dischargeEndTime);

		final StartPortSlot startSlot = new StartPortSlot("start", Mockito.mock(IPort.class), Mockito.mock(ITimeWindow.class), null);
		final PortDetails startDetails = new PortDetails(new PortOptions(startSlot));

		IHeelOptionConsumer heelOptions = new HeelOptionConsumer(0L, 0L, VesselTankState.EITHER, new ConstantHeelPriceCalculator(0));
		final IEndPortSlot endSlot = new NotionalEndPortSlot("end", null, null, heelOptions);
		final PortDetails endDetails = new PortDetails(new PortOptions(endSlot));

		final LoadSlot loadSlot = new LoadSlot("l1", Mockito.mock(IPort.class), window1, true, 0L, 140_000_000L, Mockito.mock(ILoadPriceCalculator.class), 22400, false, true);

		final DischargeSlot dischargeSlot = new DischargeSlot("d1", Mockito.mock(IPort.class), window2, true, 0L, 140_000_000L, Mockito.mock(ISalesPriceCalculator.class), 20_000, 30_000);
		dischargeSlot.setTimeWindow(window2);

		final PortDetails loadDetails = new PortDetails(new PortOptions(loadSlot));

		final PortDetails dischargeDetails = new PortDetails(new PortOptions(dischargeSlot));

		final IDetailsSequenceElement[] routeSequence = new IDetailsSequenceElement[] { startDetails, loadDetails, null, dischargeDetails, endDetails };
		final VoyagePlan voyagePlan = new VoyagePlan();
		voyagePlan.setSequence(routeSequence);
		ISequence mockedSequence = Mockito.mock(ISequence.class);
		Mockito.when(mockedSequence.size()).thenReturn(4);
		IPortTimesRecord portTimesRecord = Mockito.mock(IAllocationAnnotation.class);
		Mockito.when(portTimesRecord.getSlotTime(startSlot)).thenReturn(0);
		Mockito.when(portTimesRecord.getSlotTime(endSlot)).thenReturn(0);
		Mockito.when(portTimesRecord.getSlotTime(loadSlot)).thenReturn(loadEndTime - 1 + loadLateTime);
		Mockito.when(portTimesRecord.getSlotTime(dischargeSlot)).thenReturn(dischargeEndTime - 1 + dischargeLateTime);
		VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, vesselAvailability, mockedSequence, voyageStartTime,
				new LinkedList<Pair<VoyagePlan, IPortTimesRecord>>(Arrays.asList(new Pair<VoyagePlan, IPortTimesRecord>(voyagePlan, portTimesRecord))));

		final VolumeAllocatedSequences volumeAllocatedSequences = new VolumeAllocatedSequences();
		volumeAllocatedSequences.add(vesselAvailability, scheduledSequence);
		final ProfitAndLossSequences profitAndLossSequences = new ProfitAndLossSequences(volumeAllocatedSequences);

		checker.calculateLateness(scheduledSequence, null);
		c.startEvaluation(profitAndLossSequences);
		c.startSequence(resource);
		c.nextVoyagePlan(voyagePlan, voyageStartTime);
		c.nextObject(startDetails, 0);
		c.nextObject(loadDetails, loadEndTime - 1 + loadLateTime);
		c.nextObject(dischargeDetails, dischargeEndTime - 1 + dischargeLateTime);
		c.nextObject(endDetails, 0);

		c.endSequence();

		final long cost = c.endEvaluationAndGetCost();

		final long expectedCost = (dischargeLateTime + loadLateTime) * penalty;
		Assert.assertEquals("Expected cost equals calculated cost.", expectedCost, cost);
	}

}
