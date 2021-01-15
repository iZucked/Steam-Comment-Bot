/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.exceptions;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;

public class InfeasibleSolutionException extends UserFeedbackException {

	private static final long serialVersionUID = 1L;
	
	public InfeasibleSolutionException(final String message) {
		super(message);
	}
}
