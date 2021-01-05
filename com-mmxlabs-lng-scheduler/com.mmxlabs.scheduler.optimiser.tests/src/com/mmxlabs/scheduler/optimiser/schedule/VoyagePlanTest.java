/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VoyagePlanTest {

	@Test
	public void testGetSetTotalFuelCost() {
		final FuelComponent c = FuelComponent.Base;
		final long value = 100L;
		final VoyagePlan plan = new VoyagePlan();
		Assertions.assertEquals(0, plan.getBaseFuelCost());
		plan.setBaseFuelCost(value);
		Assertions.assertEquals(value, plan.getBaseFuelCost());
	}

	@Test
	public void testGetSetSequence() {
		final IDetailsSequenceElement[] value = new IDetailsSequenceElement[0];
		final VoyagePlan plan = new VoyagePlan();
		Assertions.assertNull(plan.getSequence());
		plan.setSequence(value);
		Assertions.assertSame(value, plan.getSequence());
	}

	@Test
	public void testEquals() {
		final IDetailsSequenceElement[] seq1 = new IDetailsSequenceElement[0];

		final VoyagePlan plan1 = make(seq1, 6, 8, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan2 = make(seq1, 6, 8, 9, 10, 11, 12, 13, 100, false);

		final VoyagePlan plan3 = make(seq1, 26, 8, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan5 = make(seq1, 6, 28, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan6 = make(seq1, 6, 8, 29, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan7 = make(seq1, 6, 8, 9, 210, 11, 12, 13, 100, false);
		final VoyagePlan plan8 = make(seq1, 6, 8, 9, 10, 211, 12, 13, 100, false);
		final VoyagePlan plan9 = make(seq1, 6, 8, 9, 10, 11, 212, 13, 100, false);
		final VoyagePlan plan10 = make(seq1, 6, 8, 9, 10, 11, 12, 213, 100, false);
		final VoyagePlan plan11 = make(seq1, 6, 8, 9, 10, 11, 12, 13, 2100, false);
		final VoyagePlan plan12 = make(seq1, 6, 8, 9, 10, 11, 12, 13, 100, true);

		Assertions.assertTrue(plan1.equals(plan1));
		Assertions.assertTrue(plan1.equals(plan2));
		Assertions.assertTrue(plan2.equals(plan1));

		Assertions.assertFalse(plan1.equals(plan3));
		Assertions.assertFalse(plan1.equals(plan5));
		Assertions.assertFalse(plan1.equals(plan6));
		Assertions.assertFalse(plan1.equals(plan7));
		Assertions.assertFalse(plan1.equals(plan8));
		Assertions.assertFalse(plan1.equals(plan9));
		Assertions.assertFalse(plan1.equals(plan10));
		Assertions.assertFalse(plan1.equals(plan11));
		Assertions.assertFalse(plan1.equals(plan12));

		Assertions.assertFalse(plan3.equals(plan1));
		Assertions.assertFalse(plan5.equals(plan1));
		Assertions.assertFalse(plan6.equals(plan1));
		Assertions.assertFalse(plan7.equals(plan1));
		Assertions.assertFalse(plan8.equals(plan1));
		Assertions.assertFalse(plan9.equals(plan1));
		Assertions.assertFalse(plan10.equals(plan1));
		Assertions.assertFalse(plan10.equals(plan1));
		Assertions.assertFalse(plan11.equals(plan1));
		Assertions.assertFalse(plan12.equals(plan1));

		Assertions.assertFalse(plan1.equals(new Object()));
	}

	VoyagePlan make(final IDetailsSequenceElement[] sequence, final long baseFuelCost, final long cooldownCost, final long lngFuelCost, final long lngFuelVolume,
			final long remainingHeel, final long startingHeel, final long totalRouteCost, final int cvtQty, boolean ignoreEnd) {

		final VoyagePlan p = new VoyagePlan();

		p.setSequence(sequence);
		p.setBaseFuelCost(baseFuelCost);
		p.setCooldownCost(cooldownCost);
		p.setLngFuelCost(lngFuelCost);
		p.setLNGFuelVolume(lngFuelVolume);
		p.setRemainingHeelInM3(remainingHeel);
		p.setStartingHeelInM3(startingHeel);
		p.setTotalRouteCost(totalRouteCost);
		p.setViolationsCount(cvtQty);
		p.setIgnoreEnd(ignoreEnd);

		return p;
	}
}
