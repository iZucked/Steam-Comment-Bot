/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;

public class CharterOutEventMaker {

	private final @NonNull CargoModelBuilder cargoModelBuilder;
	private final @NonNull CharterOutEvent event;

	public CharterOutEventMaker(@NonNull String name, @NonNull Port port, @NonNull LocalDateTime startAfter, @NonNull LocalDateTime startBy, @NonNull CargoModelBuilder cargoModelBuilder) {

		this.cargoModelBuilder = cargoModelBuilder;
		this.event = CargoFactory.eINSTANCE.createCharterOutEvent();

		this.event.setHeelOptions(FleetFactory.eINSTANCE.createHeelOptions());

		this.event.setName(name);

		this.event.setPort(port);
		this.event.setStartBy(startBy);
		this.event.setStartAfter(startAfter);
	}

	@NonNull
	public CharterOutEventMaker withDurationInDays(int durationInDays) {
		event.setDurationInDays(durationInDays);
		return this;
	}

	@NonNull
	public CharterOutEventMaker withVesselAssignment(@NonNull final VesselAvailability vesselAvailability, final int sequenceHint) {
		event.setVesselAssignmentType(vesselAvailability);
		event.setSequenceHint(sequenceHint);

		event.setSpotIndex(0);
		if (!event.getAllowedVessels().isEmpty()) {
			throw new IllegalStateException("set vessel restrictions after setting vessel assignment");
		}
		event.getAllowedVessels().clear();
		event.getAllowedVessels().add(vesselAvailability.getVessel());
		return this;
	}

	@NonNull
	public CharterOutEventMaker withRelocatePort(@Nullable Port relocationPort) {
		if (relocationPort == null) {
			event.unsetRelocateTo();
		} else {
			event.setRelocateTo(relocationPort);
		}
		return this;
	}

	public CharterOutEventMaker withAllowedVessels(Vessel... vessels) {

		event.getAllowedVessels().clear();
		for (Vessel v : vessels) {
			event.getAllowedVessels().add(v);
		}

		return this;
	}

	@NonNull
	public CharterOutEvent build() {
		cargoModelBuilder.getCargoModel().getVesselEvents().add(event);
		return event;
	}

	public CharterOutEventMaker withEndHeelOptions(double volumeInM3, double cvValue, double pricePerMMBTu) {

		event.getHeelOptions().setVolumeAvailable(volumeInM3);
		event.getHeelOptions().setCvValue(cvValue);
		event.getHeelOptions().setPricePerMMBTU(pricePerMMBTu);

		return this;
	}

	public @NonNull CharterOutEventMaker withRepositioningFee(int fee) {
		event.setRepositioningFee(fee);
		return this;
	}

}
