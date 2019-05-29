/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class FreezeSlotChange implements IRollForwardChange {
	@NonNull private final Slot<?> slot;
	@NonNull private Command command;

	public FreezeSlotChange(@NonNull final Slot<?> slot, @NonNull final EditingDomain domain) {
		this.slot = slot;
		this.command = SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__LOCKED, Boolean.TRUE);
	}
	
	@Override
	public @NonNull EObject getChangedObject() {
		return slot;
	}

	@Override
	public @NonNull String getMessage() {
		return String.format("Lock Slot '%s'", slot.getName());
	}

	@Override
	public @NonNull Command getCommand() {
		return command;
	}

}
