/**
 * Copyright (C) Minimaxlabs, 2010
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
	public void testGetSetDischargeVolume() {
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getDischargeVolume());
		plan.setDischargeVolume(value);
		Assert.assertEquals(value, plan.getDischargeVolume());
	}

	@Test
	public void testGetSetLoadVolume() {
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getLoadVolume());
		plan.setLoadVolume(value);
		Assert.assertEquals(value, plan.getLoadVolume());
	}

	@Test
	public void testGetSetSalesRevenue() {
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getSalesRevenue());
		plan.setSalesRevenue(value);
		Assert.assertEquals(value, plan.getSalesRevenue());
	}

	@Test
	public void testGetSetPurchaseCost() {
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getPurchaseCost());
		plan.setPurchaseCost(value);
		Assert.assertEquals(value, plan.getPurchaseCost());
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
		Object[] seq1 = new Object[0];
		Object[] seq2 = new Object[0];

		FuelComponent fuel1 = FuelComponent.Base;
		FuelComponent fuel2 = FuelComponent.Base_Supplemental;
		FuelComponent fuel3 = FuelComponent.NBO;
		FuelComponent fuel4 = FuelComponent.FBO;

		VoyagePlan plan1 = make(seq1, 1, 2, 3, 4, fuel1, 5, fuel2, 6);
		VoyagePlan plan2 = make(seq1, 1, 2, 3, 4, fuel1, 5, fuel2, 6);

		VoyagePlan plan3 = make(seq2, 1, 2, 3, 4, fuel1, 5, fuel2, 6);
		VoyagePlan plan4 = make(seq1, 21, 2, 3, 4, fuel1, 5, fuel2, 6);
		VoyagePlan plan5 = make(seq1, 1, 22, 3, 4, fuel1, 5, fuel2, 6);
		VoyagePlan plan6 = make(seq1, 1, 2, 23, 4, fuel1, 5, fuel2, 6);
		VoyagePlan plan7 = make(seq1, 1, 2, 3, 24, fuel1, 5, fuel2, 6);
		VoyagePlan plan8 = make(seq1, 1, 2, 3, 4, fuel3, 5, fuel2, 6);
		VoyagePlan plan9 = make(seq1, 1, 2, 3, 4, fuel1, 25, fuel2, 6);
		VoyagePlan plan10 = make(seq1, 1, 2, 3, 4, fuel1, 5, fuel4, 6);
		VoyagePlan plan11 = make(seq1, 1, 2, 3, 4, fuel1, 5, fuel2, 26);

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

		Assert.assertFalse(plan3.equals(plan1));
		Assert.assertFalse(plan4.equals(plan1));
		Assert.assertFalse(plan5.equals(plan1));
		Assert.assertFalse(plan6.equals(plan1));
		Assert.assertFalse(plan7.equals(plan1));
		Assert.assertFalse(plan8.equals(plan1));
		Assert.assertFalse(plan9.equals(plan1));
		Assert.assertFalse(plan10.equals(plan1));
		Assert.assertFalse(plan11.equals(plan1));

		Assert.assertFalse(plan1.equals(new Object()));
	}

	VoyagePlan make(Object[] sequence, long dischargeVolume, long loadVolume,
			long salesRevenue, long purchaseCost, FuelComponent fuel1,
			long consumption, FuelComponent fuel2, long cost) {

		VoyagePlan p = new VoyagePlan();

		p.setSequence(sequence);
		p.setDischargeVolume(dischargeVolume);
		p.setLoadVolume(loadVolume);
		p.setSalesRevenue(salesRevenue);
		p.setPurchaseCost(purchaseCost);
		p.setFuelConsumption(fuel1, consumption);
		p.setTotalFuelCost(fuel2, cost);

		return p;
	}
}
