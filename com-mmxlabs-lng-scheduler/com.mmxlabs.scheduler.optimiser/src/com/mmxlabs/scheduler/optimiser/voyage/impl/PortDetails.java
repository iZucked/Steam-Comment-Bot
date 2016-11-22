/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements IDetailsSequenceElement, Cloneable {

	private @NonNull PortOptions options;

	private final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption = new LongFastEnumEnumMap<FuelComponent, FuelUnit>(FuelComponent.values().length, FuelUnit.values().length);
	private final EnumMap<FuelComponent, Integer> fuelPrice = new EnumMap<FuelComponent, Integer>(FuelComponent.class);

	private long portCosts;

	public PortDetails(final @NonNull PortOptions options) {
		this.options = options;
	}

	private PortDetails(final @NonNull PortOptions options,final LongFastEnumEnumMap<FuelComponent, FuelUnit> fuelConsumption2, final EnumMap<FuelComponent, Integer> fuelPrice, final long portCosts) {
		this.options = options;
		this.portCosts = portCosts;
		putAll(this.fuelConsumption, fuelConsumption2);

		this.fuelPrice.putAll(fuelPrice);
	}

	// TODO: Add to LongFastEnumEnumMap
		private final void putAll(final LongFastEnumEnumMap<FuelComponent, FuelUnit> dst, final LongFastEnumEnumMap<FuelComponent, FuelUnit> src) {

			for (final FuelComponent fc : FuelComponent.values()) {
				for (final FuelUnit fu : FuelUnit.values()) {
					dst.put(fc, fu, src.get(fc, fu));
				}
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

	public final void setFuelConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit, final long consumption) {
		fuelConsumption.put(fuel, fuelUnit, consumption);
	}
	
	public final long getFuelConsumption(final @NonNull FuelComponent fuel, final @NonNull FuelUnit fuelUnit) {

		return fuelConsumption.get(fuel, fuelUnit);
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
