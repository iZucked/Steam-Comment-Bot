/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class FixSlotWindowChange implements IRollForwardChange {

	private final Slot<?> slot;
	private final CompoundCommand command;

	public FixSlotWindowChange(@NonNull final Slot<?> slot, @NonNull ZonedDateTime windowStart, @NonNull final EditingDomain domain) {
		this.slot = slot;
		this.command = new CompoundCommand("Fix Slot Windows");

		final LocalDateTime dt = windowStart.withZoneSameInstant(ZoneId.of(slot.getPort().getLocation().getTimeZone())).toLocalDateTime();

		this.command.append(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START, dt.toLocalDate()));
		this.command.append(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, dt.getHour()));
		this.command.append(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE, 0));
	}

	@Override
	public EObject getChangedObject() {
		return slot;
	}

	@Override
	public String getMessage() {
		return String.format("Change start window for slot %s", slot.getName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
