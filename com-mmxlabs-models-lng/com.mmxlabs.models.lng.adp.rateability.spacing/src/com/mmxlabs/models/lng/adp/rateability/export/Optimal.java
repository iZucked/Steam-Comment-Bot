/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.export;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNull;

public class Optimal extends FeasibleSolverResult {

	public Optimal(@NonNull final Command modelPopulationCommand) {
		super(modelPopulationCommand);
	}

}
