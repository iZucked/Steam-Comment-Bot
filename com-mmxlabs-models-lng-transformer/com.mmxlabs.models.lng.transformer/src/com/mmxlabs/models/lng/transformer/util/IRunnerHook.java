/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */

package com.mmxlabs.models.lng.transformer.util;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.ISequences;

// Temporary interface....
// Either requests a pre-computed sequences object, or reports the results.
public interface IRunnerHook {

	public static final @NonNull String PHASE_INITIAL = "initial";
	public static final @NonNull String PHASE_CLEAN_STATE = "cleanstate";
	public static final @NonNull String PHASE_LSO = "lso";
	public static final @NonNull String PHASE_HILL = "hill";
	public static final @NonNull String PHASE_ACTION_SETS = "actionset";

	public static List<@NonNull String> PHASE_ORDER = Lists.newArrayList(PHASE_INITIAL, PHASE_CLEAN_STATE, PHASE_LSO, PHASE_HILL, PHASE_ACTION_SETS);

	void beginPhase(@NonNull String phase, @Nullable Injector phaseInjector);

	@Nullable
	ISequences getPrestoredSequences(@NonNull String phase);

	void reportSequences(@NonNull String phase, @NonNull ISequences rawSequences);

	void endPhase(@NonNull String phase);
}
