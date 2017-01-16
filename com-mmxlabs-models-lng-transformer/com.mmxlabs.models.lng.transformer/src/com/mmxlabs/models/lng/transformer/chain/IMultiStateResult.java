/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Results storing raw {@link ISequences} to {@link Map<String, Object>} extra annotations pairings
 * 
 * @author Simon Goodall
 *
 */
public interface IMultiStateResult {

	@NonNull
	NonNullPair<ISequences, Map<String, Object>> getBestSolution();

	@NonNull
	List<NonNullPair<ISequences, Map<String, Object>>> getSolutions();
}