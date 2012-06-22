/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
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

/**
 * @author Tom Hinton
 * 
 */
public class TestNetbackContract {
	protected static final int MAX_SPEED = 10000;

	@Test
	public void testComputeNetbackPrice() {
		@SuppressWarnings("unchecked")
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = mock(IMultiMatrixProvider.class);
		@SuppressWarnings("unchecked")
		final IMatrixProvider<IPort, Integer> matrixProvider = mock(IMatrixProvider.class);
		
		final IPort A = mock(IPort.class, "Port A");
		final IPort B = mock(IPort.class, "Port B");

		final ILoadSlot slotA = mock(ILoadSlot.class, "Slot A");
		final IDischargeSlot slotB = mock(IDischargeSlot.class, "Slot B");

		final VoyageDetails ladenLeg = new VoyageDetails();

		final VoyageDetails ballastLeg = new VoyageDetails();

		final VoyageOptions ladenOptions = new VoyageOptions();
		final VoyageOptions ballastOptions = new VoyageOptions();

		ladenOptions.setFromPortSlot(slotA);
		ladenOptions.setToPortSlot(slotB);
		ballastOptions.setFromPortSlot(slotB);

		ladenLeg.setOptions(ladenOptions);
		ballastLeg.setOptions(ballastOptions);

		final IVessel vessel = mock(IVessel.class);
		final IVesselClass vesselClass = mock(IVesselClass.class);

		final IConsumptionRateCalculator curve = mock(IConsumptionRateCalculator.class);

		when(slotA.getCargoCVValue()).thenReturn(24000);

		// 1000 nautical miles
		when(matrixProvider.get(B, A)).thenReturn(1000);
		
		when(distanceProvider.get("Default")).thenReturn(matrixProvider);

		// 10 knots => 100 hours
		when(vesselClass.getMaxSpeed()).thenReturn(MAX_SPEED);
		when(vesselClass.getBaseFuelUnitPrice()).thenReturn(1000);
		when(vesselClass.getBaseFuelConversionFactor()).thenReturn(500);
		when(vesselClass.getConsumptionRate(VesselState.Ballast)).thenReturn(curve);

		when(vessel.getVesselClass()).thenReturn(vesselClass);
		when(vessel.getVesselInstanceType()).thenReturn(VesselInstanceType.SPOT_CHARTER);

		when(vesselClass.getHourlyCharterInPrice()).thenReturn(1000);

		when(slotA.getPort()).thenReturn(A);
		when(slotB.getPort()).thenReturn(B);

		when(curve.getRate(MAX_SPEED)).thenReturn(500l);

		
		
		Map<IVesselClass, BallastParameters> ballastParams = new HashMap<IVesselClass, BallastParameters>();
		BallastParameters params = new BallastParameters(vesselClass, MAX_SPEED, 1000, 1000, 500, new String[] { "Default" });
		
		ballastParams.put(vesselClass, params);
		
		
		final NetbackContract nbc = new NetbackContract(1234, distanceProvider, ballastParams);

		final VoyagePlan plan = new VoyagePlan();
		plan.setSequence(new Object[] { null, ladenLeg, null, ballastLeg, null });
		final int loadPrice = nbc.calculateLoadUnitPrice(slotA, slotB, 0, 0, 7500, 2000000, vessel, plan, null);

		Assert.assertEquals(6263, loadPrice);
	}
}
