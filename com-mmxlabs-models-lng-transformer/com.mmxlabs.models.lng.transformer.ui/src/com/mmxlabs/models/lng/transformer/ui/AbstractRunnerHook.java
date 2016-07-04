/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.ISequences;

public class AbstractRunnerHook implements IRunnerHook {
	protected String phase;
	protected Injector injector;

	@Override
	public void beginPhase(@NonNull String phase, @Nullable Injector injector) {
		this.phase = phase;
		this.injector = injector;
	}

	@Override
	@Nullable
	public ISequences getPrestoredSequences(@NonNull String phase) {
		return null;
	}

	@Override
	public void reportSequences(@NonNull String phase, @NonNull ISequences rawSequences) {

	}

	@Override
	public void endPhase(@NonNull String phase) {

	}

	public String getPhase() {
		return phase;
	}

	public Injector getInjector() {
		return injector;
	}
}
