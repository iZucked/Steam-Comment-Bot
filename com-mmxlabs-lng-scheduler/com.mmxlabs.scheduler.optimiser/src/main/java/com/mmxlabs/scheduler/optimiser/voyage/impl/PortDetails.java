package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements Cloneable {

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(
			FuelComponent.class);

	private int visitDuration;

	private IPortSlot portSlot;

	private int startTime;

	public PortDetails() {

	}

	private PortDetails(final int visitDuration2, final IPortSlot portSlot2,
			final int startTime2,
			final EnumMap<FuelComponent, Long> fuelConsumption2) {
		this.visitDuration = visitDuration2;
		this.portSlot = portSlot2;
		this.startTime = startTime2;
		this.fuelConsumption.putAll(fuelConsumption2);
	}

	public final long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	public final void setFuelConsumption(final FuelComponent fuel,
			final long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	public final int getVisitDuration() {
		return visitDuration;
	}

	public final void setVisitDuration(final int visitDuration) {
		this.visitDuration = visitDuration;
	}

	public final long getPortCost(final Object key) {
		throw new UnsupportedOperationException("Undefined API");
	}

	public final IPortSlot getPortSlot() {
		return portSlot;
	}

	public final void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	public final int getStartTime() {
		return startTime;
	}

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

	@Override
	public String toString() {
		return "PortDetails [fuelConsumption=" + fuelConsumption
				+ ", visitDuration=" + visitDuration + ", portSlot=" + portSlot
				+ ", startTime=" + startTime + "]";
	}

	public PortDetails clone() {
		return new PortDetails(visitDuration, portSlot, startTime,
				fuelConsumption);
	}
}
