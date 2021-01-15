/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class FreezeCargoChange implements IRollForwardChange {

	private final Cargo cargo;
	private final CompoundCommand command;

	public FreezeCargoChange(@NonNull final Cargo cargo, @NonNull final EditingDomain domain) {
		this.cargo = cargo;
		this.command = new CompoundCommand("Freeze Cargo");
		this.command.append(SetCommand.create(domain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));
		this.command.append(SetCommand.create(domain, cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.FALSE));
	}

	@Override
	public EObject getChangedObject() {
		return cargo;
	}

	@Override
	public String getMessage() {
		return String.format("Lock vessel assignment and wiring for Cargo %s", cargo.getLoadName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
