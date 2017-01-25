/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;

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