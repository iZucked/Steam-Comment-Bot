/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.export;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNull;

public abstract class FeasibleSolverResult implements SpacingRateabilitySolverResult {

	@NonNull
	private final Command modelPopulationCommand;

	protected FeasibleSolverResult(@NonNull final Command modelPopulationCommand) {
		this.modelPopulationCommand = modelPopulationCommand;
	}

	@NonNull
	public Command getModelPopulationCommand() {
		return this.modelPopulationCommand;
	}
}
