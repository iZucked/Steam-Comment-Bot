/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.schedule.VoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@SuppressWarnings("null")
public class VoyagePlanAnnotatorTest {

	@Test
	public void testAnnotateFromVoyagePlans() {

		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement element3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement element4 = Mockito.mock(ISequenceElement.class, "4");

		final IPort port1 = Mockito.mock(IPort.class, "port-1");
		final IPort port2 = Mockito.mock(IPort.class, "port-2");

		final LoadSlot loadSlot1 = new LoadSlot("load1", port1, Mockito.mock(ITimeWindow.class), true, 0L, 50_000, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);

		final DischargeSlot dischargeSlot1 = new DischargeSlot("discharge1", port2, Mockito.mock(ITimeWindow.class), true, 0L, 50_000L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final LoadSlot loadSlot2 = new LoadSlot("load2", port1, Mockito.mock(ITimeWindow.class), true, 0L, 50_000, Mockito.mock(ILoadPriceCalculator.class), 1_000_000, false, false);

		final DischargeSlot dischargeSlot2 = new DischargeSlot("discharge2", port2, Mockito.mock(ITimeWindow.class), true, 0L, 50_000L, Mockito.mock(ISalesPriceCalculator.class), 0, 0);

		final IPortSlotProviderEditor portSlotEditor = new HashMapPortSlotEditor();
		portSlotEditor.setPortSlot(element1, loadSlot1);
		portSlotEditor.setPortSlot(element2, dischargeSlot1);
		portSlotEditor.setPortSlot(element3, loadSlot2);
		portSlotEditor.setPortSlot(element4, dischargeSlot2);

		// final PortDetails loadDetails1 = new PortDetails();
		final int loadVisitDuration1 = 10;
		final double loadFuelPrice1 = 1.0;
		final long loadFuelConsumption1 = 0L;
		final PortDetails loadDetails1 = constructPortDetails(loadSlot1, loadVisitDuration1, loadFuelPrice1, loadFuelConsumption1);

		final int dischargeVisitDuration1 = 20;
		final double dischargeFuelPrice1 = 1.1;
		final long dischargeFuelConsumption1 = 200L;
		final PortDetails dischargeDetails1 = constructPortDetails(dischargeSlot1, dischargeVisitDuration1, dischargeFuelPrice1, dischargeFuelConsumption1);

		final int loadVisitDuration2 = 30;
		final double loadFuelPrice2 = 1.2;
		final long loadFuelConsumption2 = 400L;
		final PortDetails loadDetails2 = constructPortDetails(loadSlot2, loadVisitDuration2, loadFuelPrice2, loadFuelConsumption2);

		final int dischargeVisitDuration2 = 40;
		final double dischargeFuelPrice2 = 1.3;
		final long dischargeFuelConsumption2 = 600L;
		final PortDetails dischargeDetails2 = constructPortDetails(dischargeSlot2, dischargeVisitDuration2, dischargeFuelPrice2, dischargeFuelConsumption2);

		final VoyageOptions options1 = new VoyageOptions(loadSlot1, dischargeSlot1);
		options1.setRoute(ERouteOption.DIRECT, 500, 0L);
		options1.setAvailableTime(90);
		options1.setVesselState(VesselState.Laden);

		final VoyageDetails voyageDetails1 = new VoyageDetails(options1);

		voyageDetails1.setSpeed(15000);
		// voyageDetails1.setStartTime(110);
		voyageDetails1.setTravelTime(50);
		voyageDetails1.setIdleTime(40);

		final FuelInfo voyageFuelInfo1 = new FuelInfo(FuelComponent.Base, 1000L, 1.0, FuelComponent.Base_Supplemental, 2000L, 2.0, FuelComponent.NBO, 3000L, 3.0, FuelComponent.FBO, 4000L, 4.0,
				FuelComponent.IdleBase, 5000L, 5.0, FuelComponent.IdleNBO, 6000L, 6.0);

		voyageFuelInfo1.setVoyageFuelDetails(voyageDetails1);

		final VoyageOptions options2 = new VoyageOptions(dischargeSlot1, loadSlot2);
		final VoyageDetails voyageDetails2 = new VoyageDetails(options2);
		options2.setRoute(ERouteOption.DIRECT, 1000, 0L);
		options2.setAvailableTime(80);
		options2.setVesselState(VesselState.Ballast);

		final FuelInfo voyageFuelInfo2 = new FuelInfo(FuelComponent.Base, 1100L, 1.10, FuelComponent.Base_Supplemental, 2100L, 2.10, FuelComponent.NBO, 3100L, 3.10, FuelComponent.FBO, 4100L, 4.10,
				FuelComponent.IdleBase, 5100L, 5.10, FuelComponent.IdleNBO, 6100L, 6.10);

		voyageFuelInfo2.setVoyageFuelDetails(voyageDetails2);

		voyageDetails2.setSpeed(16000);
		// voyageDetails2.setStartTime(220);
		voyageDetails2.setTravelTime(50);
		voyageDetails2.setIdleTime(30);

		final VoyageOptions options3 = new VoyageOptions(loadSlot2, dischargeSlot2);
		options3.setRoute(ERouteOption.DIRECT, 1500, 0L);
		options3.setAvailableTime(70);
		options3.setVesselState(VesselState.Laden);

		final VoyageDetails voyageDetails3 = new VoyageDetails(options3);

		voyageDetails3.setSpeed(17000);
		// voyageDetails3.setStartTime(330);
		voyageDetails3.setTravelTime(50);
		voyageDetails3.setIdleTime(20);

		final FuelInfo voyageFuelInfo3 = new FuelInfo(FuelComponent.Base, 1200L, 1.20, FuelComponent.Base_Supplemental, 2200L, 2.20, FuelComponent.NBO, 3200L, 3.20, FuelComponent.FBO, 4200L, 4.20,
				FuelComponent.IdleBase, 5200L, 5.20, FuelComponent.IdleNBO, 6200L, 6.20);
		voyageFuelInfo3.setVoyageFuelDetails(voyageDetails3);

		final IPortCostProvider portCostProvider = Mockito.mock(IPortCostProvider.class);
		final VoyagePlanAnnotator annotator = createVoyagePlanAnnotator(portSlotEditor, portCostProvider, vesselProvider);

		final VoyagePlan plan1 = new VoyagePlan();
		plan1.setSequence(new IDetailsSequenceElement[] { loadDetails1, voyageDetails1, dischargeDetails1, voyageDetails2, loadDetails2 });
		PortTimesRecord portTimesRecord1 = new PortTimesRecord();
		portTimesRecord1.setSlotTime(loadSlot1, 0);
		portTimesRecord1.setSlotTime(dischargeSlot1, 100);
		portTimesRecord1.setSlotDuration(loadSlot1, loadVisitDuration1);
		portTimesRecord1.setSlotDuration(dischargeSlot1, dischargeVisitDuration1);
		portTimesRecord1.setReturnSlotTime(loadSlot2, 200);
		plan1.setIgnoreEnd(true);

		final VoyagePlan plan2 = new VoyagePlan();
		plan2.setSequence(new IDetailsSequenceElement[] { loadDetails2, voyageDetails3, dischargeDetails2 });
		PortTimesRecord portTimesRecord2 = new PortTimesRecord();
		portTimesRecord2.setSlotTime(loadSlot2, 200);
		portTimesRecord2.setSlotTime(dischargeSlot2, 300);
		portTimesRecord2.setSlotDuration(loadSlot2, loadVisitDuration2);
		portTimesRecord2.setSlotDuration(dischargeSlot2, dischargeVisitDuration2);
		plan2.setIgnoreEnd(false);

		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> plans = new LinkedList<>();
		plans.add(new Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>(plan1, null, portTimesRecord1));
		plans.add(new Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>(plan2, null, portTimesRecord2));

		final IResource resource = Mockito.mock(IResource.class);
		Mockito.when(vesselProvider.getVesselAvailability(resource)).thenReturn(vesselAvailability);

		ISequences sequences = Mockito.mock(ISequences.class);
		IEvaluationState evaluationState = Mockito.mock(IEvaluationState.class);
		IOptimisationContext context = Mockito.mock(IOptimisationContext.class);

		final AnnotatedSolution annotatedSolution = new AnnotatedSolution(sequences, context, evaluationState);
		ISequence sequence = Mockito.mock(ISequence.class);
		VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, sequence, 0, plans);
		annotator.annotateFromVoyagePlan(scheduledSequence, annotatedSolution);

		{
			final IJourneyEvent journey = annotatedSolution.getElementAnnotations().getAnnotation(element1, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNull(journey);

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element1, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNull(idle);

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element1, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			checkPortVisit(portVisit, element1, loadSlot1, 0, loadVisitDuration1, loadFuelConsumption1, loadFuelPrice1);
		}
		{
			final IJourneyEvent journey = annotatedSolution.getElementAnnotations().getAnnotation(element2, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element2, journey.getSequenceElement());
			Assert.assertSame(port1, journey.getFromPort());
			Assert.assertSame(port2, journey.getToPort());
			Assert.assertEquals(10, journey.getStartTime());
			Assert.assertEquals(60, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(500, journey.getDistance());

			voyageFuelInfo1.checkJourneyTravelFuelDetails(journey);

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element2, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element2, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(60, idle.getStartTime());
			Assert.assertEquals(100, idle.getEndTime());
			Assert.assertEquals(40, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			voyageFuelInfo1.checkJourneyIdleFuelDetails(idle);

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element2, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			checkPortVisit(portVisit, element2, dischargeSlot1, 100, dischargeVisitDuration1, dischargeFuelConsumption1, dischargeFuelPrice1);
		}

		{
			final IJourneyEvent journey = annotatedSolution.getElementAnnotations().getAnnotation(element3, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element3, journey.getSequenceElement());
			Assert.assertSame(port2, journey.getFromPort());
			Assert.assertSame(port1, journey.getToPort());
			Assert.assertEquals(120, journey.getStartTime());
			Assert.assertEquals(170, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(1000, journey.getDistance());

			voyageFuelInfo2.checkJourneyTravelFuelDetails(journey);

			Assert.assertEquals(VesselState.Ballast, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element3, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element3, idle.getSequenceElement());
			Assert.assertSame(port1, idle.getPort());
			Assert.assertEquals(170, idle.getStartTime());
			Assert.assertEquals(200, idle.getEndTime());
			Assert.assertEquals(30, idle.getDuration());

			Assert.assertEquals(VesselState.Ballast, idle.getVesselState());

			voyageFuelInfo2.checkJourneyIdleFuelDetails(idle);

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element3, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			checkPortVisit(portVisit, element3, loadSlot2, 200, loadVisitDuration2, loadFuelConsumption2, loadFuelPrice2);
		}

		{
			final IJourneyEvent journey = annotatedSolution.getElementAnnotations().getAnnotation(element4, SchedulerConstants.AI_journeyInfo, IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element4, journey.getSequenceElement());
			Assert.assertSame(port1, journey.getFromPort());
			Assert.assertSame(port2, journey.getToPort());
			Assert.assertEquals(230, journey.getStartTime());
			Assert.assertEquals(280, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(1500, journey.getDistance());

			voyageFuelInfo3.checkJourneyTravelFuelDetails(journey);

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element4, SchedulerConstants.AI_idleInfo, IIdleEvent.class);

			Assert.assertNotNull(idle);

			Assert.assertSame(element4, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(280, idle.getStartTime());
			Assert.assertEquals(300, idle.getEndTime());
			Assert.assertEquals(20, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			voyageFuelInfo3.checkJourneyIdleFuelDetails(idle);

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element4, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			checkPortVisit(portVisit, element4, dischargeSlot2, 300, dischargeVisitDuration2, dischargeFuelConsumption2, dischargeFuelPrice2);

		}
	}

	private int checkPortVisit(final IPortVisitEvent portVisit, final ISequenceElement element, final IPortSlot slot, final int startTime, final int duration, final long consumption,
			final double cost) {

		Assert.assertNotNull(portVisit);
		Assert.assertSame(element, portVisit.getSequenceElement());
		Assert.assertSame(slot, portVisit.getPortSlot());
		Assert.assertEquals(startTime, portVisit.getStartTime());
		Assert.assertEquals(duration, portVisit.getDuration());
		Assert.assertEquals(portVisit.getStartTime() + portVisit.getDuration(), portVisit.getEndTime());
		Assert.assertEquals(consumption, portVisit.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
		Assert.assertEquals((long) (cost * consumption), portVisit.getFuelCost(FuelComponent.Base));
		return startTime + duration;

	}

	private PortDetails constructPortDetails(final @NonNull IPortSlot slot, final int duration, final double fuelPrice, final long fuelConsumption) {

		final PortOptions options = new PortOptions(slot);
		options.setVisitDuration(duration);
		final PortDetails result = new PortDetails(options);

		result.setFuelUnitPrice(FuelComponent.Base, OptimiserUnitConvertor.convertToInternalPrice(fuelPrice));
		result.setFuelConsumption(FuelComponent.Base, fuelConsumption);

		return result;
	}

	/**
	 * 
	 * @author Simon McGregor
	 * 
	 *         Convenience test method to specify fuel details (price and consumption) for a VoyageDetails object in the form
	 * 
	 *         new FuelInfo( FuelComponent.Base, baseConsumption, basePrice, FuelComponent.NBO, nboConsumption, nboPrice, ... );
	 * 
	 *         Use the method setVoyageFuelDetails(VoyageDetails voyage) to set the details for a voyage object.
	 * 
	 * @param details
	 * @param objects
	 */
	private class FuelInfo {
		private final HashMap<FuelComponent, Double> costs = new HashMap<FuelComponent, Double>();
		private final HashMap<FuelComponent, Long> consumptions = new HashMap<FuelComponent, Long>();

		public FuelInfo(final Object... objects) {
			for (int i = 0; i < objects.length; i += 3) {
				final FuelComponent fuel = (FuelComponent) objects[i];
				final Long consumption = (Long) objects[i + 1];
				final Double price = (Double) objects[i + 2];
				costs.put(fuel, price);
				consumptions.put(fuel, consumption);
			}
		}

		public void setVoyageFuelDetails(final VoyageDetails details) {
			for (final FuelComponent fuel : costs.keySet()) {
				details.setFuelConsumption(fuel, fuel.getPricingFuelUnit(), consumptions.get(fuel));
				details.setFuelUnitPrice(fuel, OptimiserUnitConvertor.convertToInternalPrice(costs.get(fuel)));
			}
		}

		/**
		 * Check that the fuel details for a (travel) journey match the specifications in the FuelInfo object - idle values should have been set to zero.
		 * 
		 * @param journey
		 */
		public void checkJourneyTravelFuelDetails(final IJourneyEvent journey) {
			for (final FuelComponent fuel : FuelComponent.getTravelFuelComponents()) {
				if (costs.containsKey(fuel)) {
					final long consumption = consumptions.get(fuel);
					final double cost = costs.get(fuel);
					Assert.assertEquals(consumption, journey.getFuelConsumption(fuel, fuel.getPricingFuelUnit()));
					Assert.assertEquals((long) (consumption * cost), journey.getFuelCost(fuel));
				}
			}

			for (final FuelComponent fuel : FuelComponent.getIdleFuelComponents()) {
				if (costs.containsKey(fuel)) {
					Assert.assertEquals(0L, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals(0L, journey.getFuelCost(fuel));
				}
			}
		}

		/**
		 * Check that the fuel details for a (travel) journey match the specifications in the FuelInfo object - non-idle values should have been set to zero.
		 * 
		 * @param journey
		 */
		public void checkJourneyIdleFuelDetails(final IIdleEvent journey) {
			for (final FuelComponent fuel : FuelComponent.getIdleFuelComponents()) {
				if (costs.containsKey(fuel)) {
					final long consumption = consumptions.get(fuel);
					final double cost = costs.get(fuel);
					Assert.assertEquals(consumption, journey.getFuelConsumption(fuel, fuel.getPricingFuelUnit()));
					Assert.assertEquals((long) (consumption * cost), journey.getFuelCost(fuel));
				}
			}

			for (final FuelComponent fuel : FuelComponent.getTravelFuelComponents()) {
				if (costs.containsKey(fuel)) {
					Assert.assertEquals(0L, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals(0L, journey.getFuelCost(fuel));
				}
			}
		}
	}

	private VoyagePlanAnnotator createVoyagePlanAnnotator(final IPortSlotProvider portSlotProvider, final IPortCostProvider portCostProvider, final IVesselProvider vesselProvider) {

		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
				bind(IPortCostProvider.class).toInstance(portCostProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(VoyagePlanAnnotator.class);
			}
		}).getInstance(VoyagePlanAnnotator.class);
	}
}
