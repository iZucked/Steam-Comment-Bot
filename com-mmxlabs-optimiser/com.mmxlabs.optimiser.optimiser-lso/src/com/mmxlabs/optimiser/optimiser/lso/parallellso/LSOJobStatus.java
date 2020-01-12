/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

public enum LSOJobStatus {
	NullMoveFail, CannotValidateFail, ConstraintFail, EvaluationProcessFail, EvaluatedConstraintFail, Pass;
	
	public boolean isSuccess() {
		return this == Pass;
	}
}
