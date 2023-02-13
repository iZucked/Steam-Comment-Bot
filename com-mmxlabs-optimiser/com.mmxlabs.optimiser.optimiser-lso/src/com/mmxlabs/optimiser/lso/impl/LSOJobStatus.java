/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

public enum LSOJobStatus {
	NullMoveFail, CannotValidateFail, ConstraintFail, EvaluationProcessFail, EvaluatedConstraintFail, Pass;
	
	public boolean isSuccess() {
		return this == Pass;
	}
}
