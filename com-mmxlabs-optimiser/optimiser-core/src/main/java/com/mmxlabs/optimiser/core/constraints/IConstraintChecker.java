/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import java.util.List;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * {@link IConstraintChecker} implementations check {@link ISequences} for
 * constraint violations. For example this could be to ensure sequence elements
 * are assigned to particular resources or to enforce rules on sequence element
 * order. Such constraints are assumed to be hard, i.e. a constraint violation
 * means the {@link ISequences} object is in an invalid state and should never
 * be allowed. This differs from a soft constraint which would be an allowed
 * state, but penalised for it. Such soft constraints are likely to be
 * implemented as a Fitness Function.
 * 
 * @author Simon Goodall
 *
 */ 
public interface IConstraintChecker {

	/**
	 * Returns the name uniquely identifying this constraint checker.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Check the {@link ISequences} object for constraint violations. Returns
	 * true if all constraints are satisfied. Returns false on a constraint
	 * violation.
	 * 
	 * @param sequences
	 * @return Returns true if all constraints are satisfied.
	 */
	boolean checkConstraints(ISequences sequences);

	/**
	 * Check the {@link ISequences} object for constraint violations. Returns
	 * true if all constraints are satisfied. Returns false on a constraint
	 * violation. This version of {@link #checkConstraints} takes an input list
	 * which may be used to store informative messages about any constraint
	 * violations.
	 * 
	 * @param sequences
	 * @param messages
	 *            List which may be used to store constraint violation messages.
	 * @return Returns true if all constraints are satisfied.
	 */
	boolean checkConstraints(ISequences sequences, List<String> messages);

	/**
	 * Provide the {@link IConstraintChecker} with the {@link IOptimisationData}
	 * object, where it can obtain it's source data.
	 * 
	 * @param optimisationData
	 */
	void setOptimisationData(IOptimisationData optimisationData);
}
