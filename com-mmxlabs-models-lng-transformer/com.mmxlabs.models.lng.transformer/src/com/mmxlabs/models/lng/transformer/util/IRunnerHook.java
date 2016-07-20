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
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.optimiser.core.ISequences;

// Temporary interface....
// Either requests a pre-computed sequences object, or reports the results.
public interface IRunnerHook {

	public static final @NonNull String STAGE_INITIAL = "initial";
	public static final @NonNull String STAGE_CLEAN_STATE = "cleanstate";
	public static final @NonNull String STAGE_LSO = "lso";
	public static final @NonNull String STAGE_HILL = "hill";
	public static final @NonNull String STAGE_ACTION_SETS = "actionset";

	public static List<@NonNull String> STAGE_ORDER = Lists.newArrayList(STAGE_INITIAL, STAGE_CLEAN_STATE, STAGE_LSO, STAGE_HILL, STAGE_ACTION_SETS);

	// Methods below follow lifecycle 
	
	void beginStage(@NonNull String stage);

	@Nullable
	ISequences getPrestoredSequences(@NonNull String stage, @NonNull LNGDataTransformer dataTransformer);

	void beginStageJob(@NonNull String stage, int jobID, @Nullable Injector stageJobInjector);

	void endStageJob(@NonNull String stage, int jobID, @Nullable Injector stageJobInjector);

	void reportSequences(@NonNull String stage, @NonNull ISequences rawSequences, @NonNull LNGDataTransformer dataTransformer);

	void endStage(@NonNull String stage);
}
