/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.port.Port;

public class MaintenanceEventMaker {

	private final @NonNull CargoModelBuilder cargoModelBuilder;
	private final @NonNull MaintenanceEvent event;

	public MaintenanceEventMaker(@NonNull String name, @NonNull Port port, @NonNull LocalDateTime startAfter, @NonNull LocalDateTime startBy, @NonNull CargoModelBuilder cargoModelBuilder) {

		this.cargoModelBuilder = cargoModelBuilder;
		this.event = CargoFactory.eINSTANCE.createMaintenanceEvent();

		this.event.setName(name);

		this.event.setPort(port);
		this.event.setStartBy(startBy);
		this.event.setStartAfter(startAfter);
	}

	@NonNull
	public MaintenanceEventMaker withDurationInDays(int durationInDays) {
		event.setDurationInDays(durationInDays);
		return this;
	}

	@NonNull
	public MaintenanceEventMaker withVesselAssignment(@NonNull final VesselCharter vesselCharter, final int sequenceHint) {
		event.setVesselAssignmentType(vesselCharter);
		event.setSequenceHint(sequenceHint);

		event.setSpotIndex(0);
		event.getAllowedVessels().clear();
		event.getAllowedVessels().add(vesselCharter.getVessel());
		return this;
	}

	@NonNull
	public MaintenanceEvent build() {
		cargoModelBuilder.getCargoModel().getVesselEvents().add(event);
		return event;
	}
}
