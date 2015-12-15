/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.ISequences;

public class AbstractRunnerHook implements IRunnerHook {
	protected String phase;
	protected Injector injector;

	@Override
	public void beginPhase(@NonNull String phase, @Nullable Injector injector) {
		this.phase = phase;
		this.injector = injector;
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

	public Injector getInjector() {
		return injector;
	}
}
