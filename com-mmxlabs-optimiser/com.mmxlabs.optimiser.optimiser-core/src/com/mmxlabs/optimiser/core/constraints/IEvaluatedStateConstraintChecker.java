/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * {@link IEvaluatedStateConstraintChecker} implementations check {@link ISequences} for constraint violations. For example this could be to ensure sequence elements are assigned to particular
 * resources or to enforce rules on sequence element order. Such constraints are assumed to be hard, i.e. a constraint violation means the {@link ISequences} object is in an invalid state and should
 * never be allowed. This differs from a soft constraint which would be an allowed state, but penalised for it. Such soft constraints are likely to be implemented as a Fitness Function.
 * 
 * @author Simon Goodall
 * 
 */
public interface IEvaluatedStateConstraintChecker {

	/**
	 * Returns the name uniquely identifying this constraint checker.
	 * 
	 * @return
	 */
	@NonNull
	String getName();

	/**
	 * Check the {@link ISequences} object for constraint violations. Returns true if all constraints are satisfied. Returns false on a constraint violation.
	 * 
	 * @param sequences
	 * @return Returns true if all constraints are satisfied.
	 */
	boolean checkConstraints(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState);
}
