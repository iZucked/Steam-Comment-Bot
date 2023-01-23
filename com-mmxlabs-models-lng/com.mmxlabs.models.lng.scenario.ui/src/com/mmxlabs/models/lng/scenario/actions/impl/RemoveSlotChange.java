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

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class RemoveSlotChange implements IRollForwardChange {

	private final Slot slot;
	private final Command command;

	public RemoveSlotChange(@NonNull final Slot slot, @NonNull final EditingDomain domain) {
		this.slot = slot;
		this.command = DeleteCommand.create(domain, slot);
	}

	@Override
	public EObject getChangedObject() {
		return slot;
	}

	@Override
	public String getMessage() {
		return String.format("Removing Slot %s", slot.getName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
