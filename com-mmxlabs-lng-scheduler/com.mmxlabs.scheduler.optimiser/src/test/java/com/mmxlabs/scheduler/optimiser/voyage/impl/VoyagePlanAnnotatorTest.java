/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.events.IFuelUsingEvent;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@RunWith(JMock.class)
public class VoyagePlanAnnotatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testAnnotateFromVoyagePlans() {

		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		final IVessel vessel = context.mock(IVessel.class);

		final ISequenceElement element1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement element2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement element3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement element4 = context.mock(ISequenceElement.class, "4");

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");

		final LoadSlot loadSlot1 = new LoadSlot();
		loadSlot1.setPort(port1);
		loadSlot1.setMaxLoadVolume(50000);

		final DischargeSlot dischargeSlot1 = new DischargeSlot();
		dischargeSlot1.setPort(port2);
		dischargeSlot1.setMaxDischargeVolume(50000);

		final LoadSlot loadSlot2 = new LoadSlot();
		loadSlot2.setPort(port1);
		loadSlot2.setMaxLoadVolume(50000);

		final DischargeSlot dischargeSlot2 = new DischargeSlot();
		dischargeSlot2.setPort(port2);
		dischargeSlot2.setMaxDischargeVolume(50000);

		final IPortSlotProviderEditor portSlotEditor = new HashMapPortSlotEditor("name");
		portSlotEditor.setPortSlot(element1, loadSlot1);
		portSlotEditor.setPortSlot(element2, dischargeSlot1);
		portSlotEditor.setPortSlot(element3, loadSlot2);
		portSlotEditor.setPortSlot(element4, dischargeSlot2);

		//final PortDetails loadDetails1 = new PortDetails();
		int loadVisitDuration1 = 10;
		double loadFuelPrice1 = 1.0;
		long loadFuelConsumption1 = 0l;
		final PortDetails loadDetails1 = constructPortDetails(loadSlot1, loadVisitDuration1, loadFuelPrice1, loadFuelConsumption1);
		
		int dischargeVisitDuration1 = 20;
		double dischargeFuelPrice1 = 1.1;
		long dischargeFuelConsumption1 = 200l;
		final PortDetails dischargeDetails1 = constructPortDetails(dischargeSlot1, dischargeVisitDuration1, dischargeFuelPrice1, dischargeFuelConsumption1);
		
		int loadVisitDuration2 = 30;
		double loadFuelPrice2 = 1.2;
		long loadFuelConsumption2 = 400l;
		final PortDetails loadDetails2 = constructPortDetails(loadSlot2, loadVisitDuration2, loadFuelPrice2, loadFuelConsumption2);

		int dischargeVisitDuration2 = 40;
		double dischargeFuelPrice2 = 1.3;
		long dischargeFuelConsumption2 = 600l;
		final PortDetails dischargeDetails2 = constructPortDetails(dischargeSlot2, dischargeVisitDuration2, dischargeFuelPrice2, dischargeFuelConsumption2);

		final VoyageDetails voyageDetails1 = new VoyageDetails();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setFromPortSlot(loadSlot1);
		options1.setToPortSlot(dischargeSlot1);
		options1.setDistance(500);
		options1.setAvailableTime(90);
		options1.setVesselState(VesselState.Laden);

		voyageDetails1.setOptions(options1);

		voyageDetails1.setSpeed(15000);
		// voyageDetails1.setStartTime(110);
		voyageDetails1.setTravelTime(50);
		voyageDetails1.setIdleTime(40);

		FuelInfo voyageFuelInfo1 = new FuelInfo(
				FuelComponent.Base, 1000l, 1.0,
				FuelComponent.Base_Supplemental, 2000l, 2.0,
				FuelComponent.NBO, 3000l, 3.0,
				FuelComponent.FBO, 4000l, 4.0,
				FuelComponent.IdleBase, 5000l, 5.0,
				FuelComponent.IdleNBO, 6000l, 6.0
				);
		
		voyageFuelInfo1.setVoyageFuelDetails(voyageDetails1);

		final VoyageDetails voyageDetails2 = new VoyageDetails();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setFromPortSlot(dischargeSlot1);
		options2.setToPortSlot(loadSlot2);
		options2.setDistance(1000);
		options2.setAvailableTime(80);
		options2.setVesselState(VesselState.Ballast);

		FuelInfo voyageFuelInfo2 = new FuelInfo (
				FuelComponent.Base, 1100l, 1.10,
				FuelComponent.Base_Supplemental, 2100l, 2.10,
				FuelComponent.NBO, 3100l, 3.10,
				FuelComponent.FBO, 4100l, 4.10,
				FuelComponent.IdleBase, 5100l, 5.10,
				FuelComponent.IdleNBO, 6100l, 6.10
				);
		
		voyageFuelInfo2.setVoyageFuelDetails(voyageDetails2);

		voyageDetails2.setOptions(options2);

		voyageDetails2.setSpeed(16000);
		// voyageDetails2.setStartTime(220);
		voyageDetails2.setTravelTime(50);
		voyageDetails2.setIdleTime(30);

		final VoyageDetails voyageDetails3 = new VoyageDetails();
		final VoyageOptions options3 = new VoyageOptions();
		options3.setFromPortSlot(loadSlot2);
		options3.setToPortSlot(dischargeSlot2);
		options3.setDistance(1500);
		options3.setAvailableTime(70);
		options3.setVesselState(VesselState.Laden);

		FuelInfo voyageFuelInfo3 = new FuelInfo(
				FuelComponent.Base, 1200l, 1.20,
				FuelComponent.Base_Supplemental, 2200l, 2.20,
				FuelComponent.NBO, 3200l, 3.20,
				FuelComponent.FBO, 4200l, 4.20,
				FuelComponent.IdleBase, 5200l, 5.20,
				FuelComponent.IdleNBO, 6200l, 6.20
				);
		voyageFuelInfo3.setVoyageFuelDetails(voyageDetails3);
		
		voyageDetails3.setOptions(options3);

		voyageDetails3.setSpeed(17000);
		// voyageDetails3.setStartTime(330);
		voyageDetails3.setTravelTime(50);
		voyageDetails3.setIdleTime(20);

		final VoyagePlanAnnotator annotator = new VoyagePlanAnnotator();
		annotator.setPortSlotProvider(portSlotEditor);
		annotator.setVesselProvider(vesselProvider);

		final VoyagePlan plan1 = new VoyagePlan();
		plan1.setSequence(new Object[] { loadDetails1, voyageDetails1, dischargeDetails1, voyageDetails2, loadDetails2 });

		final VoyagePlan plan2 = new VoyagePlan();
		plan2.setSequence(new Object[] { loadDetails2, voyageDetails3, dischargeDetails2 });

		final List<VoyagePlan> plans = new LinkedList<VoyagePlan>();
		plans.add(plan1);
		plans.add(plan2);

		final IResource resource = context.mock(IResource.class);

		context.checking(new Expectations() {
			{
				one(vesselProvider).getVessel(resource);
				will(returnValue(vessel));
			}
		});

		final AnnotatedSolution annotatedSolution = new AnnotatedSolution();
		annotator.annotateFromVoyagePlan(resource, plans, 0, annotatedSolution);
		
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
	
	private int checkPortVisit(IPortVisitEvent portVisit,
			ISequenceElement element, IPortSlot slot, int startTime,
			int duration, long consumption, double cost) {
		
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

	private PortDetails constructPortDetails(IPortSlot slot, int duration, double fuelPrice, long fuelConsumption) {
		PortDetails result = new PortDetails();
		
		PortOptions options = new PortOptions();
		options.setPortSlot(slot);
		options.setVisitDuration(duration);
		
		result.setOptions(options);
		result.setFuelUnitPrice(FuelComponent.Base, OptimiserUnitConvertor.convertToInternalPrice(fuelPrice));
		result.setFuelConsumption(FuelComponent.Base, fuelConsumption);
		
		return result;
	}
	

	/**
	 * 
	 * @author Simon McGregor
	 * 
	 * Convenience test method to specify fuel details (price and consumption) for a VoyageDetails object
	 * in the form
	 * 
	 * new FuelInfo(
	 *    FuelComponent.Base, baseConsumption, basePrice,
	 *    FuelComponent.NBO, nboConsumption, nboPrice,
	 *    ... 
	 * );
	 * 
	 * Use the method setVoyageFuelDetails(VoyageDetails voyage) to set the details for a voyage object.
	 * 
	 * @param details
	 * @param objects
	 */
	private class FuelInfo {
		private HashMap<FuelComponent, Double> costs = new HashMap<FuelComponent, Double>();
		private HashMap<FuelComponent, Long> consumptions = new HashMap<FuelComponent, Long>();
		
		public FuelInfo(Object...objects) {
			for (int i = 0; i < objects.length; i += 3) {
				FuelComponent fuel = (FuelComponent) objects[i];
				Long consumption = (Long) objects[i+1];
				Double price = (Double) objects[i+2];
				costs.put(fuel, price);
				consumptions.put(fuel, consumption);
			}
		}
		
		public void setVoyageFuelDetails(VoyageDetails details) {
			for (FuelComponent fuel: costs.keySet()) {				
				details.setFuelConsumption(fuel, fuel.getDefaultFuelUnit(), consumptions.get(fuel));
				details.setFuelUnitPrice(fuel, OptimiserUnitConvertor.convertToInternalPrice(costs.get(fuel)));
			}
		}
		
		/**
		 * Check that the fuel details for a (travel) journey match the specifications in the FuelInfo object
		 * - idle values should have been set to zero.
		 *  
		 * @param journey
		 */
		public void checkJourneyTravelFuelDetails(IJourneyEvent journey) {
			for (FuelComponent fuel: FuelComponent.getTravelFuelComponents()) {
				if (costs.containsKey(fuel)) {
					long consumption = consumptions.get(fuel);
					double cost = costs.get(fuel);
					Assert.assertEquals(consumption, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals((long) (consumption * cost), journey.getFuelCost(fuel)); 					
				}
			}
			
			for (FuelComponent fuel: FuelComponent.getIdleFuelComponents()) {
				if (costs.containsKey(fuel)) {
					Assert.assertEquals(0l, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals(0l, journey.getFuelCost(fuel)); 					
				}
			}
		}

		/**
		 * Check that the fuel details for a (travel) journey match the specifications in the FuelInfo object
		 * - non-idle values should have been set to zero.
		 *  
		 * @param journey
		 */
		public void checkJourneyIdleFuelDetails(IIdleEvent journey) {
			for (FuelComponent fuel: FuelComponent.getIdleFuelComponents()) {
				if (costs.containsKey(fuel)) {
					long consumption = consumptions.get(fuel);
					double cost = costs.get(fuel);
					Assert.assertEquals(consumption, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals((long) (consumption * cost), journey.getFuelCost(fuel)); 					
				}
			}
			
			for (FuelComponent fuel: FuelComponent.getTravelFuelComponents()) {
				if (costs.containsKey(fuel)) {
					Assert.assertEquals(0l, journey.getFuelConsumption(fuel, fuel.getDefaultFuelUnit()));
					Assert.assertEquals(0l, journey.getFuelCost(fuel)); 					
				}
			}
		}
	}
}
