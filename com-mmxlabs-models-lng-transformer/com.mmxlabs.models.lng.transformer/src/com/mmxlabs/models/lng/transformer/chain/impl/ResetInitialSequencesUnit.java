/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;

public class ResetInitialSequencesUnit {

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ResetInitialSequencesStage stageSettings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private SequencesContainer initialSequencesContainer;
			private IMultiStateResult inputState;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (inputState == null || initialSequencesContainer == null) {
					throw new IllegalStateException("#init has not been called");
				}
				monitor.beginTask("Reset initial sequences", 1);
				try {
					initialSequencesContainer.setSequences(inputState.getBestSolution().getFirst());
					monitor.worked(1);
				} finally {
					monitor.done();
				}
				return inputState;
			}

			@Override
			public void init(final SequencesContainer initialSequences, final IMultiStateResult inputState) {

				this.initialSequencesContainer = initialSequences;
				this.inputState = inputState;
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			@Override
			public IMultiStateResult getInputState() {
				if (inputState == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return inputState;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

}
