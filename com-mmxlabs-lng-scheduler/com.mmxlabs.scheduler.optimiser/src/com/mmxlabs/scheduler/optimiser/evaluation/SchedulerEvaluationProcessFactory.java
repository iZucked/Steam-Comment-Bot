/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;

public final class SchedulerEvaluationProcessFactory implements IEvaluationProcessFactory {

	public static final String NAME = "SchedulerEvaluationProcess";

	@Override
	public String getName() {
		assert NAME != null;
		return NAME;
	}

	@Override
	public IEvaluationProcess instantiate() {
		return new SchedulerEvaluationProcess();
	}

}
