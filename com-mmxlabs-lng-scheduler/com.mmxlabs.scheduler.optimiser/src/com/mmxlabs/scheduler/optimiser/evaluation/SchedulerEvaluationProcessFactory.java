/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;

public final class SchedulerEvaluationProcessFactory implements IEvaluationProcessFactory {

	@NonNull
	public static final String NAME = "SchedulerEvaluationProcess";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IEvaluationProcess instantiate() {
		return new SchedulerEvaluationProcess();
	}

}
