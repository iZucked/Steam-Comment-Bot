/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;

public class AbstractRunnerHook implements IRunnerHook {
	private String phase;

	public void beginPhase(@NonNull String phase) {
		this.phase = phase;
	}

	@Nullable
	public ISequences getPrestoredSequences(@NonNull String phase) {
		return null;
	}

	public void reportSequences(@NonNull String phase, @NonNull ISequences rawSequences) {

	}

	public void endPhase(@NonNull String phase) {

	}

	public String getPhase() {
		return phase;
	}
}
