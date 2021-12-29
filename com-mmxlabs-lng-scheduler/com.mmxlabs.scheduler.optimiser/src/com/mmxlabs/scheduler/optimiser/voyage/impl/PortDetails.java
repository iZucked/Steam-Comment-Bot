/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumEnumMap;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements IDetailsSequenceElement {

	private @NonNull PortOptions options;

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);
	private final LongFastEnumMap<FuelComponent> fuelUnitPrices = new LongFastEnumMap<FuelComponent>(FuelComponent.values().length);
	private long portCosts;
	private long charterCost;
	
	public PortDetails(final @NonNull PortOptions options) {
		this.options = options;
	}

	private PortDetails(final @NonNull PortOptions options, final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final LongFastEnumMap<FuelComponent> fuelPrice, final long portCosts) {
		this.options = options;
		this.portCosts = portCosts;
		this.fuelConsumption.putAll(fuelConsumption2);
		this.fuelUnitPrices.putAll(fuelPrice);
	}


	public final long getFuelConsumption(final FuelKey fuelKey) {
		return fuelConsumption.get(fuelKey.getFuelComponent(), fuelKey.getFuelUnit());
	}

	public void setFuelConsumption(@NonNull FuelKey fk, long consumption) {
		fuelConsumption.put(fk.getFuelComponent(), fk.getFuelUnit(), consumption);
	}

	/**
	 */
	public final int getFuelUnitPrice(final @NonNull FuelComponent baseFuel) {
		return (int) fuelUnitPrices.get(baseFuel);
	}

	public final void setFuelUnitPrice(final @NonNull FuelComponent baseFuel, final int unitPrice) {
		fuelUnitPrices.put(baseFuel, unitPrice);
	}

	/**
	 */
	public @NonNull PortOptions getOptions() {
		return options;
	}

	public void setOptions(final @NonNull PortOptions options) {
		this.options = options;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortDetails) {
			final PortDetails d = (PortDetails) obj;

			// @formatter:off
			return portCosts ==  d.portCosts
				&&  Objects.equal(fuelUnitPrices, d.fuelUnitPrices)
				&& Objects.equal(fuelConsumption, d.fuelConsumption)
				&& Objects.equal(options, d.options)
						;
			// @formatter:on
		}

		return false;
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(options, fuelConsumption, fuelUnitPrices, portCosts);
	}

	@Override
	public String toString() {
		// @formatter:off
		return MoreObjects.toStringHelper(PortDetails.class)
				.add("options", options)
				.add("fuelConsumption", fuelConsumption)
				.add("fuelPrice", fuelUnitPrices)
				.add("portCosts", portCosts)
				.toString();
		// @formatter:on
	}

	public PortDetails copy() {
		return new PortDetails(new PortOptions(options), fuelConsumption, fuelUnitPrices, portCosts);
	}

	public long getPortCosts() {
		return portCosts;
	}

	public void setPortCosts(final long portCosts) {
		this.portCosts = portCosts;
	}

	public long getCharterCost() {
		return charterCost;
	}

	public void setCharterCost(long charterCost) {
		this.charterCost = charterCost;
	}
}
