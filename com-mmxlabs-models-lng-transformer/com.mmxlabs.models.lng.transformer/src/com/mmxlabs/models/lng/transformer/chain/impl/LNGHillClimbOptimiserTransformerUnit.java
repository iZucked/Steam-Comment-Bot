package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
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
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.ArbitraryStateLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGHillClimbOptimiserTransformerUnit implements ILNGStateTransformerUnit {

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, final @NonNull OptimiserSettings settings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGHillClimbOptimiserTransformerUnit t;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.run(monitor);
			}

			@Override
			public void init(final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGHillClimbOptimiserTransformerUnit(dt, settings, inputState.getBestSolution().getFirst(), dt.getHints());
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
	private final ArbitraryStateLocalSearchOptimiser optimiser;

	@NonNull
	private final IMultiStateResult inputState;

	@SuppressWarnings("null")
	public LNGHillClimbOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final OptimiserSettings settings, @NonNull final ISequences inputSequences,
			@NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InputSequencesModule(inputSequences));
		modules.addAll(
				LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(settings), services, IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
		modules.addAll(
				LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_OptimiserSettingsModule(settings), services, IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		injector = dataTransformer.getInjector().createChildInjector(modules);
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			final ConstrainedMoveGenerator cmg = injector.getInstance(ConstrainedMoveGenerator.class);
			cmg.setRandom(new Random(dataTransformer.getOptimiserSettings().getSeed()));

			optimiser = injector.getInstance(ArbitraryStateLocalSearchOptimiser.class);
			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();
			final IAnnotatedSolution startSolution = optimiser.start(injector.getInstance(IOptimisationContext.class), injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL))),
					inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
			inputState = new MultiStateResult(inputSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(startSolution));
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
	public IMultiStateResult getInputState() {
		return inputState;
	}

	@Override
	public IMultiStateResult run(final IProgressMonitor monitor) {
		
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			final ISequences preloadedResult = runnerHook.getSequences(IRunnerHook.PHASE_HILL);
			if (preloadedResult != null) {
				monitor.beginTask("", 1);
				try {
					monitor.worked(1);
					return new MultiStateResult(preloadedResult, new HashMap<>());
				} finally {
					monitor.done();
				}
			}
		}

		
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			monitor.beginTask("", 100);
			try {
				while (!optimiser.isFinished()) {
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					optimiser.step(1);
					monitor.worked(1);
				}
				assert optimiser.isFinished();

				final IAnnotatedSolution bestSolution = optimiser.getBestSolution();
				final ISequences bestRawSequences = optimiser.getBestRawSequences();
				
				if (runnerHook != null) {
					runnerHook.reportSequences(IRunnerHook.PHASE_HILL, bestRawSequences);
				}
				
				if (bestRawSequences != null && bestSolution != null) {
					return new MultiStateResult(bestRawSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(bestSolution));
				} else {
					throw new RuntimeException("Unable to optimise");
				}
			} finally {
				monitor.done();
			}
		}
	}

}
