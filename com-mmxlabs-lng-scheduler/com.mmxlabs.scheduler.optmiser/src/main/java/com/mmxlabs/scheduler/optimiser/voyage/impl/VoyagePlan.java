package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * Implementation of {@link IVoyagePlan}.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyagePlan implements IVoyagePlan {

	private Object[] sequence;
	private long dischargeVolume;
	private long loadVolume;
	private long salesRevenue;
	private long purchaseCost;

	private final EnumMap<FuelComponent, Long> fuelConsumptions = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);
	private final EnumMap<FuelComponent, Long> fuelCosts = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	@Override
	public long getFuelConsumption(final FuelComponent fuel) {
		if (fuelConsumptions.containsKey(fuel)) {
			return fuelConsumptions.get(fuel);
		}
		return 0;
	}

	@Override
	public void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumptions.put(fuel, consumption);
	}

	@Override
	public long getFuelCost(final FuelComponent fuel) {
		if (fuelCosts.containsKey(fuel)) {
			return fuelCosts.get(fuel);
		}
		return 0;
	}

	@Override
	public void setFuelCost(final FuelComponent fuel, final long cost) {
		fuelCosts.put(fuel, cost);
	}

	@Override
	public long getDischargeVolume() {
		return dischargeVolume;
	}

	@Override
	public void setDischargeVolume(final long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}

	@Override
	public long getLoadVolume() {
		return loadVolume;
	}

	@Override
	public void setLoadVolume(final long loadVolume) {
		this.loadVolume = loadVolume;
	}

	@Override
	public long getSalesRevenue() {
		return salesRevenue;
	}

	@Override
	public void setSalesRevenue(final long salesRevenue) {
		this.salesRevenue = salesRevenue;
	}

	@Override
	public long getPurchaseCost() {
		return purchaseCost;
	}

	@Override
	public void setPurchaseCost(final long purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	@Override
	public Object[] getSequence() {
		return sequence;
	}

	@Override
	public void setSequence(final Object[] sequence) {
		this.sequence = sequence;
	}
}
