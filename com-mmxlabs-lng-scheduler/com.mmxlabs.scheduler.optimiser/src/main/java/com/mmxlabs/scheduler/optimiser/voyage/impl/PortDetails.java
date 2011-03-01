/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
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

	public PortDetails() {

	}

	private PortDetails(final int visitDuration, final IPortSlot portSlot,
			final EnumMap<FuelComponent, Long> fuelConsumption) {
		this.visitDuration = visitDuration;
		this.portSlot = portSlot;
		this.fuelConsumption.putAll(fuelConsumption);
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

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortDetails) {
			final PortDetails d = (PortDetails) obj;

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
				+ "]";
	}

	public PortDetails clone() {
		return new PortDetails(visitDuration, portSlot, fuelConsumption);
	}
}
