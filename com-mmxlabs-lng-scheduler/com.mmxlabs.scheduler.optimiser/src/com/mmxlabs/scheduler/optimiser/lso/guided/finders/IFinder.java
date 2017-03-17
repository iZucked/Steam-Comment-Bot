/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequence;

public interface IFinder {

	/**
	 * Returns the index for this element or -1 if not found
	 */
	int findInsertionIndex(@NonNull ISequence sequence);
}