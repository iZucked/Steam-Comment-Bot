package com.mmxlabs.models.lng.scenario.actions.impl;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class RemoveCargoChange implements IRollForwardChange {

	private final Cargo cargo;
	private final Command command;

	public RemoveCargoChange(@NonNull final Cargo cargo, @NonNull final EditingDomain domain) {
		this.cargo = cargo;
		this.command = DeleteCommand.create(domain, cargo);
	}

	@Override
	public EObject getChangedObject() {
		return cargo;
	}

	@Override
	public String getMessage() {
		return String.format("Removing Cargo %s", cargo.getLoadName());
	}

	@Override
	public Command getCommand() {
		return command;
	}
}
