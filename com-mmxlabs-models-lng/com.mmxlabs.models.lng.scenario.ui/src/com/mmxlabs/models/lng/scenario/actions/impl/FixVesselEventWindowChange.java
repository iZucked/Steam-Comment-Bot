/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class FixVesselEventWindowChange implements IRollForwardChange {

	private final VesselEvent event;
	private final CompoundCommand command;

	public FixVesselEventWindowChange(@NonNull final VesselEvent slot, @NonNull final ZonedDateTime windowStart, @NonNull final EditingDomain domain) {
		this.event = slot;
		this.command = new CompoundCommand("Fix Event Windows");

		final LocalDateTime dt = windowStart.withZoneSameInstant(ZoneId.of(slot.getPort().getLocation().getTimeZone())).toLocalDateTime();
		this.command.append(SetCommand.create(domain, slot, CargoPackage.Literals.VESSEL_EVENT__START_AFTER, dt));
		this.command.append(SetCommand.create(domain, slot, CargoPackage.Literals.VESSEL_EVENT__START_BY, dt));
	}

	@Override
	public EObject getChangedObject() {
		return event;
	}

	@Override
	public String getMessage() {
		return String.format("Change start window for Vessel Event %s", event.getName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
