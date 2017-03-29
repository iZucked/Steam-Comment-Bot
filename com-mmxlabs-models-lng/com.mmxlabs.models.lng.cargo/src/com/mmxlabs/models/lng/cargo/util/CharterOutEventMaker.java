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
import com.mmxlabs.models.lng.cargo.EVesselTankState;
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

		this.event.setAvailableHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
		this.event.setRequiredHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

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

	public CharterOutEventMaker withRequiredHeelOptions(int minVolumeInM3, int maxVolumeInM3, @NonNull EVesselTankState state, final @Nullable String priceExpression) {

		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		if (state == EVesselTankState.MUST_BE_WARM) {
			if (minVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
			if (maxVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
		}

		event.getRequiredHeel().setMinimumEndHeel(minVolumeInM3);
		event.getRequiredHeel().setMaximumEndHeel(maxVolumeInM3);
		event.getRequiredHeel().setTankState(state);
		event.getRequiredHeel().setPriceExpression(priceExpression);

		return this;
	}

	public CharterOutEventMaker withAvailableHeelOptions(double minVolumeInM3, double maxVolumeInM3, double cvValue, String pricePerMMBTu) {
		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		event.getAvailableHeel().setMinVolumeAvailable(minVolumeInM3);
		event.getAvailableHeel().setMaxVolumeAvailable(maxVolumeInM3);
		event.getAvailableHeel().setCvValue(cvValue);
		event.getAvailableHeel().setPriceExpression(pricePerMMBTu);

		return this;
	}

	public @NonNull CharterOutEventMaker withRepositioningFee(int fee) {
		event.setRepositioningFee(fee);
		return this;
	}

	public @NonNull CharterOutEventMaker withHireRate(int hireRate) {
		event.setHireRate(hireRate);
		return this;
	}

}
