/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

public interface IRollForwardChange {

	@NonNull
	EObject getChangedObject();

	@NonNull
	String getMessage();

	@NonNull
	Command getCommand();
}