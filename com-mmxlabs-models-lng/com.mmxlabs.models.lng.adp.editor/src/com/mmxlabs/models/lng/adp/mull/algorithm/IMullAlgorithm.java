/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IMullAlgorithm {
	public void run();

	public List<InventoryLocalState> getInventoryLocalStates();

	public AlgorithmState getAlgorithmState();

	public GlobalStatesContainer getGlobalStatesContainer();
}
