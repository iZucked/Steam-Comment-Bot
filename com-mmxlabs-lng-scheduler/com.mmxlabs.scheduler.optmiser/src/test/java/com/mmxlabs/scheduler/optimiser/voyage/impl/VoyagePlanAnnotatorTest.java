package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.LinkedList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

@RunWith(JMock.class)
public class VoyagePlanAnnotatorTest {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings("unchecked")
	@Test
	public void testAnnotateFromVoyagePlans() {

		final Object element1 = new Object();
		final Object element2 = new Object();

		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");

		final LoadSlot loadSlot = new LoadSlot();
		loadSlot.setPort(port1);

		final DischargeSlot dischargeSlot = new DischargeSlot();
		dischargeSlot.setPort(port2);

		final IPortSlotProviderEditor<Object> portSlotEditor = new HashMapPortSlotEditor<Object>(
				"name");
		portSlotEditor.setPortSlot(element1, loadSlot);
		portSlotEditor.setPortSlot(element2, dischargeSlot);

		final PortDetails loadDetails = new PortDetails();
		loadDetails.setPortSlot(loadSlot);
		loadDetails.setVisitDuration(10);

		final PortDetails dischargeDetails = new PortDetails();
		dischargeDetails.setPortSlot(dischargeSlot);
		dischargeDetails.setVisitDuration(20);

		final VoyageDetails<Object> voyageDetails = new VoyageDetails<Object>();
		final VoyageOptions options = new VoyageOptions();
		options.setFromPortSlot(loadSlot);
		options.setToPortSlot(dischargeSlot);
		options.setDistance(500);
		
		options.setVesselState(VesselState.Laden);

		voyageDetails.setOptions(options);

		voyageDetails.setIdleTime(100);
		voyageDetails.setSpeed(15000);
		voyageDetails.setTravelTime(200);
		
		voyageDetails.setFuelConsumption(FuelComponent.Base, 1000l);
		voyageDetails.setFuelConsumption(FuelComponent.Base_Supplemental, 2000l);
		voyageDetails.setFuelConsumption(FuelComponent.NBO, 3000l);
		voyageDetails.setFuelConsumption(FuelComponent.FBO, 4000l);
		voyageDetails.setFuelConsumption(FuelComponent.IdleBase, 5000l);
		voyageDetails.setFuelConsumption(FuelComponent.IdleNBO, 6000l);

		final VoyagePlanAnnotator<Object> annotator = new VoyagePlanAnnotator<Object>();
		annotator.setPortSlotProvider(portSlotEditor);

		final VoyagePlan plan = new VoyagePlan();
		plan.setSequence(new Object[] { loadDetails, voyageDetails,
				dischargeDetails });

		final List<IVoyagePlan> plans = new LinkedList<IVoyagePlan>();
		plans.add(plan);

		final IResource resource = context.mock(IResource.class);

		final AnnotatedSequence<Object> annotatedSequence = new AnnotatedSequence<Object>();
		annotator.annonateFromVoyagePlan(resource, plans, annotatedSequence);

		final IJourneyEvent<Object> journey1 = annotatedSequence.getAnnotation(
				element1, SchedulerConstants.AI_journeyInfo,
				IJourneyEvent.class);
		Assert.assertNull(journey1);

		final IIdleEvent<Object> idle1 = annotatedSequence.getAnnotation(
				element1, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
		Assert.assertNull(idle1);

		final IPortVisitEvent<Object> portVisit1 = annotatedSequence
				.getAnnotation(element1, SchedulerConstants.AI_visitInfo,
						IPortVisitEvent.class);
		Assert.assertNotNull(portVisit1);
		Assert.assertSame(element1, portVisit1.getSequenceElement());
		Assert.assertSame(port1, portVisit1.getPort());
		Assert.assertEquals(0, portVisit1.getStartTime());
		Assert.assertEquals(10, portVisit1.getEndTime());
		Assert.assertEquals(10, portVisit1.getDuration());

		final IJourneyEvent<Object> journey2 = annotatedSequence.getAnnotation(
				element2, SchedulerConstants.AI_journeyInfo,
				IJourneyEvent.class);
		Assert.assertNotNull(journey2);

		Assert.assertSame(element2, journey2.getSequenceElement());
		Assert.assertSame(port1, journey2.getFromPort());
		Assert.assertSame(port2, journey2.getToPort());
		Assert.assertEquals(10, journey2.getStartTime());
		Assert.assertEquals(210, journey2.getEndTime());
		Assert.assertEquals(200, journey2.getDuration());
		Assert.assertEquals(500, journey2.getDistance());

		Assert.assertEquals(1000l, journey2.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(2000l, journey2.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(3000l, journey2.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(4000l, journey2.getFuelConsumption(FuelComponent.FBO));
		
		Assert.assertEquals(0l, journey2.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(0l, journey2.getFuelConsumption(FuelComponent.IdleNBO));

		
		Assert.assertEquals(1000l, journey2.getFuelCost(FuelComponent.Base));
		Assert.assertEquals(2000l, journey2.getFuelCost(FuelComponent.Base_Supplemental));
		Assert.assertEquals(3000l, journey2.getFuelCost(FuelComponent.NBO));
		Assert.assertEquals(4000l, journey2.getFuelCost(FuelComponent.FBO));
		
		Assert.assertEquals(0l, journey2.getFuelCost(FuelComponent.IdleBase));
		Assert.assertEquals(0l, journey2.getFuelCost(FuelComponent.IdleNBO));

		
		Assert.assertEquals(VesselState.Laden, journey2.getVesselState());
		
		final IIdleEvent<Object> idle2 = annotatedSequence.getAnnotation(
				element2, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
		Assert.assertNotNull(idle2);

		Assert.assertSame(element2, idle2.getSequenceElement());
		Assert.assertSame(port2, idle2.getPort());
		Assert.assertEquals(210, idle2.getStartTime());
		Assert.assertEquals(310, idle2.getEndTime());
		Assert.assertEquals(100, idle2.getDuration());
		
		Assert.assertEquals(VesselState.Laden, idle2.getVesselState());
		
		Assert.assertEquals(0, idle2.getFuelConsumption(FuelComponent.Base));
		Assert.assertEquals(0, idle2.getFuelConsumption(FuelComponent.Base_Supplemental));
		Assert.assertEquals(0, idle2.getFuelConsumption(FuelComponent.NBO));
		Assert.assertEquals(0, idle2.getFuelConsumption(FuelComponent.FBO));
		
		Assert.assertEquals(5000l, idle2.getFuelConsumption(FuelComponent.IdleBase));
		Assert.assertEquals(6000l, idle2.getFuelConsumption(FuelComponent.IdleNBO));
		
		Assert.assertEquals(0, idle2.getFuelCost(FuelComponent.Base));
		Assert.assertEquals(0, idle2.getFuelCost(FuelComponent.Base_Supplemental));
		Assert.assertEquals(0, idle2.getFuelCost(FuelComponent.NBO));
		Assert.assertEquals(0, idle2.getFuelCost(FuelComponent.FBO));
		
		Assert.assertEquals(5000l, idle2.getFuelCost(FuelComponent.IdleBase));
		Assert.assertEquals(6000l, idle2.getFuelCost(FuelComponent.IdleNBO));
		

		final IPortVisitEvent<Object> portVisit2 = annotatedSequence
				.getAnnotation(element2, SchedulerConstants.AI_visitInfo,
						IPortVisitEvent.class);
		Assert.assertNotNull(portVisit2);
		Assert.assertSame(element2, portVisit2.getSequenceElement());
		Assert.assertSame(port2, portVisit2.getPort());
		Assert.assertEquals(310, portVisit2.getStartTime());
		Assert.assertEquals(330, portVisit2.getEndTime());
		Assert.assertEquals(20, portVisit2.getDuration());

	}
}
