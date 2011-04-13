/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * @author Tom Hinton
 * 
 */
public class TestNetbackContract {
	@Test
	public void testComputeNetbackPrice() {
		final Mockery context = new Mockery();
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = context
				.mock(IMultiMatrixProvider.class);
		final IPort A = context.mock(IPort.class, "Port A");
		final IPort B = context.mock(IPort.class, "Port B");

		final VoyageDetails ladenLeg = new VoyageDetails();

		final VoyageDetails ballastLeg = new VoyageDetails();

		final IVesselClass vesselClass = context.mock(IVesselClass.class);

		context.checking(new Expectations() {
			{
				oneOf(distanceProvider).getMaximumValue(A, B);
				will(returnValue(1000)); // 1000 nautical miles
				oneOf(vesselClass).getMaxSpeed();
				will(returnValue(10000)); // 10 knots => 100 hours
				oneOf(vesselClass).getHourlyCharterPrice();
				will(returnValue(1000));
			}
		});

		final NetbackContract nbc = new NetbackContract(1234, distanceProvider);

		final int loadPrice = nbc.calculateLoadUnitPrice(0, 2000000, 0, 7500,
				24000, ladenLeg, ballastLeg, vesselClass);
		context.assertIsSatisfied();
	}
}
