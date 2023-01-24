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

import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class RemoveVesselCharterChange implements IRollForwardChange {

	private final VesselCharter vessel;
	private final Command command;

	public RemoveVesselCharterChange(@NonNull final VesselCharter vessel, @NonNull final EditingDomain domain) {
		this.vessel = vessel;
		this.command = DeleteCommand.create(domain, vessel);
	}

	@Override
	public EObject getChangedObject() {
		return vessel;
	}

	@Override
	public String getMessage() {
		return String.format("Removing Vessel %s from scenario", vessel.getVessel().getName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
