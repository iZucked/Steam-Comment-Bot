/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

@NonNullByDefault
@FunctionalInterface
public interface ResultRecorder {

	void record(ILoadOption load, IDischargeOption discharge, IResource resource, Pair<IAnnotatedSolution, EvaluationState> result);

}