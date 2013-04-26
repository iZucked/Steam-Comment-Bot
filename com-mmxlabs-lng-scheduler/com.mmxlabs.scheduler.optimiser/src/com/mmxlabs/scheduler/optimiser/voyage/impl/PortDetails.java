/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.google.common.base.Objects;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements IProfitAndLossDetails, Cloneable {

	private PortOptions options;

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(FuelComponent.class);
	private final EnumMap<FuelComponent, Integer> fuelPrice = new EnumMap<FuelComponent, Integer>(FuelComponent.class);

	private final LongFastEnumMap<CapacityViolationType> capacityViolations = new LongFastEnumMap<CapacityViolationType>(CapacityViolationType.values().length);

	private long totalGroupProfitAndLoss;

	public PortDetails() {

	}

	private PortDetails(final PortOptions options, final EnumMap<FuelComponent, Long> fuelConsumption, final LongFastEnumMap<CapacityViolationType> capacityViolations, long totalGroupProfitAndLoss) {
		this.options = options;
		this.fuelConsumption.putAll(fuelConsumption);
		this.capacityViolations.putAll(capacityViolations);
		this.totalGroupProfitAndLoss = totalGroupProfitAndLoss;
	}

	public final long getFuelConsumption(final FuelComponent fuel) {

		if (fuelConsumption.containsKey(fuel)) {
			return fuelConsumption.get(fuel);
		} else {
			return 0l;
		}
	}

	/**
	 * @since 2.0
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
	 * @since 2.0
	 */
	public final void setFuelUnitPrice(final FuelComponent fuel, final int price) {
		fuelPrice.put(fuel, price);
	}

	public final long getCapacityViolation(final CapacityViolationType type) {
		return capacityViolations.get(type);
	}

	public final void setCapacityViolation(final CapacityViolationType type, final long quantity) {
		capacityViolations.put(type, quantity);
	}

	public final long getPortCost(final Object key) {
		throw new UnsupportedOperationException("Undefined API");
	}

	/**
	 * @since 2.0
	 */
	public PortOptions getOptions() {
		return options;
	}

	/**
	 * @since 2.0
	 */
	public void setOptions(final PortOptions options) {
		this.options = options;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortDetails) {
			final PortDetails d = (PortDetails) obj;

			// @formatter:off
			return Objects.equal(totalGroupProfitAndLoss, d.totalGroupProfitAndLoss)
				&& Objects.equal(fuelConsumption, d.fuelConsumption)
				&& Objects.equal(capacityViolations, d.capacityViolations)
				&& Objects.equal(options, d.options)
						;
			// @formatter:on
		}

		return false;
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(options, capacityViolations, fuelConsumption, totalGroupProfitAndLoss);
	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(PortDetails.class)
				.add("fuelConsumption", fuelConsumption)
				.add("options", options)
				.add("capacityViolations", capacityViolations)
				.add("totalGroupProfitAndLoss", totalGroupProfitAndLoss)
				.toString();
		// @formatter:on
	}

	@Override
	public PortDetails clone() {
		return new PortDetails(new PortOptions(options), fuelConsumption, capacityViolations, totalGroupProfitAndLoss);
	}

	@Override
	public long getTotalGroupProfitAndLoss() {
		return totalGroupProfitAndLoss;
	}

	@Override
	public void setTotalGroupProfitAndLoss(final long totalGroupProfitAndLoss) {
		this.totalGroupProfitAndLoss = totalGroupProfitAndLoss;
	}

}
