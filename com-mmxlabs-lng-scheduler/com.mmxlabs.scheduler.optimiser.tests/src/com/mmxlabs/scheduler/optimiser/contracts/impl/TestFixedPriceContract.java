/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author Tom Hinton
 * 
 */
public class TestFixedPriceContract {
	@SuppressWarnings("null")
	@Test
	public void testComputeFixedPrice() {

		final ILoadSlot loadOption = Mockito.mock(ILoadSlot.class);
		final IDischargeSlot dischargeOption = Mockito.mock(IDischargeSlot.class);
		final IAllocationAnnotation allocationAnnotation = Mockito.mock(IAllocationAnnotation.class);
		final IVesselAvailability vesselAvailability = Mockito.mock(IVesselAvailability.class);
		final VolumeAllocatedSequences volumeAllocatedSequences = Mockito.mock(VolumeAllocatedSequences.class);
		final VoyagePlan voyagePlan = new VoyagePlan();

		final FixedPriceContract contract = new FixedPriceContract(35353);
		assert (contract.calculateFOBPricePerMMBTu(loadOption, dischargeOption, 0, allocationAnnotation, vesselAvailability, 0, voyagePlan, volumeAllocatedSequences, null) == 35353);
	}
}
