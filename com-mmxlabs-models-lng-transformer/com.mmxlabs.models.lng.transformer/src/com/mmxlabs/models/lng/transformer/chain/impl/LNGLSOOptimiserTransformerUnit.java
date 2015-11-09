package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
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
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGLSOOptimiserTransformerUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LNGLSOOptimiserTransformerUnit.class);

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGLSOOptimiserTransformerUnit t;

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
				t = new LNGLSOOptimiserTransformerUnit(dt, settings, inputState.getBestSolution().getFirst(), dt.getHints());
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
	public static IChainLink chainPool(@NonNull final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, final int progressTicks, final int numCopies,
			@NonNull final ExecutorService executorService) {
		final IChainLink link = new IChainLink() {

			private LNGLSOOptimiserTransformerUnit[] t;

			class MyRunnable implements Callable<IMultiStateResult> {

				LNGLSOOptimiserTransformerUnit t;
				IProgressMonitor m;

				public MyRunnable(final LNGLSOOptimiserTransformerUnit t, final IProgressMonitor monitor, final int ticks) {
					this.t = t;
					this.m = new SubProgressMonitor(monitor, ticks);
				}

				@Override
				public IMultiStateResult call() {
					return t.run(m);
				}
			}

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				try {
					if (t == null) {
						throw new IllegalStateException("#init has not been called");
					}

					monitor.beginTask("", 100 * numCopies);

					final List<Future<IMultiStateResult>> results = new ArrayList<>(numCopies);
					for (int i = 0; i < numCopies; ++i) {
						results.add(executorService.submit(new MyRunnable(t[i], monitor, 100)));
					}

					for (final Future<IMultiStateResult> f : results) {
						try {
							f.get();
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
						}
					}

					// TODO: Attach ranking, combine solutions.
					final IMultiStateResult r = results.get(0).get();
					if (r == null) {
						throw new IllegalStateException();
					}

					// DEBUG: Sanity checking each solution should get the same fitness
					final Map<String, Long> original = createFitessMap(r);
					for (final Future<IMultiStateResult> f : results) {
						final IMultiStateResult result = f.get();
						final Map<String, Long> c = createFitessMap(result);
						if (c.equals(original) == false) {
							final int ii = 0;
						}
					}
					return r;
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				} finally {
					monitor.done();
				}
			}

			private Map<String, Long> createFitessMap(final IMultiStateResult r) {

				final Pair<ISequences, IAnnotatedSolution> s = r.getBestSolution();
				final IAnnotatedSolution second = s.getSecond();
				return second.getGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, Map.class);
			}

			@Override
			public void init(final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGLSOOptimiserTransformerUnit[numCopies];
				for (int i = 0; i < numCopies; ++i) {
					t[i] = new LNGLSOOptimiserTransformerUnit(dt, settings, inputState.getBestSolution().getFirst(), dt.getHints());
				}
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
				return t[0].getInputState();
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
	private final LocalSearchOptimiser optimiser;

	@NonNull
	private final IMultiStateResult inputState;

	// @SuppressWarnings("null")
	public LNGLSOOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final OptimiserSettings settings, @NonNull final ISequences inputSequences,
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

			optimiser = injector.getInstance(LocalSearchOptimiser.class);
			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();
			final IAnnotatedSolution startSolution = optimiser.start(injector.getInstance(IOptimisationContext.class), injector.getInstance(Key.get(ISequences.class, Names.named("Initial"))),
					inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
			inputState = new MultiStateResult(inputSequences, startSolution);
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

	@SuppressWarnings("null")
	@NonNull
	public IOptimisationContext getOptimisationContext() {
		return injector.getInstance(IOptimisationContext.class);
	}

	@Override
	public IMultiStateResult run(final IProgressMonitor monitor) {
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			monitor.beginTask("", 100);
			try {

				// Main Optimisation Loop
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

				if (bestRawSequences != null && bestSolution != null) {
					return new MultiStateResult(bestRawSequences, bestSolution);
				} else {
					throw new RuntimeException("Unable to optimise");
				}
			} finally {
				monitor.done();
			}
		}
	}

	public IMultiStateResult getInputState() {
		return inputState;
	}
}
