package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;
import java.util.EnumMap;

import com.mmxlabs.common.Equality;
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

	private final EnumMap<FuelComponent, Long> fuelConsumptions;
	private final EnumMap<FuelComponent, Long> fuelCosts;

	public VoyagePlan() {
		fuelConsumptions = new EnumMap<FuelComponent, Long>(FuelComponent.class);
		fuelCosts = new EnumMap<FuelComponent, Long>(FuelComponent.class);
	}

	
	
	protected VoyagePlan(Object[] sequence, long dischargeVolume, long loadVolume,
			long salesRevenue, long purchaseCost,
			EnumMap<FuelComponent, Long> fuelConsumptions,
			EnumMap<FuelComponent, Long> fuelCosts) {
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
		if (fuelConsumptions.containsKey(fuel)) {
			return fuelConsumptions.get(fuel);
		}
		return 0;
	}

	public final void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumptions.put(fuel, consumption);
	}

	public final long getTotalFuelCost(final FuelComponent fuel) {
		if (fuelCosts.containsKey(fuel)) {
			return fuelCosts.get(fuel);
		}
		return 0;
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
		for (Object o : sequence) {
			if (o instanceof VoyageDetails) {
				clonedSequence[k++] = ((VoyageDetails) o).clone();
			} else if (o instanceof PortDetails) {
				clonedSequence[k++] = ((PortDetails) o).clone();
			} else {
				clonedSequence[k++] = o;
			}
		}
		return new VoyagePlan(clonedSequence, dischargeVolume, loadVolume, salesRevenue, 
				purchaseCost, fuelConsumptions, fuelCosts);
	}

}
