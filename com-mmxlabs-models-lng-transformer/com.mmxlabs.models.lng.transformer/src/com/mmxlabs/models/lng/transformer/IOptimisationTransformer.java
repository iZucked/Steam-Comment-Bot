/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

/**
 */
public interface IOptimisationTransformer {
	/**
	 * Create initial sequences; starts with the advice sequences (if there are any) and then uses the {@link ConstrainedInitialSequenceBuilder} to sort out any unsequenced elements.
	 * 
	 * @param data
	 * @return
	 */
	@NonNull
	ISequences createInitialSequences(@NonNull IPhaseOptimisationData data, @NonNull ModelEntityMap mem);

}