/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author Tom Hinton
 * 
 */
public class TestFixedPriceContract {

	@Test
	public void testComputeFixedPrice() {

		final ILoadSlot loadOption = Mockito.mock(ILoadSlot.class);
		final IDischargeSlot dischargeOption = Mockito.mock(IDischargeSlot.class);
		final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final VoyagePlan voyagePlan = new VoyagePlan();

		final FixedPriceContract contract = new FixedPriceContract(35353);

		Assertions.assertEquals(35353, contract.calculateFOBPricePerMMBTu(loadOption, dischargeOption, 0, allocationAnnotation, vesselAvailability, voyagePlan, null, null));
	}
}
