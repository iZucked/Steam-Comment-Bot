package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.LinkedList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.events.IDischargeEvent;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@RunWith(JMock.class)
public class VoyagePlanAnnotatorTest {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testAnnotateFromVoyagePlans() {

		final Object element1 = new Object();
		final Object element2 = new Object();
		final Object element3 = new Object();
		final Object element4 = new Object();

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

		final IPortSlotProviderEditor<Object> portSlotEditor = new HashMapPortSlotEditor<Object>(
				"name");
		portSlotEditor.setPortSlot(element1, loadSlot1);
		portSlotEditor.setPortSlot(element2, dischargeSlot1);
		portSlotEditor.setPortSlot(element3, loadSlot2);
		portSlotEditor.setPortSlot(element4, dischargeSlot2);

		final PortDetails loadDetails1 = new PortDetails();
		loadDetails1.setPortSlot(loadSlot1);
		
		loadDetails1.setVisitDuration(10);
		loadDetails1.setStartTime(100);
		
		
		final PortDetails dischargeDetails1 = new PortDetails();
		dischargeDetails1.setPortSlot(dischargeSlot1);
		dischargeDetails1.setVisitDuration(20);
		dischargeDetails1.setStartTime(200);

		final PortDetails loadDetails2 = new PortDetails();
		loadDetails2.setPortSlot(loadSlot2);
		loadDetails2.setVisitDuration(30);
		loadDetails2.setStartTime(300);

		final PortDetails dischargeDetails2 = new PortDetails();
		dischargeDetails2.setPortSlot(dischargeSlot2);
		dischargeDetails2.setVisitDuration(40);
		dischargeDetails2.setStartTime(400);

		final VoyageDetails<Object> voyageDetails1 = new VoyageDetails<Object>();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setFromPortSlot(loadSlot1);
		options1.setToPortSlot(dischargeSlot1);
		options1.setDistance(500);

		options1.setVesselState(VesselState.Laden);

		voyageDetails1.setFuelUnitPrice(FuelComponent.Base, 1000);
		voyageDetails1.setFuelUnitPrice(FuelComponent.Base_Supplemental, 2000);
		voyageDetails1.setFuelUnitPrice(FuelComponent.NBO, 3000);
		voyageDetails1.setFuelUnitPrice(FuelComponent.FBO, 4000);
		voyageDetails1.setFuelUnitPrice(FuelComponent.IdleBase, 5000);
		voyageDetails1.setFuelUnitPrice(FuelComponent.IdleNBO, 6000);

		voyageDetails1.setOptions(options1);

		voyageDetails1.setSpeed(15000);
		voyageDetails1.setStartTime(110);
		voyageDetails1.setTravelTime(50);
		voyageDetails1.setIdleTime(40);

		voyageDetails1.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1000l);
		voyageDetails1.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 
				2000l);
		voyageDetails1.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3000l);
		voyageDetails1.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4000l);
		voyageDetails1.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5000l);
		voyageDetails1.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6000l);

		
		final VoyageDetails<Object> voyageDetails2 = new VoyageDetails<Object>();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setFromPortSlot(dischargeSlot1);
		options2.setToPortSlot(loadSlot2);
		options2.setDistance(1000);

		options2.setVesselState(VesselState.Ballast);

		voyageDetails2.setFuelUnitPrice(FuelComponent.Base, 1100);
		voyageDetails2.setFuelUnitPrice(FuelComponent.Base_Supplemental, 2100);
		voyageDetails2.setFuelUnitPrice(FuelComponent.NBO, 3100);
		voyageDetails2.setFuelUnitPrice(FuelComponent.FBO, 4100);
		voyageDetails2.setFuelUnitPrice(FuelComponent.IdleBase, 5100);
		voyageDetails2.setFuelUnitPrice(FuelComponent.IdleNBO, 6100);

		voyageDetails2.setOptions(options2);

		voyageDetails2.setSpeed(16000);
		voyageDetails2.setStartTime(220);
		voyageDetails2.setTravelTime(50);
		voyageDetails2.setIdleTime(30);
		
		
		voyageDetails2.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1100l);
		voyageDetails2.setFuelConsumption(FuelComponent.Base_Supplemental,FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 
				2100l);
		voyageDetails2.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3100l);
		voyageDetails2.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4100l);
		voyageDetails2.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5100l);
		voyageDetails2.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6100l);

		final VoyageDetails<Object> voyageDetails3 = new VoyageDetails<Object>();
		final VoyageOptions options3 = new VoyageOptions();
		options3.setFromPortSlot(loadSlot2);
		options3.setToPortSlot(dischargeSlot2);
		options3.setDistance(1500);

		options3.setVesselState(VesselState.Laden);

		voyageDetails3.setFuelUnitPrice(FuelComponent.Base, 1200);
		voyageDetails3.setFuelUnitPrice(FuelComponent.Base_Supplemental, 2200);
		voyageDetails3.setFuelUnitPrice(FuelComponent.NBO, 3200);
		voyageDetails3.setFuelUnitPrice(FuelComponent.FBO, 4200);
		voyageDetails3.setFuelUnitPrice(FuelComponent.IdleBase, 5200);
		voyageDetails3.setFuelUnitPrice(FuelComponent.IdleNBO, 6200);

		voyageDetails3.setOptions(options3);

		voyageDetails3.setSpeed(17000);
		voyageDetails3.setStartTime(330);
		voyageDetails3.setTravelTime(50);
		voyageDetails3.setIdleTime(20);

		voyageDetails3.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1200l);
		voyageDetails3.setFuelConsumption(FuelComponent.Base_Supplemental,FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 
				2200l);
		voyageDetails3.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3200l);
		voyageDetails3.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4200l);
		voyageDetails3.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5200l);
		voyageDetails3.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6200l);

		final VoyagePlanAnnotator<Object> annotator = new VoyagePlanAnnotator<Object>();
		annotator.setPortSlotProvider(portSlotEditor);

		final VoyagePlan plan1 = new VoyagePlan();
		plan1.setSequence(new Object[] { loadDetails1, voyageDetails1,
				dischargeDetails1, voyageDetails2, loadDetails2 });

		final VoyagePlan plan2 = new VoyagePlan();
		plan2.setSequence(new Object[] { loadDetails2, voyageDetails3,
				dischargeDetails2 });

		final List<VoyagePlan> plans = new LinkedList<VoyagePlan>();
		plans.add(plan1);
		plans.add(plan2);

		plan1.setLoadVolume(400);
		plan1.setDischargeVolume(300);
		plan2.setLoadVolume(200);
		plan2.setDischargeVolume(100);

		final IResource resource = context.mock(IResource.class);

		final AnnotatedSequence<Object> annotatedSequence = new AnnotatedSequence<Object>();
		annotator.annotateFromVoyagePlan(resource, plans, annotatedSequence);

		{
			final IJourneyEvent<Object> journey = annotatedSequence
					.getAnnotation(element1, SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNull(journey);

			final IIdleEvent<Object> idle = annotatedSequence.getAnnotation(
					element1, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNull(idle);

			final IPortVisitEvent<Object> portVisit = annotatedSequence
					.getAnnotation(element1, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element1, portVisit.getSequenceElement());
			Assert.assertSame(loadSlot1, portVisit.getPortSlot());
			Assert.assertEquals(100, portVisit.getStartTime());
			Assert.assertEquals(110, portVisit.getEndTime());
			Assert.assertEquals(10, portVisit.getDuration());

			Assert.assertTrue(portVisit instanceof ILoadEvent<?>);
			ILoadEvent loadEvent = (ILoadEvent) portVisit;
			Assert.assertEquals(400, loadEvent.getLoadVolume());
		}
		{
			final IJourneyEvent<Object> journey = annotatedSequence
					.getAnnotation(element2, SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element2, journey.getSequenceElement());
			Assert.assertSame(port1, journey.getFromPort());
			Assert.assertSame(port2, journey.getToPort());
			Assert.assertEquals(110, journey.getStartTime());
			Assert.assertEquals(160, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(500, journey.getDistance());

			Assert.assertEquals(1000l,
					journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2000l,
					journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3000l,
					journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4000l,
					journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1000l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4000l,
					journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(9000l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(16000l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent<Object> idle = annotatedSequence.getAnnotation(
					element2, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element2, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(160, idle.getStartTime());
			Assert.assertEquals(200, idle.getEndTime());
			Assert.assertEquals(40, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0,
					idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5000l,
					idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6000l,
					idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(25000l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(36000l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent<Object> portVisit = annotatedSequence
					.getAnnotation(element2, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element2, portVisit.getSequenceElement());
			Assert.assertSame(dischargeSlot1, portVisit.getPortSlot());
			Assert.assertEquals(200, portVisit.getStartTime());
			Assert.assertEquals(220, portVisit.getEndTime());
			Assert.assertEquals(20, portVisit.getDuration());

			Assert.assertTrue(portVisit instanceof IDischargeEvent<?>);
			IDischargeEvent dischargeEvent = (IDischargeEvent) portVisit;
			Assert.assertEquals(300, dischargeEvent.getDischargeVolume());
		}

		{
			final IJourneyEvent<Object> journey = annotatedSequence
					.getAnnotation(element3, SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element3, journey.getSequenceElement());
			Assert.assertSame(port2, journey.getFromPort());
			Assert.assertSame(port1, journey.getToPort());
			Assert.assertEquals(220, journey.getStartTime());
			Assert.assertEquals(270, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(1000, journey.getDistance());

			Assert.assertEquals(1100l,
					journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2100l,
					journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3100l,
					journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4100l,
					journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1210l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4410l,
					journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(9610l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(16810l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Ballast, journey.getVesselState());

			final IIdleEvent<Object> idle = annotatedSequence.getAnnotation(
					element3, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element3, idle.getSequenceElement());
			Assert.assertSame(port1, idle.getPort());
			Assert.assertEquals(270, idle.getStartTime());
			Assert.assertEquals(300, idle.getEndTime());
			Assert.assertEquals(30, idle.getDuration());

			Assert.assertEquals(VesselState.Ballast, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0,
					idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5100l,
					idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6100l,
					idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(26010l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(37210l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent<Object> portVisit = annotatedSequence
					.getAnnotation(element3, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element3, portVisit.getSequenceElement());
			Assert.assertSame(loadSlot2, portVisit.getPortSlot());
			Assert.assertEquals(300, portVisit.getStartTime());
			Assert.assertEquals(330, portVisit.getEndTime());
			Assert.assertEquals(30, portVisit.getDuration());

			Assert.assertTrue(portVisit instanceof ILoadEvent<?>);
			ILoadEvent loadEvent = (ILoadEvent) portVisit;
			Assert.assertEquals(200, loadEvent.getLoadVolume());
		}

		{
			final IJourneyEvent<Object> journey = annotatedSequence
					.getAnnotation(element4, SchedulerConstants.AI_journeyInfo,
							IJourneyEvent.class);
			Assert.assertNotNull(journey);

			Assert.assertSame(element4, journey.getSequenceElement());
			Assert.assertSame(port1, journey.getFromPort());
			Assert.assertSame(port2, journey.getToPort());
			Assert.assertEquals(330, journey.getStartTime());
			Assert.assertEquals(380, journey.getEndTime());
			Assert.assertEquals(50, journey.getDuration());
			Assert.assertEquals(1500, journey.getDistance());

			Assert.assertEquals(1200l,
					journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2200l,
					journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3200l,
					journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4200l,
					journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l,
					journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1440l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4840l,
					journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(10240l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(17640l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent<Object> idle = annotatedSequence.getAnnotation(
					element4, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element4, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(380, idle.getStartTime());
			Assert.assertEquals(400, idle.getEndTime());
			Assert.assertEquals(20, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0,
					idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5200l,
					idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6200l,
					idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0,
					idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(27040l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(38440l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent<Object> portVisit = annotatedSequence
					.getAnnotation(element4, SchedulerConstants.AI_visitInfo,
							IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element4, portVisit.getSequenceElement());
			Assert.assertSame(dischargeSlot2, portVisit.getPortSlot());
			Assert.assertEquals(400, portVisit.getStartTime());
			Assert.assertEquals(440, portVisit.getEndTime());
			Assert.assertEquals(40, portVisit.getDuration());

			Assert.assertTrue(portVisit instanceof IDischargeEvent<?>);
			IDischargeEvent dischargeEvent = (IDischargeEvent) portVisit;
			Assert.assertEquals(100, dischargeEvent.getDischargeVolume());

		}
	}
}
