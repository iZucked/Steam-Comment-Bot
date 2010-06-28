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
	public void testGetSetFuelCost() {
		final FuelComponent c = FuelComponent.Base;
		final long value = 100l;
		final VoyagePlan plan = new VoyagePlan();
		Assert.assertEquals(0, plan.getFuelCost(c));
		plan.setFuelCost(c, value);
		Assert.assertEquals(value, plan.getFuelCost(c));
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
}
