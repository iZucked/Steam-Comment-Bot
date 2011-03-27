/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Implementation of {@link VoyagePlan}.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyagePlan implements Cloneable {

	private Object[] sequence;
	private long dischargeVolume;
	private long loadVolume;
	private long salesRevenue;
	private long purchaseCost;

	private final LongFastEnumMap<FuelComponent> fuelConsumptions;
	private final LongFastEnumMap<FuelComponent> fuelCosts;
	private long lngFuelVolume;

	public VoyagePlan() {
		fuelConsumptions = new LongFastEnumMap<FuelComponent>(
				FuelComponent.values().length);
		fuelCosts = new LongFastEnumMap<FuelComponent>(
				FuelComponent.values().length);
	}

	protected VoyagePlan(final Object[] sequence, final long dischargeVolume,
			final long loadVolume, final long salesRevenue,
			final long purchaseCost,
			final LongFastEnumMap<FuelComponent> fuelConsumptions,
			final LongFastEnumMap<FuelComponent> fuelCosts) {
		super();
		this.sequence = sequence;
		this.dischargeVolume = dischargeVolume;
		this.loadVolume = loadVolume;
		this.salesRevenue = salesRevenue;
		this.purchaseCost = purchaseCost;
		this.fuelConsumptions = fuelConsumptions;
		this.fuelCosts = fuelCosts;
	}

	public final long getFuelConsumption(final FuelComponent fuel) {
		return fuelConsumptions.get(fuel);
	}

	public final void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumptions.put(fuel, consumption);
	}

	public final long getTotalFuelCost(final FuelComponent fuel) {
		return fuelCosts.get(fuel);
	}

	public final void setTotalFuelCost(final FuelComponent fuel, final long cost) {
		fuelCosts.put(fuel, cost);
	}

	public final long getDischargeVolume() {
		return dischargeVolume;
	}

	public final void setDischargeVolume(final long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}

	public final long getLoadVolume() {
		return loadVolume;
	}

	public final void setLoadVolume(final long loadVolume) {
		this.loadVolume = loadVolume;
	}

	public final long getSalesRevenue() {
		return salesRevenue;
	}

	public final void setSalesRevenue(final long salesRevenue) {
		this.salesRevenue = salesRevenue;
	}

	public final long getPurchaseCost() {
		return purchaseCost;
	}

	public final void setPurchaseCost(final long purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public final Object[] getSequence() {
		return sequence;
	}

	public final void setSequence(final Object[] sequence) {
		this.sequence = sequence;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyagePlan) {
			final VoyagePlan p = (VoyagePlan) obj;

			if (dischargeVolume != p.dischargeVolume) {
				return false;
			}
			if (loadVolume != p.loadVolume) {
				return false;
			}
			if (purchaseCost != p.purchaseCost) {
				return false;
			}
			if (salesRevenue != p.salesRevenue) {
				return false;
			}

			if (!Equality.isEqual(sequence, p.sequence)) {
				return false;
			}
			if (!Equality.isEqual(fuelConsumptions, p.fuelConsumptions)) {
				return false;
			}
			if (!Equality.isEqual(fuelCosts, p.fuelCosts)) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "VoyagePlan [sequence=" + Arrays.toString(sequence)
				+ ", dischargeVolume=" + dischargeVolume + ", loadVolume="
				+ loadVolume + ", salesRevenue=" + salesRevenue
				+ ", purchaseCost=" + purchaseCost + ", fuelConsumptions="
				+ fuelConsumptions + ", fuelCosts=" + fuelCosts + "]";
	}

	@Override
	public final VoyagePlan clone() {
		final Object[] clonedSequence = new Object[sequence.length];
		int k = 0;
		for (final Object o : sequence) {
			if (o instanceof VoyageDetails) {
				clonedSequence[k++] = ((VoyageDetails<?>) o).clone();
			} else if (o instanceof PortDetails) {
				clonedSequence[k++] = ((PortDetails) o).clone();
			} else {
				clonedSequence[k++] = o;
			}
		}
		return new VoyagePlan(clonedSequence, dischargeVolume, loadVolume,
				salesRevenue, purchaseCost, fuelConsumptions, fuelCosts);
	}

	/**
	 * Set the total quantity of LNG used for fuel in this voyageplan
	 * 
	 * @param lngConsumed
	 */
	public void setLNGFuelVolume(final long lngConsumed) {
		this.lngFuelVolume = lngConsumed;
	}

	/**
	 * @return the total quantity of LNG used for fuel in this plan.
	 */
	public final long getLNGFuelVolume() {
		return lngFuelVolume;
	}

	private int totalRouteCost;

	public void setTotalRouteCost(final int routeCost) {
		totalRouteCost = routeCost;
	}

	public int getTotalRouteCost() {
		return totalRouteCost;
	}
}
