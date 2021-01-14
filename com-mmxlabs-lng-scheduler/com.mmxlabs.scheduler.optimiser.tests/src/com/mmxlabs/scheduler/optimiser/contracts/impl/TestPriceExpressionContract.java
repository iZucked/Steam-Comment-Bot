/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * @author Simon McGregor
 * 
 *         Simple JUnit test for the optimiser internal PriceExpressionContract class.
 */
public class TestPriceExpressionContract {
	/**
	 * Test for the PriceExpressionContract.calculateSimpleUnitPrice method.
	 * 
	 * Tests whether the ICurve object supplied as a constructor argument is called with appropriate arguments when calculateSimpleUnitPrice is called. Also tests that the appropriate values are
	 * returned.
	 * 
	 */
	@Test
	public void testCalculateSimpleUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		final IVesselProvider vesselProvider = mock(IVesselProvider.class);
		final PriceExpressionContract contract = createPriceExpressionContract(curve, vesselProvider);

		// create named test constants
		final int p1 = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int p2 = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int t1 = 35;
		final int t2 = 55;

		final IPort port = mock(IPort.class);
		when(port.getTimeZoneId()).thenReturn("UTC");

		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(t1)).thenReturn(p1);
		when(curve.getValueAtPoint(t2)).thenReturn(p2);

		// calculate the unit price at times t1 and t2
		final int price1 = contract.calculateSimpleUnitPrice(t1, port);
		final int price2 = contract.calculateSimpleUnitPrice(t2, port);

		// make sure the mock was evaluated at the right points and nowhere else
		verify(curve).getValueAtPoint(t1);
		verify(curve).getValueAtPoint(t2);
		verifyNoMoreInteractions(curve);

		// check that the returned results are correct
		Assertions.assertEquals(p1, price1);
		Assertions.assertEquals(p2, price2);
	}

	@Test
	public void testCalculateLoadUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		final IVesselProvider vesselProvider = mock(IVesselProvider.class);
		final PriceExpressionContract contract = createPriceExpressionContract(curve, vesselProvider);

		// create named test constants
		final int priceAtLoadTime = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int oriceAtPricingDate = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int loadTime = 120;
		final int loadPricingDate = 90;

		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(loadTime)).thenReturn(priceAtLoadTime);
		when(curve.getValueAtPoint(loadPricingDate)).thenReturn(oriceAtPricingDate);

		final ILoadSlot loadSlotWithPricingDate = mock(ILoadSlot.class);
		when(loadSlotWithPricingDate.getPricingDate()).thenReturn(loadPricingDate);

		final ILoadSlot loadSlotNoPricingDate = mock(ILoadSlot.class);
		when(loadSlotNoPricingDate.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);

		when(loadSlotNoPricingDate.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(loadSlotWithPricingDate.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);

		final IDischargeSlot dischargeSlot = mock(IDischargeSlot.class);

		final int dischargeTime = 170;
		final int dischargePricePerMMBTu = 40;
		final long dischargeVolumeInM3 = 100;
		final long loadVolumeInM3 = 200;
		final IVesselAvailability vesselAvailability = mock(IVesselAvailability.class);
		final VoyagePlan plan = new VoyagePlan();

		final IDetailTree annotations = mock(IDetailTree.class);

		final IAllocationAnnotation allocationAnnotation = mock(IAllocationAnnotation.class);
		when(allocationAnnotation.getSlotTime(loadSlotNoPricingDate)).thenReturn(loadTime);
		when(allocationAnnotation.getSlotTime(loadSlotWithPricingDate)).thenReturn(loadTime);
		when(allocationAnnotation.getSlotTime(dischargeSlot)).thenReturn(dischargeTime);

		when(allocationAnnotation.getCommercialSlotVolumeInM3(loadSlotNoPricingDate)).thenReturn(loadVolumeInM3);
		when(allocationAnnotation.getCommercialSlotVolumeInM3(loadSlotWithPricingDate)).thenReturn(loadVolumeInM3);
		when(allocationAnnotation.getCommercialSlotVolumeInM3(dischargeSlot)).thenReturn(dischargeVolumeInM3);

		final int loadPriceWithPricingDate = contract.calculateFOBPricePerMMBTu(loadSlotWithPricingDate, dischargeSlot, dischargePricePerMMBTu, allocationAnnotation, vesselAvailability, plan, null,
				annotations);

		final int loadPriceNoPricingDate = contract.calculateFOBPricePerMMBTu(loadSlotNoPricingDate, dischargeSlot, dischargePricePerMMBTu, allocationAnnotation, vesselAvailability, plan, null,
				annotations);

		verify(curve).getValueAtPoint(loadPricingDate);
		verify(curve).getValueAtPoint(loadTime);
		verifyNoMoreInteractions(curve);

		verify(loadSlotWithPricingDate, Mockito.atLeastOnce()).getPricingDate();
		verify(loadSlotWithPricingDate, Mockito.never()).getPricingEvent();
		verify(loadSlotWithPricingDate, Mockito.atLeastOnce()).getPort();
		verifyNoMoreInteractions(loadSlotWithPricingDate);

		verify(loadSlotNoPricingDate, Mockito.atLeastOnce()).getPort();
		verify(loadSlotNoPricingDate, Mockito.atLeastOnce()).getPricingDate();
		verify(loadSlotNoPricingDate, Mockito.atLeastOnce()).getPricingEvent();
		verifyNoMoreInteractions(loadSlotNoPricingDate);

		// check that the returned results are correct
		Assertions.assertEquals(priceAtLoadTime, loadPriceNoPricingDate);
		Assertions.assertEquals(oriceAtPricingDate, loadPriceWithPricingDate);

	}

	@Test
	public void testCalculateDischargeUnitPrice() {
		// create a PriceExpressionContract with a mocked ICurve object
		final ICurve curve = mock(ICurve.class);
		final IVesselProvider vesselProvider = mock(IVesselProvider.class);
		final PriceExpressionContract contract = createPriceExpressionContract(curve, vesselProvider);

		// create named test constants
		final int priceAtDischargeTime = (int) OptimiserUnitConvertor.convertToInternalDailyCost(40);
		final int priceAtPricingDate = (int) OptimiserUnitConvertor.convertToInternalDailyCost(70);
		final int dischargeTime = 120;
		final int pricingDate = 90;

		// tell the ICurve mock to return specified values at given points
		when(curve.getValueAtPoint(dischargeTime)).thenReturn(priceAtDischargeTime);
		when(curve.getValueAtPoint(pricingDate)).thenReturn(priceAtPricingDate);

		final ILoadSlot loadSlot = mock(ILoadSlot.class);
		
		final IPort port = mock(IPort.class);
		final IDischargeSlot dischargeSlotWithPricingDate = mock(IDischargeSlot.class);
		when(dischargeSlotWithPricingDate.getPort()).thenReturn(port);
		when(dischargeSlotWithPricingDate.getPricingDate()).thenReturn(pricingDate);

		when(dischargeSlotWithPricingDate.getPricingEvent()).thenReturn(PricingEventType.START_OF_DISCHARGE);

		final IDischargeSlot dischargeSlotNoPricingDate = mock(IDischargeSlot.class);
		when(dischargeSlotNoPricingDate.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);

		final IPortTimesRecord portTimesRecord1 = mock(IPortTimesRecord.class);
		when(portTimesRecord1.getSlots()).thenReturn(Lists.newArrayList(loadSlot, dischargeSlotWithPricingDate));
		when(portTimesRecord1.getSlotTime(dischargeSlotWithPricingDate)).thenReturn(dischargeTime);

		when(dischargeSlotNoPricingDate.getPricingEvent()).thenReturn(PricingEventType.START_OF_DISCHARGE);

		final int salesPriceWithPricingDate = contract.estimateSalesUnitPrice(dischargeSlotWithPricingDate, portTimesRecord1, null);
		verify(curve).getValueAtPoint(pricingDate);

		final IPortTimesRecord portTimesRecord2 = mock(IPortTimesRecord.class);
		when(portTimesRecord2.getSlotTime(dischargeSlotNoPricingDate)).thenReturn(dischargeTime);
		when(portTimesRecord2.getSlots()).thenReturn(Lists.newArrayList(loadSlot, dischargeSlotNoPricingDate));

		final int salesPriceNoPricingDate = contract.estimateSalesUnitPrice(dischargeSlotNoPricingDate, portTimesRecord2, null);

		verify(curve).getValueAtPoint(dischargeTime);
		verifyNoMoreInteractions(curve);

		// check that the returned results are correct
		Assertions.assertEquals(priceAtDischargeTime, salesPriceNoPricingDate);
		Assertions.assertEquals(priceAtPricingDate, salesPriceWithPricingDate);

	}

	private PriceExpressionContract createPriceExpressionContract(final ICurve curve, @NonNull final IVesselProvider vesselProvider) {
		final IIntegerIntervalCurve integerIntervalCurve = new IntegerIntervalCurve();
		for (int i = 0; i < 2000; i++) {
			if (i % 31 == 0) {
				integerIntervalCurve.add(i);
			}
		}
		final PriceExpressionContract contract = new PriceExpressionContract(curve, integerIntervalCurve);

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).to(TimeZoneToUtcOffsetProvider.class);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IVesselBaseFuelCalculator.class).toInstance(Mockito.mock(IVesselBaseFuelCalculator.class));
				bind(ICharterRateCalculator.class).toInstance(Mockito.mock(ICharterRateCalculator.class));
				bind(IElementDurationProvider.class).toInstance(Mockito.mock(IElementDurationProvider.class));
				bind(IPortSlotProvider.class).toInstance(Mockito.mock(IPortSlotProvider.class));
			}

		});

		injector.injectMembers(contract);
		return contract;
	}
}
