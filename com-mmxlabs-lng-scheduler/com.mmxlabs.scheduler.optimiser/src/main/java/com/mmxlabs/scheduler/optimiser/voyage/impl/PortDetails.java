/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.EnumMap;

import com.google.common.base.Objects;
import com.mmxlabs.common.Equality;
import com.mmxlabs.common.impl.LongFastEnumMap;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Object recording the details of a port visit.
 * 
 * @author Simon Goodall
 * 
 */

public final class PortDetails implements Cloneable {

	private PortOptions options;

	private final EnumMap<FuelComponent, Long> fuelConsumption = new EnumMap<FuelComponent, Long>(FuelComponent.class);
	private final EnumMap<FuelComponent, Integer> fuelPrice = new EnumMap<FuelComponent, Integer>(FuelComponent.class);

	private final LongFastEnumMap<CapacityViolationType> capacityViolations = new LongFastEnumMap<CapacityViolationType>(CapacityViolationType.values().length);

	public PortDetails() {
	
	}

	// TODO: remove when finished recoding
	//private PortDetails(final int visitDuration, final IPortSlot portSlot, final EnumMap<FuelComponent, Long> fuelConsumption, final LongFastEnumMap<CapacityViolationType> capacityViolations) {
	private PortDetails(final PortOptions options, final EnumMap<FuelComponent, Long> fuelConsumption, final LongFastEnumMap<CapacityViolationType> capacityViolations) {
		//this.visitDuration = visitDuration;
		//this.portSlot = portSlot;
		this.options = options;
		this.fuelConsumption.putAll(fuelConsumption);
		this.capacityViolations.putAll(capacityViolations);
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
		return (int) fuelPrice.get(fuel);
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

	// TODO: remove when finished recoding
	/*
	public final int getVisitDuration() {
		return visitDuration;
	}

	public final void setVisitDuration(final int visitDuration) {
		this.visitDuration = visitDuration;
	}
	*/
	

	public final long getPortCost(final Object key) {
		throw new UnsupportedOperationException("Undefined API");
	}

	// TODO: remove when finished recoding
	/*
	public final IPortSlot getPortSlot() {
		return portSlot;
	}

	public final void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}
	*/

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

			// TODO: remove when finished recoding
			/*
			if (visitDuration != d.visitDuration) {
				return false;
			}
			*/

			if (!Equality.isEqual(capacityViolations, d.capacityViolations)) {
				return false;
			}

			if (!Equality.isEqual(fuelConsumption, d.fuelConsumption)) {
				return false;
			}
			
			// TODO: remove when finished recoding
			/*
			if (!Equality.isEqual(portSlot, d.portSlot)) {
				return false;
			}
			*/
			
			if (!Equality.isEqual(options, d.options)) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public final int hashCode() {

		// TODO: remove when finished recoding
		//return Objects.hashCode(visitDuration, capacityViolations, fuelConsumption, portSlot);
		return Objects.hashCode(options, capacityViolations, fuelConsumption);

	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(PortDetails.class)
				.add("fuelConsumption", fuelConsumption)
				.add("options", options)
				// TODO: remove when finished recoding
				/*
				.add("visitDuration", visitDuration)
				.add("portSlot", portSlot)
				*/
				.add("capacityViolations", capacityViolations)
				.toString();
		// @formatter:on
	}

	@Override
	public PortDetails clone() {
		return new PortDetails(new PortOptions(options), fuelConsumption, capacityViolations);
	}
}
