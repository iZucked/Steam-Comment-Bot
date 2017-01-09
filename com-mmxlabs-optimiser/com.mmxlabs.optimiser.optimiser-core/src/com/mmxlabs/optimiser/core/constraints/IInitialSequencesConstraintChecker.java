/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;

/**
 * The {@link IInitialSequencesConstraintChecker} is an extended version of an {@link IConstraintChecker}. A {@link IConstraintChecker} which also implements {@link IInitialSequencesConstraintChecker}
 * requires an initial sequence, which it stores as state and uses in constraint checking.
 * 
 * @author achurchill
 * 
 */
public interface IInitialSequencesConstraintChecker extends IConstraintChecker {

	/**
	 * Pass through an {@link ISequences} object which is considered to be valid even if it violates the basic constraint. This will be invoked whenever a {@link ISequences} state is accepted.
	 * 
	 * @param sequences
	 */
	void sequencesAccepted(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences);

}
