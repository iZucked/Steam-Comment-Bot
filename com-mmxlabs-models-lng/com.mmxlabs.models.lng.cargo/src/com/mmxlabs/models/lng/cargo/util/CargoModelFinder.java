/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;

public class CargoModelFinder {
	private final @NonNull CargoModel cargoModel;

	public CargoModelFinder(final @NonNull CargoModel cargoModel) {
		this.cargoModel = cargoModel;
	}

	@NonNull
	public CargoModel getCargoModel() {
		return cargoModel;
	}

	@NonNull
	public Cargo findCargo(@NonNull final String name) {
		for (final Cargo cargo : getCargoModel().getCargoes()) {
			if (name.equals(cargo.getLoadName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown cargo");
	}

	@NonNull
	public LoadSlot findLoadSlot(@NonNull final String name) {
		for (final LoadSlot cargo : getCargoModel().getLoadSlots()) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown load slot");
	}

	@NonNull
	public DischargeSlot findDischargeSlot(@NonNull final String name) {
		for (final DischargeSlot cargo : getCargoModel().getDischargeSlots()) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown discharge slot");
	}

	@NonNull
	public VesselEvent findVesselEvent(@NonNull final String name) {
		for (final VesselEvent vesselEvent : getCargoModel().getVesselEvents()) {
			if (name.equals(vesselEvent.getName())) {
				return vesselEvent;
			}
		}
		throw new IllegalArgumentException("Unknown vessel event");
	}
}
