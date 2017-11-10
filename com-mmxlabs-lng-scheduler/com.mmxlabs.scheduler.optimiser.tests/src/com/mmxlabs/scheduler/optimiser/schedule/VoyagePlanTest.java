/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VoyagePlanTest {

	@Test
	public void testGetSetTotalFuelCost() {
		final FuelComponent c = FuelComponent.Base;
		final long value = 100L;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getBaseFuelCost());
		plan.setBaseFuelCost(value);
		Assert.assertEquals(value, plan.getBaseFuelCost());
	}

	@Test
	public void testGetSetSequence() {
		final IDetailsSequenceElement[] value = new IDetailsSequenceElement[0];
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertNull(plan.getSequence());
		plan.setSequence(value);
		Assert.assertSame(value, plan.getSequence());
	}

	@Test
	public void testEquals() {
		final IDetailsSequenceElement[] seq1 = new IDetailsSequenceElement[0];

		final VoyagePlan plan1 = make(seq1, 6, 7, 8, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan2 = make(seq1, 6, 7, 8, 9, 10, 11, 12, 13, 100, false);

		final VoyagePlan plan3 = make(seq1, 26, 7, 8, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan4 = make(seq1, 6, 27, 8, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan5 = make(seq1, 6, 7, 28, 9, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan6 = make(seq1, 6, 7, 8, 29, 10, 11, 12, 13, 100, false);
		final VoyagePlan plan7 = make(seq1, 6, 7, 8, 9, 210, 11, 12, 13, 100, false);
		final VoyagePlan plan8 = make(seq1, 6, 7, 8, 9, 10, 211, 12, 13, 100, false);
		final VoyagePlan plan9 = make(seq1, 6, 7, 8, 9, 10, 11, 212, 13, 100, false);
		final VoyagePlan plan10 = make(seq1, 6, 7, 8, 9, 10, 11, 12, 213, 100, false);
		final VoyagePlan plan11 = make(seq1, 6, 7, 8, 9, 10, 11, 12, 13, 2100, false);
		final VoyagePlan plan12 = make(seq1, 6, 7, 8, 9, 10, 11, 12, 13, 100, true);

		Assert.assertTrue(plan1.equals(plan1));
		Assert.assertTrue(plan1.equals(plan2));
		Assert.assertTrue(plan2.equals(plan1));

		Assert.assertFalse(plan1.equals(plan3));
		Assert.assertFalse(plan1.equals(plan4));
		Assert.assertFalse(plan1.equals(plan5));
		Assert.assertFalse(plan1.equals(plan6));
		Assert.assertFalse(plan1.equals(plan7));
		Assert.assertFalse(plan1.equals(plan8));
		Assert.assertFalse(plan1.equals(plan9));
		Assert.assertFalse(plan1.equals(plan10));
		Assert.assertFalse(plan1.equals(plan11));
		Assert.assertFalse(plan1.equals(plan12));

		Assert.assertFalse(plan3.equals(plan1));
		Assert.assertFalse(plan4.equals(plan1));
		Assert.assertFalse(plan5.equals(plan1));
		Assert.assertFalse(plan6.equals(plan1));
		Assert.assertFalse(plan7.equals(plan1));
		Assert.assertFalse(plan8.equals(plan1));
		Assert.assertFalse(plan9.equals(plan1));
		Assert.assertFalse(plan10.equals(plan1));
		Assert.assertFalse(plan10.equals(plan1));
		Assert.assertFalse(plan11.equals(plan1));
		Assert.assertFalse(plan12.equals(plan1));

		Assert.assertFalse(plan1.equals(new Object()));
	}

	VoyagePlan make(final IDetailsSequenceElement[] sequence, final long baseFuelCost, final long charterInRate, final long cooldownCost, final long lngFuelCost, final long lngFuelVolume,
			final long remainingHeel, final long startingHeel, final long totalRouteCost, final int cvtQty, boolean ignoreEnd) {

		final VoyagePlan p = new VoyagePlan();

		p.setSequence(sequence);
		p.setBaseFuelCost(baseFuelCost);
		p.setCharterInRatePerDay(charterInRate);
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
