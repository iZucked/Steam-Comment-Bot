/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDate;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

public class UpdateDateChange implements IRollForwardChange {
	
	private @NonNull EObject object;
	private String label;
	private LocalDate date;
	private Command command;

	public UpdateDateChange(@NonNull final String label, @NonNull final EObject object, @NonNull final EAttribute feature, @NonNull final LocalDate date, @NonNull final EditingDomain domain) {
		this.label = label;
		this.object = object;
		this.date = date;
		this.command = SetCommand.create(domain, object, feature, date);
	}

	@Override
	public @NonNull EObject getChangedObject() {
		// TODO Auto-generated method stub
		return object;
	}

	@Override
	public @NonNull Command getCommand() {
		// TODO Auto-generated method stub
		return command;
	}

	@Override
	public @NonNull String getMessage() {
		// TODO Auto-generated method stub
		return String.format("Set %s to %s", label, date.toString());
	}

}
