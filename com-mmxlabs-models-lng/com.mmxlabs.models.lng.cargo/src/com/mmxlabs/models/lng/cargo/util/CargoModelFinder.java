/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;

public class CargoModelFinder {
	private final @NonNull CargoModel cargoModel;
	private List<LoadSlot> extraLoads = new LinkedList<>();
	private List<DischargeSlot> extraDischarges = new LinkedList<>();

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
		for (final LoadSlot cargo : extraLoads) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown load slot: " + name);
	}

	public @NonNull LoadSlot findLoadSlotByUUID(@NonNull final String uuid) {
		for (final LoadSlot slot : getCargoModel().getLoadSlots()) {
			if (uuid.equals(slot.getUuid())) {
				return slot;
			}
		}
		for (final LoadSlot slot : extraLoads) {
			if (uuid.equals(slot.getUuid())) {
				return slot;
			}
		}
		throw new IllegalArgumentException("Unknown load slot: " + uuid);
	}

	public @NonNull DischargeSlot findDischargeSlotByUUID(@NonNull final String uuid) {
		for (final DischargeSlot slot : getCargoModel().getDischargeSlots()) {
			if (uuid.equals(slot.getUuid())) {
				return slot;
			}
		}
		for (final DischargeSlot slot : extraDischarges) {
			if (uuid.equals(slot.getUuid())) {
				return slot;
			}
		}
		throw new IllegalArgumentException("Unknown discharge slot: " + uuid);
	}

	@NonNull
	public DischargeSlot findDischargeSlot(@NonNull final String name) {
		for (final DischargeSlot cargo : getCargoModel().getDischargeSlots()) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		for (final DischargeSlot cargo : extraDischarges) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown discharge slot: " + name);
	}

	@NonNull
	public Slot findSlot(@NonNull final String name) {
		for (final LoadSlot cargo : getCargoModel().getLoadSlots()) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		for (final DischargeSlot cargo : getCargoModel().getDischargeSlots()) {
			if (name.equals(cargo.getName())) {
				return cargo;
			}
		}
		throw new IllegalArgumentException("Unknown slot");
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

	public @NonNull VesselEvent findVesselEventByUUID(@NonNull final String uuid) {
		for (final VesselEvent vesselEvent : getCargoModel().getVesselEvents()) {
			if (uuid.equals(vesselEvent.getUuid())) {
				return vesselEvent;
			}
		}
		throw new IllegalArgumentException("Unknown vessel event");
	}

	@NonNull
	public VesselAvailability findVesselAvailability(@NonNull final String vesselName) {
		for (final VesselAvailability vesselAvailability : getCargoModel().getVesselAvailabilities()) {
			final Vessel vessel = vesselAvailability.getVessel();
			if (vessel != null) {
				if (vesselName.equals(vessel.getName())) {
					return vesselAvailability;
				}
			}
		}
		throw new IllegalArgumentException("Unknown vessel availability");
	}

	public void includeLoads(@Nullable List<LoadSlot> extra) {
		extraLoads.addAll(extra);
	}

	public void includeDischarges(@Nullable List<DischargeSlot> extra) {
		extraDischarges.addAll(extra);
	}
}
