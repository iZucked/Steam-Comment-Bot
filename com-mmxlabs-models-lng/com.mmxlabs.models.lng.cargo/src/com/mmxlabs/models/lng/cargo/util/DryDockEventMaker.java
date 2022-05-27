/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.port.Port;

public class DryDockEventMaker {

	private final @NonNull CargoModelBuilder cargoModelBuilder;
	private final @NonNull DryDockEvent event;

	public DryDockEventMaker(@NonNull String name, @NonNull Port port, @NonNull LocalDateTime startAfter, @NonNull LocalDateTime startBy, @NonNull CargoModelBuilder cargoModelBuilder) {

		this.cargoModelBuilder = cargoModelBuilder;
		this.event = CargoFactory.eINSTANCE.createDryDockEvent();

		this.event.setName(name);

		this.event.setPort(port);
		this.event.setStartBy(startBy);
		this.event.setStartAfter(startAfter);
	}

	@NonNull
	public DryDockEventMaker withDurationInDays(int durationInDays) {
		event.setDurationInDays(durationInDays);
		return this;
	}

	@NonNull
	public DryDockEventMaker withVesselAssignment(@NonNull final VesselCharter vesselCharter, final int sequenceHint) {
		event.setVesselAssignmentType(vesselCharter);
		event.setSequenceHint(sequenceHint);

		event.setSpotIndex(0);
		event.getAllowedVessels().clear();
		event.getAllowedVessels().add(vesselCharter.getVessel());
		return this;
	}

	@NonNull
	public DryDockEvent build() {
		cargoModelBuilder.getCargoModel().getVesselEvents().add(event);
		return event;
	}
}
