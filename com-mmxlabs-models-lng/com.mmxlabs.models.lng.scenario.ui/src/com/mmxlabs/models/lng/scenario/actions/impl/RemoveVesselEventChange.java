/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class RemoveVesselEventChange implements IRollForwardChange {

	private final VesselEvent event;
	private final Command command;

	public RemoveVesselEventChange(@NonNull final VesselEvent event, @NonNull final EditingDomain domain) {
		this.event = event;
		this.command = DeleteCommand.create(domain, event);
	}

	@Override
	public EObject getChangedObject() {
		return event;
	}

	@Override
	public String getMessage() {
		return String.format("Removing Vessel Event %s", event.getName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
