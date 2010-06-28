package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;

/**
 * Implementation of {@link IPortDetails}
 * 
 * @author Simon Goodall
 * 
 */
public final class PortDetails implements IPortDetails {

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private int visitDuration;

	private IPortSlot portSlot;

	@Override
	public long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	@Override
	public void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	@Override
	public int getVisitDuration() {
		return visitDuration;
	}

	@Override
	public void setVisitDuration(final int visitDuration) {
		this.visitDuration = visitDuration;
	}

	@Override
	public long getPortCost(final Object key) {
		throw new UnsupportedOperationException("Undefined API");
	}

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}

	@Override
	public void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}
}
