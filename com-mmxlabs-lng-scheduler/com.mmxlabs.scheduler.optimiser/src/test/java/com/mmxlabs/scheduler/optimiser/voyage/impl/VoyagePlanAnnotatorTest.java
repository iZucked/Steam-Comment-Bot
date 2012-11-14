/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

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
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
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

		final PortDetails loadDetails1 = new PortDetails();
		loadDetails1.setOptions(new PortOptions());
		loadDetails1.getOptions().setPortSlot(loadSlot1);
		loadDetails1.getOptions().setVisitDuration(10);

		final PortDetails dischargeDetails1 = new PortDetails();
		dischargeDetails1.setOptions(new PortOptions());
		dischargeDetails1.getOptions().setPortSlot(dischargeSlot1);
		dischargeDetails1.getOptions().setVisitDuration(20);

		final PortDetails loadDetails2 = new PortDetails();
		loadDetails2.setOptions(new PortOptions());
		loadDetails2.getOptions().setPortSlot(loadSlot2);
		loadDetails2.getOptions().setVisitDuration(30);

		final PortDetails dischargeDetails2 = new PortDetails();
		dischargeDetails2.setOptions(new PortOptions());
		dischargeDetails2.getOptions().setPortSlot(dischargeSlot2);
		dischargeDetails2.getOptions().setVisitDuration(40);

		final VoyageDetails voyageDetails1 = new VoyageDetails();
		final VoyageOptions options1 = new VoyageOptions();
		options1.setFromPortSlot(loadSlot1);
		options1.setToPortSlot(dischargeSlot1);
		options1.setDistance(500);
		options1.setAvailableTime(90);
		options1.setVesselState(VesselState.Laden);

		voyageDetails1.setFuelUnitPrice(FuelComponent.Base, OptimiserUnitConvertor.convertToInternalPrice(1.0));
		voyageDetails1.setFuelUnitPrice(FuelComponent.Base_Supplemental, OptimiserUnitConvertor.convertToInternalPrice(2.0));
		voyageDetails1.setFuelUnitPrice(FuelComponent.NBO, OptimiserUnitConvertor.convertToInternalPrice(3.0));
		voyageDetails1.setFuelUnitPrice(FuelComponent.FBO, OptimiserUnitConvertor.convertToInternalPrice(4.0));
		voyageDetails1.setFuelUnitPrice(FuelComponent.IdleBase, OptimiserUnitConvertor.convertToInternalPrice(5.0));
		voyageDetails1.setFuelUnitPrice(FuelComponent.IdleNBO, OptimiserUnitConvertor.convertToInternalPrice(6.0));

		voyageDetails1.setOptions(options1);

		voyageDetails1.setSpeed(15000);
		// voyageDetails1.setStartTime(110);
		voyageDetails1.setTravelTime(50);
		voyageDetails1.setIdleTime(40);

		voyageDetails1.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1000l);
		voyageDetails1.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 2000l);
		voyageDetails1.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3000l);
		voyageDetails1.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4000l);
		voyageDetails1.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5000l);
		voyageDetails1.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6000l);

		final VoyageDetails voyageDetails2 = new VoyageDetails();
		final VoyageOptions options2 = new VoyageOptions();
		options2.setFromPortSlot(dischargeSlot1);
		options2.setToPortSlot(loadSlot2);
		options2.setDistance(1000);
		options2.setAvailableTime(80);
		options2.setVesselState(VesselState.Ballast);

		voyageDetails2.setFuelUnitPrice(FuelComponent.Base, OptimiserUnitConvertor.convertToInternalPrice(1.10));
		voyageDetails2.setFuelUnitPrice(FuelComponent.Base_Supplemental, OptimiserUnitConvertor.convertToInternalPrice(2.1));
		voyageDetails2.setFuelUnitPrice(FuelComponent.NBO, OptimiserUnitConvertor.convertToInternalPrice(3.1));
		voyageDetails2.setFuelUnitPrice(FuelComponent.FBO, OptimiserUnitConvertor.convertToInternalPrice(4.1));
		voyageDetails2.setFuelUnitPrice(FuelComponent.IdleBase, OptimiserUnitConvertor.convertToInternalPrice(5.1));
		voyageDetails2.setFuelUnitPrice(FuelComponent.IdleNBO, OptimiserUnitConvertor.convertToInternalPrice(6.1));

		voyageDetails2.setOptions(options2);

		voyageDetails2.setSpeed(16000);
		// voyageDetails2.setStartTime(220);
		voyageDetails2.setTravelTime(50);
		voyageDetails2.setIdleTime(30);

		voyageDetails2.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1100l);
		voyageDetails2.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 2100l);
		voyageDetails2.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3100l);
		voyageDetails2.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4100l);
		voyageDetails2.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5100l);
		voyageDetails2.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6100l);

		final VoyageDetails voyageDetails3 = new VoyageDetails();
		final VoyageOptions options3 = new VoyageOptions();
		options3.setFromPortSlot(loadSlot2);
		options3.setToPortSlot(dischargeSlot2);
		options3.setDistance(1500);
		options3.setAvailableTime(70);
		options3.setVesselState(VesselState.Laden);

		voyageDetails3.setFuelUnitPrice(FuelComponent.Base, OptimiserUnitConvertor.convertToInternalPrice(1.2));
		voyageDetails3.setFuelUnitPrice(FuelComponent.Base_Supplemental, OptimiserUnitConvertor.convertToInternalPrice(2.2));
		voyageDetails3.setFuelUnitPrice(FuelComponent.NBO, OptimiserUnitConvertor.convertToInternalPrice(3.2));
		voyageDetails3.setFuelUnitPrice(FuelComponent.FBO, OptimiserUnitConvertor.convertToInternalPrice(4.2));
		voyageDetails3.setFuelUnitPrice(FuelComponent.IdleBase, OptimiserUnitConvertor.convertToInternalPrice(5.2));
		voyageDetails3.setFuelUnitPrice(FuelComponent.IdleNBO, OptimiserUnitConvertor.convertToInternalPrice(6.2));

		voyageDetails3.setOptions(options3);

		voyageDetails3.setSpeed(17000);
		// voyageDetails3.setStartTime(330);
		voyageDetails3.setTravelTime(50);
		voyageDetails3.setIdleTime(20);

		voyageDetails3.setFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit(), 1200l);
		voyageDetails3.setFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit(), 2200l);
		voyageDetails3.setFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit(), 3200l);
		voyageDetails3.setFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit(), 4200l);
		voyageDetails3.setFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit(), 5200l);
		voyageDetails3.setFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit(), 6200l);

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
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element1, portVisit.getSequenceElement());
			Assert.assertSame(loadSlot1, portVisit.getPortSlot());
			Assert.assertEquals(0, portVisit.getStartTime());
			Assert.assertEquals(10, portVisit.getEndTime());
			Assert.assertEquals(10, portVisit.getDuration());

			// Assert.assertTrue(portVisit instanceof ILoadEvent<?>);
			// ILoadEvent loadEvent = (ILoadEvent) portVisit;
			// Assert.assertEquals(400, loadEvent.getLoadVolume());
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

			Assert.assertEquals(1000l, journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2000l, journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3000l, journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4000l, journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1000l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4000l, journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(9000l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(16000l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element2, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element2, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(60, idle.getStartTime());
			Assert.assertEquals(100, idle.getEndTime());
			Assert.assertEquals(40, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5000l, idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6000l, idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(25000l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(36000l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element2, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element2, portVisit.getSequenceElement());
			Assert.assertSame(dischargeSlot1, portVisit.getPortSlot());
			Assert.assertEquals(100, portVisit.getStartTime());
			Assert.assertEquals(120, portVisit.getEndTime());
			Assert.assertEquals(20, portVisit.getDuration());

			// Assert.assertTrue(portVisit instanceof IDischargeEvent<?>);
			// IDischargeEvent dischargeEvent = (IDischargeEvent) portVisit;
			// Assert.assertEquals(300, dischargeEvent.getDischargeVolume());
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

			Assert.assertEquals(1100l, journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2100l, journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3100l, journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4100l, journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1210l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4410l, journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(9610l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(16810l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Ballast, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element3, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element3, idle.getSequenceElement());
			Assert.assertSame(port1, idle.getPort());
			Assert.assertEquals(170, idle.getStartTime());
			Assert.assertEquals(200, idle.getEndTime());
			Assert.assertEquals(30, idle.getDuration());

			Assert.assertEquals(VesselState.Ballast, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5100l, idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6100l, idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(26010l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(37210l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element3, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element3, portVisit.getSequenceElement());
			Assert.assertSame(loadSlot2, portVisit.getPortSlot());
			Assert.assertEquals(200, portVisit.getStartTime());
			Assert.assertEquals(230, portVisit.getEndTime());
			Assert.assertEquals(30, portVisit.getDuration());

			// Assert.assertTrue(portVisit instanceof ILoadEvent<?>);
			// ILoadEvent loadEvent = (ILoadEvent) portVisit;
			// Assert.assertEquals(200, loadEvent.getLoadVolume());
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

			Assert.assertEquals(1200l, journey.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(2200l, journey.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(3200l, journey.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(4200l, journey.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(0l, journey.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(1440l, journey.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(4840l, journey.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(10240l, journey.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(17640l, journey.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(0l, journey.getFuelCost(FuelComponent.IdleNBO));

			Assert.assertEquals(VesselState.Laden, journey.getVesselState());

			final IIdleEvent idle = annotatedSolution.getElementAnnotations().getAnnotation(element4, SchedulerConstants.AI_idleInfo, IIdleEvent.class);
			Assert.assertNotNull(idle);

			Assert.assertSame(element4, idle.getSequenceElement());
			Assert.assertSame(port2, idle.getPort());
			Assert.assertEquals(280, idle.getStartTime());
			Assert.assertEquals(300, idle.getEndTime());
			Assert.assertEquals(20, idle.getDuration());

			Assert.assertEquals(VesselState.Laden, idle.getVesselState());

			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base, FuelComponent.Base.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.Base_Supplemental, FuelComponent.Base_Supplemental.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.NBO, FuelComponent.NBO.getDefaultFuelUnit()));
			Assert.assertEquals(0, idle.getFuelConsumption(FuelComponent.FBO, FuelComponent.FBO.getDefaultFuelUnit()));

			Assert.assertEquals(5200l, idle.getFuelConsumption(FuelComponent.IdleBase, FuelComponent.IdleBase.getDefaultFuelUnit()));
			Assert.assertEquals(6200l, idle.getFuelConsumption(FuelComponent.IdleNBO, FuelComponent.IdleNBO.getDefaultFuelUnit()));

			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.Base_Supplemental));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.NBO));
			Assert.assertEquals(0, idle.getFuelCost(FuelComponent.FBO));

			Assert.assertEquals(27040l, idle.getFuelCost(FuelComponent.IdleBase));
			Assert.assertEquals(38440l, idle.getFuelCost(FuelComponent.IdleNBO));

			final IPortVisitEvent portVisit = annotatedSolution.getElementAnnotations().getAnnotation(element4, SchedulerConstants.AI_visitInfo, IPortVisitEvent.class);
			Assert.assertNotNull(portVisit);
			Assert.assertSame(element4, portVisit.getSequenceElement());
			Assert.assertSame(dischargeSlot2, portVisit.getPortSlot());
			Assert.assertEquals(300, portVisit.getStartTime());
			Assert.assertEquals(340, portVisit.getEndTime());
			Assert.assertEquals(40, portVisit.getDuration());
			//
			// Assert.assertTrue(portVisit instanceof IDischargeEvent<?>);
			// IDischargeEvent dischargeEvent = (IDischargeEvent) portVisit;
			// Assert.assertEquals(100, dischargeEvent.getDischargeVolume());

		}
	}
}
