/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

public enum LSOJobStatus {
	NullMoveFail, CannotValidateFail, ConstraintFail, EvaluationProcessFail, EvaluatedConstraintFail, Pass;
	
	public boolean isSuccess() {
		return this == Pass;
	}
}
