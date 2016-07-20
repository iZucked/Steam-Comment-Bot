/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.stochasticactionsets;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class BreakEvenTransformerUnit implements ILNGStateTransformerUnit {

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private BreakEvenTransformerUnit t;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.run(monitor);
			}

			@Override
			public void init(SequencesContainer initialSequences, final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new BreakEvenTransformerUnit(dt, settings, initialSequences.getSequences(), inputState, dt.getHints());
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			@Override
			public IMultiStateResult getInputState() {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.getInputState();
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
	private final IMultiStateResult inputState;

	private long targetProfitAndLoss;

	@SuppressWarnings("null")
	public BreakEvenTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final OptimiserSettings settings, @NonNull ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;

		// TODO: Hook in as input e.g. from data model
		targetProfitAndLoss = 0L;// OptimiserUnitConvertor.convertToInternalFixedCost(settings.getBreakEvenProfitAndLoss());
		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		// FIXME: Disable main break even evalRuator
		// FIXME: Disable caches!

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(settings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_OptimiserSettingsModule(settings), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		// modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	@Override
	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@Override
	@NonNull
	public IMultiStateResult getInputState() {
		return inputState;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			monitor.beginTask("", 100);
			try {

				final BreakEvenOptimiser instance = injector.getInstance(BreakEvenOptimiser.class);
				final ISequences inputRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INPUT)));

				// FIXME: Disable main break even evalRuator
				// FIXME: Disable caches!

				// TODO: User parameter (derive from a parent case)?
				// long targetProfitAndLoss = 4_065_955L * 1000L;
				// long targetProfitAndLoss = 700_000_000L * 1000L;

				instance.optimise(inputRawSequences, targetProfitAndLoss);

				// Should we return a new state? No change in sequences, but change in P&L

				// final IMultiStateResult result = instance.optimise(inputRawSequences, new SubProgressMonitor(monitor, 95), 1000);
				// if (result != null) {
				// return result;
				// }
				return inputState;
			} catch (final Exception e) {
				e.printStackTrace();
				throw e;
				// } finally {
				// monitor.done();
				// // Clean up thread-locals created in the scope object
				// for (final Thread thread : threadCache.keySet()) {
				// scope.exit(thread);
				// }
				// threadCache.clear();
			}
		}
	}

}
