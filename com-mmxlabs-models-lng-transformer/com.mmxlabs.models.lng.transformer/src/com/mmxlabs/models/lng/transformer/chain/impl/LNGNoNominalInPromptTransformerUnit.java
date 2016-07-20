/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.NoNominalInPromptTransformer;

public class LNGNoNominalInPromptTransformerUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LNGNoNominalInPromptTransformerUnit.class);

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final UserSettings userSettings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				LNGNoNominalInPromptTransformerUnit t = new LNGNoNominalInPromptTransformerUnit(dt, userSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(),
						dt.getHints());
				return t.run(monitor);
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final NoNominalInPromptTransformer transformer;

	@NonNull
	private final IMultiStateResult inputState;

	public LNGNoNominalInPromptTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings, @NonNull ISequences initialSequences,
			@NonNull final ISequences inputSequences, @NonNull final Collection<@NonNull String> hints) {
		this.dataTransformer = dataTransformer;

		final List<Module> modules = new LinkedList<>();

		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));

		injector = dataTransformer.getInjector().createChildInjector(modules);
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			transformer = injector.getInstance(NoNominalInPromptTransformer.class);
			inputState = new MultiStateResult(inputSequences, new HashMap<>());
		}
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			monitor.beginTask("", 1);
			try {

				ISequences newResult = transformer.run(inputState.getBestSolution().getFirst());
				return new MultiStateResult(newResult, new HashMap<>());
			} finally {
				monitor.done();
			}
		}
	}
}
