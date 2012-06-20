/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.trading.optimiser.contracts.impl.NetbackContract;

/**
 * @author Tom Hinton
 * 
 */
public class TestNetbackContract {
	protected static final int MAX_SPEED = 10000;

	@Test
	public void testComputeNetbackPrice() {
		final Mockery context = new Mockery();
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = context.mock(IMultiMatrixProvider.class);
		final IPort A = context.mock(IPort.class, "Port A");
		final IPort B = context.mock(IPort.class, "Port B");

		final ILoadSlot slotA = context.mock(ILoadSlot.class, "Slot A");
		final IDischargeSlot slotB = context.mock(IDischargeSlot.class, "Slot B");

		final VoyageDetails ladenLeg = new VoyageDetails();

		final VoyageDetails ballastLeg = new VoyageDetails();

		final VoyageOptions ladenOptions = new VoyageOptions();
		final VoyageOptions ballastOptions = new VoyageOptions();

		ladenOptions.setFromPortSlot(slotA);
		ladenOptions.setToPortSlot(slotB);
		ballastOptions.setFromPortSlot(slotB);

		ladenLeg.setOptions(ladenOptions);
		ballastLeg.setOptions(ballastOptions);

		final IVessel vessel = context.mock(IVessel.class);
		final IVesselClass vesselClass = context.mock(IVesselClass.class);

		final IConsumptionRateCalculator curve = context.mock(IConsumptionRateCalculator.class);

		context.checking(new Expectations() {
			{

				atLeast(1).of(slotA).getCargoCVValue();
				will(returnValue(24000));

				atLeast(1).of(distanceProvider).getMaximumValue(B, A);
				will(returnValue(1000)); // 1000 nautical miles
				atLeast(1).of(vesselClass).getMaxSpeed();
				will(returnValue(MAX_SPEED)); // 10 knots => 100 hours
				atLeast(1).of(vessel).getVesselClass();
				will(returnValue(vesselClass));
				atLeast(1).of(vessel).getVesselInstanceType();
				will(returnValue(VesselInstanceType.SPOT_CHARTER));
				atLeast(1).of(vesselClass).getHourlyCharterInPrice();
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

		final VoyagePlan plan = new VoyagePlan();
		plan.setSequence(new Object[] { null, ladenLeg, null, ballastLeg, null });
		final int loadPrice = nbc.calculateLoadUnitPrice(slotA, slotB, 0, 0, 7500, 2000000, vessel, plan);

		context.assertIsSatisfied();
		Assert.assertEquals(6263, loadPrice);
	}
}
