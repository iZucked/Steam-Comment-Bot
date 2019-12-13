/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.NonNull;

public class UpdateStep implements UpdateItem {
	private String displayString;

	private Consumer<CompoundCommand> action;

	public Consumer<CompoundCommand> getAction() {
		return action;
	}

	public UpdateStep(String displayString) {
		this.displayString = displayString;
	}

	public UpdateStep(String displayString, Consumer<CompoundCommand> action) {
		this.displayString = displayString;
		this.action = action;
	}

	@Override
	public @NonNull String toString() {
		return displayString;
	}
}