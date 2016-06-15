/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;

public class ShippingHoursRestrictionConstraintCheckerTest {

	@Test
	public void testName() {
		final String name = "ShippingHoursRestrictionChecker";
		final ShippingHoursRestrictionChecker checker = new ShippingHoursRestrictionChecker();

		Assert.assertSame(name, checker.getName());
	}

	@SuppressWarnings("null")
	@Test
	public void testConstraintPasses() {
		final Pair<ISequenceElement[], ShippingHoursRestrictionChecker> testData = createTester(1230, new Pair<>(661, 1227));
		final ShippingHoursRestrictionChecker shippingHoursRestrictionChecker = testData.getSecond();
		// Mock resource here is ok as #createTester works with any resource
		Assert.assertTrue(shippingHoursRestrictionChecker.checkPairwiseConstraint(testData.getFirst()[0], testData.getFirst()[1], Mockito.mock(IResource.class)));
	}

	@SuppressWarnings("null")
	@Test
	public void testConstraintFails() {
		final Pair<ISequenceElement[], ShippingHoursRestrictionChecker> testData = createTester(1220, new Pair<>(661, 1227));
		final ShippingHoursRestrictionChecker shippingHoursRestrictionChecker = testData.getSecond();
		// Mock resource here is ok as #createTester works with any resource
		Assert.assertFalse(shippingHoursRestrictionChecker.checkPairwiseConstraint(testData.getFirst()[0], testData.getFirst()[1], Mockito.mock(IResource.class)));
	}

	@SuppressWarnings("null")
	private Pair<ISequenceElement[], ShippingHoursRestrictionChecker> createTester(final int shippingHoursRestriction, final Pair<Integer, Integer> desTimes) {
		final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor();
		final IActualsDataProvider actualsDataProvider = Mockito.mock(IActualsDataProvider.class);
		final IShippingHoursRestrictionProvider shippingHoursRestrictionProvider = Mockito.mock(IShippingHoursRestrictionProvider.class);
		final IVesselProvider vp = Mockito.mock(IVesselProvider.class, "vp");
		final INominatedVesselProvider nvp = Mockito.mock(INominatedVesselProvider.class, "nvp");
		final IDivertableDESShippingTimesCalculator divertableDESShippingTimesCalculator = Mockito.mock(IDivertableDESShippingTimesCalculator.class);

		// check empty behaviour
		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");

		final ILoadSlot s1 = Mockito.mock(ILoadSlot.class, "s1");
		// discharge slot s2 should be OK
		final IDischargeSlot s2 = Mockito.mock(IDischargeSlot.class, "s2");
		// discharge slot s3 should be out of bounds (load CV too low)
		final IDischargeSlot s3 = Mockito.mock(IDischargeSlot.class, "s3");
		// discharge slot s3 should be out of bounds (load CV too high)
		final IDischargeSlot s4 = Mockito.mock(IDischargeSlot.class, "s4");

		portSlotProvider.setPortSlot(o1, s1);
		portSlotProvider.setPortSlot(o2, s2);
		portSlotProvider.setPortSlot(o3, s3);
		portSlotProvider.setPortSlot(o4, s4);

		Mockito.when(actualsDataProvider.hasActuals(Matchers.any(IPortSlot.class))).thenReturn(false);
		Mockito.when(shippingHoursRestrictionProvider.getShippingHoursRestriction(o1)).thenReturn(shippingHoursRestriction);
		Mockito.when(shippingHoursRestrictionProvider.isDivertable(Matchers.any(ISequenceElement.class))).thenReturn(true);
		final ITimeWindow timeWindow = Mockito.mock(ITimeWindow.class);
		Mockito.when(timeWindow.getInclusiveStart()).thenReturn(0);
		Mockito.when(shippingHoursRestrictionProvider.getBaseTime(o1)).thenReturn(timeWindow);

		Mockito.when(divertableDESShippingTimesCalculator.getDivertableDESTimes(Matchers.any(ILoadOption.class), Matchers.any(IDischargeOption.class), Matchers.any(IVessel.class),
				Matchers.any(IResource.class))).thenReturn(desTimes);

		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final IVessel vessel = Mockito.mock(IVessel.class);
		Mockito.when(vesselAvailability.getVesselInstanceType()).thenReturn(VesselInstanceType.DES_PURCHASE);
		Mockito.when(vp.getVesselAvailability(Matchers.any(IResource.class))).thenReturn(vesselAvailability);
		Mockito.when(nvp.getNominatedVessel(Matchers.any(IResource.class))).thenReturn(vessel);
		Mockito.when(nvp.getNominatedVessel(Matchers.any(ISequenceElement.class))).thenReturn(vessel);

		final ShippingHoursRestrictionChecker shippingHoursRestrictionChecker = createChecker(portSlotProvider, actualsDataProvider, shippingHoursRestrictionProvider, vp, nvp,
				divertableDESShippingTimesCalculator);
		return new Pair<>(new ISequenceElement[] { o1, o2 }, shippingHoursRestrictionChecker);
	}

	private ShippingHoursRestrictionChecker createChecker(final IPortSlotProviderEditor portSlotProvider, final IActualsDataProvider actualsDataProvider,
			final IShippingHoursRestrictionProvider shippingHoursRestrictionProvider, final IVesselProvider vp, final INominatedVesselProvider nvp,
			final IDivertableDESShippingTimesCalculator divertableDESShippingTimesCalculator) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
				bind(IActualsDataProvider.class).toInstance(actualsDataProvider);
				bind(IShippingHoursRestrictionProvider.class).toInstance(shippingHoursRestrictionProvider);
				bind(IVesselProvider.class).toInstance(vp);
				bind(INominatedVesselProvider.class).toInstance(nvp);
				bind(IDivertableDESShippingTimesCalculator.class).toInstance(divertableDESShippingTimesCalculator);
			}

			@Provides
			ShippingHoursRestrictionChecker create(final Injector injector) {
				final ShippingHoursRestrictionChecker checker = new ShippingHoursRestrictionChecker();
				injector.injectMembers(checker);
				return checker;
			}

		});
		return injector.getInstance(ShippingHoursRestrictionChecker.class);
	}
}
