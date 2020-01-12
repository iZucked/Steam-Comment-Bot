/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;

public class UpdateError extends UpdateWarning {

	public UpdateError(String displayString, Consumer<CompoundCommand> action) {
		super(displayString, action);
	}

	public UpdateError(String displayString) {
		super(displayString);
	}

	public UpdateError(String displayString, String quickFixMsg, Consumer<CompoundCommand> quickFix) {
		super(displayString, null);
	}

}
