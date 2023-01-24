/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public interface IBreakPointHelper {

	List<Pair<ISequenceElement, ISequenceElement>> getValidBreaks();

	void init(IPhaseOptimisationData data);

}