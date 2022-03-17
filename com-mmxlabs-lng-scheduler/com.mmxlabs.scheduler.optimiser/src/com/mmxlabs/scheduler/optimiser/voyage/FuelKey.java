/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

public final class FuelKey {

	private final @NonNull FuelComponent fuelComponent;
	private final @NonNull FuelUnit fuelUnit;
	private final @NonNull IBaseFuel baseFuel;
	private final int hash;

	public FuelKey(final @NonNull FuelComponent fuelComponent, final @NonNull FuelUnit fuelUnit, final @NonNull IBaseFuel baseFuel) {
		this.fuelComponent = fuelComponent;
		this.fuelUnit = fuelUnit;
		this.baseFuel = baseFuel;
		// this.hash = Objects.hash(fuelComponent, fuelUnit, baseFuel);

		int result = 1;
		result = 31 * result + fuelComponent.ordinal();
		result = 31 * result + fuelUnit.ordinal();
		result = 31 * result + baseFuel.hashCode();
		this.hash = result;
	}

	@Override
	public final int hashCode() {
		return hash;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof FuelKey) {
			final FuelKey other = (FuelKey) obj;
			final FuelComponent otherFuelComponent = other.getFuelComponent();
			final FuelUnit otherFuelUnit = other.getFuelUnit();
			final IBaseFuel otherBaseFuel = other.getBaseFuel();

			return fuelComponent == otherFuelComponent && fuelUnit == otherFuelUnit && baseFuel == otherBaseFuel;
		}
		return false;
	}

	@Override
	public final String toString() {
		return ("[Component : " + fuelComponent + ", Unit : " + fuelUnit + ", baseFuel : " + baseFuel + ", hash : " + hash + "]");

	}

	public final @NonNull FuelComponent getFuelComponent() {
		return fuelComponent;
	}

	public final @NonNull FuelUnit getFuelUnit() {
		return fuelUnit;
	}

	public final @NonNull IBaseFuel getBaseFuel() {
		return baseFuel;
	}
}
