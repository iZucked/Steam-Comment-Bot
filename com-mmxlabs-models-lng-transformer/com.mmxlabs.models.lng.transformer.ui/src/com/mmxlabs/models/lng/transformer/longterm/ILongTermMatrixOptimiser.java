/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import org.eclipse.jdt.annotation.NonNull;

public interface ILongTermMatrixOptimiser {

	/**
	 * Finds optimal pairings of slots given a value array
	 * @param values
	 * @return
	 */
	boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid);
}
