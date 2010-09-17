package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.common.Equality;
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

	private int startTime;

	@Override
	public final long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	@Override
	public final void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	@Override
	public final int getVisitDuration() {
		return visitDuration;
	}

	@Override
	public final void setVisitDuration(final int visitDuration) {
		this.visitDuration = visitDuration;
	}

	@Override
	public final long getPortCost(final Object key) {
		throw new UnsupportedOperationException("Undefined API");
	}

	@Override
	public final IPortSlot getPortSlot() {
		return portSlot;
	}

	@Override
	public final void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	@Override
	public final int getStartTime() {
		return startTime;
	}

	@Override
	public final void setStartTime(final int startTime) {
		this.startTime = startTime;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortDetails) {
			final PortDetails d = (PortDetails) obj;
			if (startTime != d.startTime) {
				return false;
			}
			if (visitDuration != d.visitDuration) {
				return false;
			}

			if (!Equality.isEqual(fuelConsumption, d.fuelConsumption)) {
				return false;
			}
			if (!Equality.isEqual(portSlot, d.portSlot)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
