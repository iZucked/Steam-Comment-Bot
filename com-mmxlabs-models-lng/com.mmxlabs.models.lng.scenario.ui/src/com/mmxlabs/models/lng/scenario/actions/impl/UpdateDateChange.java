/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;

@NonNullByDefault
public class UpdateDateChange implements IRollForwardChange {

	private final EObject object;
	private final String label;
	private final LocalDate date;
	private final Command command;

	public UpdateDateChange(final String label, final EObject object, final EAttribute feature, final LocalDate date, final EditingDomain domain) {
		this.label = label;
		this.object = object;
		this.date = date;
		this.command = SetCommand.create(domain, object, feature, date);
	}

	@Override
	public EObject getChangedObject() {
		return object;
	}

	@Override
	public Command getCommand() {
		return command;
	}

	@Override
	public String getMessage() {
		return String.format("Set %s to %s", label, date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
	}

}
