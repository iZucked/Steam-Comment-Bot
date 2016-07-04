/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements IDetailsSequenceElement, Cloneable {

	private @NonNull PortOptions options;

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(FuelComponent.class);
	private final EnumMap<FuelComponent, Integer> fuelPrice = new EnumMap<FuelComponent, Integer>(FuelComponent.class);

	private long portCosts;

	public PortDetails(final @NonNull PortOptions options) {
		this.options = options;
	}

	private PortDetails(final @NonNull PortOptions options, final EnumMap<FuelComponent, Long> fuelConsumption, final EnumMap<FuelComponent, Integer> fuelPrice, final long portCosts) {
		this.options = options;
		this.portCosts = portCosts;
		this.fuelConsumption.putAll(fuelConsumption);
		this.fuelPrice.putAll(fuelPrice);
	}

	public final long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0L;
		}
	}

	/**
	 */
	public final int getFuelUnitPrice(final FuelComponent fuel) {
		if (!fuelPrice.containsKey(fuel)) {
			return 0;
		}
		return fuelPrice.get(fuel);
	}

	public final void setFuelConsumption(final FuelComponent fuel, final long consumption) {
		fuelConsumption.put(fuel, consumption);
	}

	/**
	 */
	public final void setFuelUnitPrice(final FuelComponent fuel, final int price) {
		fuelPrice.put(fuel, price);
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
			return Objects.equal(fuelConsumption, d.fuelConsumption)
				&& Objects.equal(fuelPrice, d.fuelPrice)
				&& Objects.equal(portCosts, d.portCosts)
				&& Objects.equal(options, d.options)
						;
			// @formatter:on
		}

		return false;
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(options, fuelConsumption, fuelPrice, portCosts);
	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(PortDetails.class)
				.add("options", options)
				.add("fuelConsumption", fuelConsumption)
				.add("fuelPrice", fuelPrice)
				.add("portCosts", portCosts)
				.toString();
		// @formatter:on
	}

	@Override
	public PortDetails clone() {
		return new PortDetails(new PortOptions(options), fuelConsumption, fuelPrice, portCosts);
	}

	public long getPortCosts() {
		return portCosts;
	}

	public void setPortCosts(final long portCosts) {
		this.portCosts = portCosts;
	}
}
