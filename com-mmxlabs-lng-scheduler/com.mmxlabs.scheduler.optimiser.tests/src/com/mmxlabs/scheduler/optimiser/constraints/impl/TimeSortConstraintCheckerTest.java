/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@SuppressWarnings("null")
public class TimeSortConstraintCheckerTest {

	@Test
	public void test1() {

		final String name = "name";
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final TimeSortConstraintChecker checker = createChecker(name, portSlotProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);
		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class);
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class);

		Mockito.when(portSlotProvider.getPortSlot(element1)).thenReturn(slot1);
		Mockito.when(portSlotProvider.getPortSlot(element2)).thenReturn(slot2);

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class);
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class);

		Mockito.when(slot1.getTimeWindow()).thenReturn(tw1);
		Mockito.when(slot2.getTimeWindow()).thenReturn(tw2);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(1);
		Mockito.when(tw2.getInclusiveStart()).thenReturn(2);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(3);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));

		Mockito.when(tw2.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(1);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(2);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(3);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
	}

	/**
	 * Overlapping windows
	 */
	@Test
	public void test2() {

		final String name = "name";
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final TimeSortConstraintChecker checker = createChecker(name, portSlotProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);

		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class);
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class);

		Mockito.when(portSlotProvider.getPortSlot(element1)).thenReturn(slot1);
		Mockito.when(portSlotProvider.getPortSlot(element2)).thenReturn(slot2);

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class);
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class);

		Mockito.when(slot1.getTimeWindow()).thenReturn(tw1);
		Mockito.when(slot2.getTimeWindow()).thenReturn(tw2);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(2);
		Mockito.when(tw2.getInclusiveStart()).thenReturn(1);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(3);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));

		Mockito.when(tw2.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(2);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(1);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(3);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));
	}

	/**
	 * Overlapping windows
	 */
	@Test
	public void test3() {

		final String name = "name";
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final TimeSortConstraintChecker checker = createChecker(name, portSlotProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);
		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class);
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class);

		Mockito.when(portSlotProvider.getPortSlot(element1)).thenReturn(slot1);
		Mockito.when(portSlotProvider.getPortSlot(element2)).thenReturn(slot2);

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class);
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class);

		Mockito.when(slot1.getTimeWindow()).thenReturn(tw1);
		Mockito.when(slot2.getTimeWindow()).thenReturn(tw2);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(3);
		Mockito.when(tw2.getInclusiveStart()).thenReturn(1);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(2);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));

		Mockito.when(tw2.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(3);

		Mockito.when(tw1.getInclusiveStart()).thenReturn(1);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(2);

		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource1));
	}

	/**
	 * Check difference between cargo shorts and normal routes
	 */
	@Test
	public void test4() {

		final String name = "name";
		final IPortSlotProvider portSlotProvider = Mockito.mock(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = Mockito.mock(IVesselProvider.class);
		final IPortTypeProvider portTypeProvider = Mockito.mock(IPortTypeProvider.class);
		final TimeSortConstraintChecker checker = createChecker(name, portSlotProvider, vesselProvider, portTypeProvider);

		final IResource resource1 = Mockito.mock(IResource.class);
		final IVessel vessel1 = Mockito.mock(IVessel.class);

		final IResource resource2 = Mockito.mock(IResource.class);
		final IVessel vessel2 = Mockito.mock(IVessel.class);

		final IVesselAvailability vesselAvailability1 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability1.getVessel()).thenReturn(vessel1);
		final IVesselAvailability vesselAvailability2 = Mockito.mock(IVesselAvailability.class);
		Mockito.when(vesselAvailability2.getVessel()).thenReturn(vessel2);

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		Mockito.when(vesselProvider.getVesselAvailability(resource1)).thenReturn(vesselAvailability1);
		Mockito.when(vesselAvailability1.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		Mockito.when(vesselProvider.getVesselAvailability(resource2)).thenReturn(vesselAvailability2);
		Mockito.when(vesselAvailability2.getVesselInstanceType()).thenReturn(VesselInstanceType.ROUND_TRIP);

		final IPortSlot slot1 = Mockito.mock(IPortSlot.class);
		final IPortSlot slot2 = Mockito.mock(IPortSlot.class);

		Mockito.when(portSlotProvider.getPortSlot(element1)).thenReturn(slot1);
		Mockito.when(portSlotProvider.getPortSlot(element2)).thenReturn(slot2);

		final ITimeWindow tw1 = Mockito.mock(ITimeWindow.class);
		final ITimeWindow tw2 = Mockito.mock(ITimeWindow.class);

		Mockito.when(slot1.getTimeWindow()).thenReturn(tw1);
		Mockito.when(slot2.getTimeWindow()).thenReturn(tw2);

		Mockito.when(tw2.getInclusiveStart()).thenReturn(0);
		Mockito.when(tw2.getExclusiveEnd()).thenReturn(1);
		Mockito.when(tw1.getInclusiveStart()).thenReturn(2);
		Mockito.when(tw1.getExclusiveEnd()).thenReturn(3);

		// First case, Discharge -> Load does not apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Discharge);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Load);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertTrue(checker.checkPairwiseConstraint(element1, element2, resource2));

		// First case, Load -> Discharge does apply on cargo shorts
		Mockito.when(portTypeProvider.getPortType(element1)).thenReturn(PortType.Load);
		Mockito.when(portTypeProvider.getPortType(element2)).thenReturn(PortType.Discharge);

		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource1));
		Assert.assertFalse(checker.checkPairwiseConstraint(element1, element2, resource2));

	}

	private TimeSortConstraintChecker createChecker(final String name, final IPortSlotProvider portSlotProvider, final IVesselProvider vesselProvider, final IPortTypeProvider portTypeProvider) {

		final AbstractModule module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
			}

			@Provides
			TimeSortConstraintChecker create(final Injector injector) {
				final TimeSortConstraintChecker checker = new TimeSortConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}
		};
		final Injector injector = Guice.createInjector(module);
		return injector.getInstance(TimeSortConstraintChecker.class);
	}
}
