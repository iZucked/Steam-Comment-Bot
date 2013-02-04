/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;

/**
 * @since 2.0
 */
public interface IOptimisationTransformer {

//	IOptimisationContext createOptimisationContext(IOptimisationData data, ResourcelessModelEntityMap mem);
////
//	Pair<IOptimisationContext, LocalSearchOptimiser> createOptimiserAndContext(IOptimisationData data, ResourcelessModelEntityMap mem);
////
	/**
	 * Create initial sequences; starts with the advice sequences (if there are any) and then uses the {@link ConstrainedInitialSequenceBuilder} to sort out any unsequenced elements.
	 * 
	 * @param data
	 * @return
	 */
	ISequences createInitialSequences(IOptimisationData data, ResourcelessModelEntityMap mem);

}