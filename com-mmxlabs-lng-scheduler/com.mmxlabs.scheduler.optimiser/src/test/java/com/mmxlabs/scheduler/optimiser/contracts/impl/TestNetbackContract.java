/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * @author Tom Hinton
 * 
 */
public class TestNetbackContract {
	protected static final int MAX_SPEED = 10000;

	@Test
	public void testComputeNetbackPrice() {
		final Mockery context = new Mockery();
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = context
				.mock(IMultiMatrixProvider.class);
		final IPort A = context.mock(IPort.class, "Port A");
		final IPort B = context.mock(IPort.class, "Port B");

		final IPortSlot slotA = context.mock(IPortSlot.class, "Slot A");
		final IPortSlot slotB = context.mock(IPortSlot.class, "Slot B");
		
		final VoyageDetails ladenLeg = new VoyageDetails();

		final VoyageDetails ballastLeg = new VoyageDetails();

		final VoyageOptions ladenOptions = new VoyageOptions();
		final VoyageOptions ballastOptions = new VoyageOptions();
		
		ladenOptions.setFromPortSlot(slotA);
		ladenOptions.setToPortSlot(slotB);
		ballastOptions.setFromPortSlot(slotB);
		
		ladenLeg.setOptions(ladenOptions);
		ballastLeg.setOptions(ballastOptions);
		
		final IVesselClass vesselClass = context.mock(IVesselClass.class);
		
		final IConsumptionRateCalculator curve = context.mock(IConsumptionRateCalculator.class);
		
		context.checking(new Expectations() {
			{
				atLeast(1).of(distanceProvider).getMaximumValue(B, A);
				will(returnValue(1000)); // 1000 nautical miles
				atLeast(1).of(vesselClass).getMaxSpeed();
				will(returnValue(MAX_SPEED)); // 10 knots => 100 hours
				atLeast(1).of(vesselClass).getHourlyCharterPrice();
				will(returnValue(1000));
				
				atLeast(1).of(slotA).getPort();
				will(returnValue(A));
				atLeast(1).of(slotB).getPort();
				will(returnValue(B));
				
				atLeast(1).of(vesselClass).getBaseFuelUnitPrice();
				will(returnValue(1000));
				
				atLeast(1).of(vesselClass).getBaseFuelConversionFactor();
				will(returnValue(500));
				
				atLeast(1).of(vesselClass).getConsumptionRate(VesselState.Ballast);
				will(returnValue(curve));
				
				atLeast(1).of(curve).getRate(MAX_SPEED);
				will(returnValue(500l));
			}
		});

		final NetbackContract nbc = new NetbackContract(1234, distanceProvider);

		final int loadPrice = nbc.calculateLoadUnitPrice(0, 2000000, 0, 7500,
				24000, ladenLeg, ballastLeg, vesselClass);
		System.err.println(loadPrice);
		context.assertIsSatisfied();
		Assert.assertEquals(6263, loadPrice);
	}
}
