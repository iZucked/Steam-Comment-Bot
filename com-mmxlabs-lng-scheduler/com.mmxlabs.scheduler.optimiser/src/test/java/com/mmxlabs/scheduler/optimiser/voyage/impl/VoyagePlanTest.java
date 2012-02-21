/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public class VoyagePlanTest {

	@Test
	public void testGetSetFuelConsumption() {
		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getFuelConsumption(c));
		plan.setFuelConsumption(c, value);
		Assert.assertEquals(value, plan.getFuelConsumption(c));
	}

	@Test
	public void testGetSetTotalFuelCost() {
		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getTotalFuelCost(c));
		plan.setTotalFuelCost(c, value);
		Assert.assertEquals(value, plan.getTotalFuelCost(c));
	}

	@Test
	public void testGetSetCapacityViolationType() {
		final CapacityViolationType cvt = CapacityViolationType.FORCED_COOLDOWN;
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getCapacityViolation(cvt));
		plan.setCapacityViolation(cvt, value);
		Assert.assertEquals(value, plan.getCapacityViolation(cvt));
	}
	
	@Test
	public void testGetSetSequence() {
		final Object[] value = new Object[0];
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertNull(plan.getSequence());
		plan.setSequence(value);
		Assert.assertSame(value, plan.getSequence());
	}

	@Test
	public void testEquals() {
		final Object[] seq1 = new Object[0];

		final FuelComponent fuel1 = FuelComponent.Base;
		final FuelComponent fuel2 = FuelComponent.Base_Supplemental;
		final FuelComponent fuel3 = FuelComponent.NBO;
		final FuelComponent fuel4 = FuelComponent.FBO;
		
		final CapacityViolationType cvt1 = CapacityViolationType.FORCED_COOLDOWN;
		final CapacityViolationType cvt2 = CapacityViolationType.VESSEL_CAPACITY;

		final VoyagePlan plan1 = make(seq1, fuel1, 5, fuel2, 6, cvt1, 100);
		final VoyagePlan plan2 = make(seq1, fuel1, 5, fuel2, 6, cvt1, 100);

		final VoyagePlan plan3 = make(seq1, fuel3, 5, fuel2, 6, cvt1, 100);
		final VoyagePlan plan4 = make(seq1, fuel1, 25, fuel2, 6, cvt1, 100);
		final VoyagePlan plan5 = make(seq1, fuel1, 5, fuel4, 6, cvt1, 100);
		final VoyagePlan plan6 = make(seq1, fuel1, 5, fuel2, 26, cvt1, 100);
		final VoyagePlan plan7 = make(seq1, fuel1, 5, fuel2, 26, cvt2, 100);
		final VoyagePlan plan8 = make(seq1, fuel1, 5, fuel2, 26, cvt1, 200);

		Assert.assertTrue(plan1.equals(plan1));
		Assert.assertTrue(plan1.equals(plan2));
		Assert.assertTrue(plan2.equals(plan1));

		Assert.assertFalse(plan1.equals(plan3));
		Assert.assertFalse(plan1.equals(plan4));
		Assert.assertFalse(plan1.equals(plan5));
		Assert.assertFalse(plan1.equals(plan6));
		Assert.assertFalse(plan1.equals(plan7));
		Assert.assertFalse(plan1.equals(plan8));

		Assert.assertFalse(plan3.equals(plan1));
		Assert.assertFalse(plan4.equals(plan1));
		Assert.assertFalse(plan5.equals(plan1));
		Assert.assertFalse(plan6.equals(plan1));
		Assert.assertFalse(plan7.equals(plan1));
		Assert.assertFalse(plan8.equals(plan1));

		Assert.assertFalse(plan1.equals(new Object()));
	}

	VoyagePlan make(final Object[] sequence, final FuelComponent fuel1, final long consumption, final FuelComponent fuel2, final long cost, CapacityViolationType cvt, final long cvtQty) {

		final VoyagePlan p = new VoyagePlan();

		p.setSequence(sequence);
		p.setFuelConsumption(fuel1, consumption);
		p.setTotalFuelCost(fuel2, cost);
		p.setCapacityViolation(cvt, cvtQty);

		return p;
	}
}
