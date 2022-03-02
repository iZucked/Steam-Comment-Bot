/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Modifies the state of {@link IDataComponentProvider}, etc.
 * For example if one step of the chain modifies something that we want to be
 * ignored or added to a constraint checker down the line.
 * 
 * @author alex
 *
 */
public interface ILightWeightPostOptimisationStateModifier {
	/**
	 * Modifies state of providers, constraint checkers, etc.
	 * @param outputSequences
	 */
	void modifyState(ISequences outputSequences);
}
