/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.ISequences;

public class AbstractRunnerHook implements IRunnerHook {

	private String stage;

	private ThreadLocal<Integer> currentJobId = new ThreadLocal<>();

	@Override
	public final void beginStage(@NonNull String stage) {
		this.stage = stage;
		doBeginStage(stage);
	}

	@Override
	@Nullable
	public final ISequences getPrestoredSequences(@NonNull String stage, @NonNull LNGDataTransformer dataTransformer) {
		if (!stage.equals(this.stage)) {
			throw new IllegalStateException("Stages does not match");
		}
		return doGetPrestoredSequences(stage, dataTransformer);
	}

	@Override
	public final void beginStageJob(@NonNull String stages, int jobId, @Nullable Injector injector) {
		if (!stage.equals(this.stage)) {
			throw new IllegalStateException("Stages does not match");
		}
		if (currentJobId.get() != null) {
			throw new IllegalStateException("Existing job already running");
		}
		currentJobId.set(jobId);
		doBeginStageJob(stages, jobId, injector);
	}

	public final @NonNull String getStageAndJobID() {
		int jobID = currentJobId.get();
		// Note: this is the format expected by the results server parser. Should perhaps be encoded in headless app rather than here.
		return String.format("%s_%d", stage, jobID);
	}

	@Override
	public final void endStageJob(@NonNull String stage, int jobID, @Nullable Injector injector) {
		if (!stage.equals(this.stage)) {
			throw new IllegalStateException("Stages does not match");
		}
		doEndStageJob(stage, jobID, injector);
		currentJobId.remove();
	}

	@Override
	public final void reportSequences(@NonNull String stage, @NonNull ISequences rawSequences, @NonNull LNGDataTransformer dataTransformer) {
		if (!stage.equals(this.stage)) {
			throw new IllegalStateException("Stages does not match");
		}
		doReportSequences(stage, rawSequences, dataTransformer);
	}

	@Override
	public final void endStage(@NonNull String stage) {
		if (!stage.equals(this.stage)) {
			throw new IllegalStateException("Stages does not match");
		}
		doEndStage(stage);
		this.stage = null;
	}

	protected void doBeginStage(@NonNull String stage) {
	}

	protected void doBeginStageJob(@NonNull String stage, int jobID, @Nullable Injector injector) {
	}

	protected void doEndStage(@NonNull String stage) {
	}

	protected void doEndStageJob(@NonNull String stage, int jobID, @Nullable Injector injector) {
	}

	protected @Nullable ISequences doGetPrestoredSequences(@NonNull String stage, @NonNull LNGDataTransformer dataTransformer) {
		return null;
	}

	protected void doReportSequences(@NonNull String stage, @NonNull ISequences rawSequences, @NonNull LNGDataTransformer dataTransformer) {
	}
}
