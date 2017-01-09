/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;

/**
 * The {@link IReducingConstraintChecker} is an extended version of an {@link IConstraintChecker}. A {@link IConstraintChecker} which also implements {@link IReducingConstraintChecker} permits some
 * constraint violations providing that the number of violations does not increase. This permits the optimisation to start with a less than ideal situation and not make it worse. For example, let's
 * assume only 4 out of a set of 8 sequence elements are permitted in the solution. However a user has constructed an initial solution with 5 of these elements. Normally this would be rejected as an
 * invalid solution, but if we implements the {@link IReducingConstraintChecker} interface we can record the initial count of 5 and use that as the limit rather than 4 and permit the optimisation to
 * continue and remove the element. As the count reduces, the constraint checker can revert back to normal behaviour.
 * 
 * @author Simon Goodall
 * 
 */
public interface IReducingConstraintChecker extends IConstraintChecker {

	/**
	 * Pass through an {@link ISequences} object which is considered to be valid even if it violates the basic constraint. This will be invoked whenever a {@link ISequences} state is accepted.
	 * 
	 * @param sequences
	 */
	void sequencesAccepted(@NonNull ISequences sequences);

}
