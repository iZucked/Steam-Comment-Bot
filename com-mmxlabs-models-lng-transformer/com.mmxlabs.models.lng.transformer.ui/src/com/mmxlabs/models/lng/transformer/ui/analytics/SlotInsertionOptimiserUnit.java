/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.ContainerProvider;
import com.mmxlabs.models.lng.transformer.ui.LNGExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiser;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class SlotInsertionOptimiserUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private final Map<Thread, SlotInsertionOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private IVesselAvailability nominalMarketAvailability;

	@SuppressWarnings("null")
	public SlotInsertionOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.executorService = executorService;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constainAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				install(new MoveGeneratorModule(true));
			}

			@Provides
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE) final boolean isReevaluating) {
				final EvaluationHelper helper = new EvaluationHelper(isReevaluating);
				injector.injectMembers(helper);
				return helper;
			}

			@Provides
			private SlotInsertionOptimiser providePerThreadOptimiser(@NonNull final Injector injector) {

				SlotInsertionOptimiser optimiser = threadCache.get(Thread.currentThread());
				if (optimiser == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					optimiser = new SlotInsertionOptimiser();
					injector.injectMembers(optimiser);
					threadCache.put(Thread.currentThread(), optimiser);
				}
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public IMultiStateResult run(final @NonNull List<Slot> slotsToInsert, final int tries, @NonNull final IProgressMonitor monitor) {
		// try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
		// scope.enter();
		try {

			@NonNull
			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final List<IPortSlot> elements = slotsToInsert.stream() //
					.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
					.collect(Collectors.toList());

			monitor.beginTask("Generate solutions", tries);

			final List<Future<Pair<ISequences, Long>>> futures = new LinkedList<>();
			try {
				for (int tryNo = 0; tryNo < tries; ++tryNo) {
					final int pTryNo = tryNo;

					futures.add(executorService.submit(() -> {
						if (monitor.isCanceled()) {
							return null;
						}
						try {
							// Bit nasty, but we are still in PoC stages

							final SlotInsertionOptimiser calculator = injector.getInstance(SlotInsertionOptimiser.class);
							return calculator.generate(elements, pTryNo);
						} finally {
							monitor.worked(1);
						}
					}));
				}
				final List<Pair<ISequences, Long>> results = new LinkedList<>();

				// Block until all futures completed
				for (final Future<Pair<ISequences, Long>> f : futures) {
					if (monitor.isCanceled()) {
						return null;
					}
					try {
						final Pair<ISequences, Long> s = f.get();
						if (s != null) {
							results.add(s);
						}
					} catch (final InterruptedException e) {
						e.printStackTrace();
					} catch (final ExecutionException e) {
						e.printStackTrace();
					}
				}
				if (monitor.isCanceled()) {
					return null;
				}

				Collections.sort(results, (a, b) -> {
					final long al = a.getSecond();
					final long bl = b.getSecond();
					if (al > bl) {
						return -1;
					} else if (al < bl) {
						return 1;
					} else {
						return 0;
					}
				});

				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.stream() //
						.distinct() //
						.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getFirst(), new HashMap<>())) //
						.collect(Collectors.toList());

				solutions.add(0, new NonNullPair<ISequences, Map<String, Object>>(inputState.getBestSolution().getFirst(), new HashMap<>()));

				return new MultiStateResult(inputState.getBestSolution(), solutions);
			} finally {
				monitor.done();
			}

		} finally {
			final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);

			// Clean up thread-locals created in the scope object
			for (final Thread thread : threadCache.keySet()) {
				scope.exit(thread);
			}
			threadCache.clear();
		}
		// }
	}

	public static IChainLink export(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, @NonNull final ContainerProvider containerProvider) {
		return LNGExporterUnit.exportMultiple(chainBuilder, progressTicks, runner, containerProvider, "Saving insertion plans", parent -> {

			final List<Container> elementsToRemove = new LinkedList<>();
			for (final Container c : parent.getElements()) {
				if (c.getName().startsWith("InsertionPlan-")) {
					elementsToRemove.add(c);
				}
			}
			for (final Container c : elementsToRemove) {
				parent.getScenarioService().delete(c);
			}
		}, changeSetIdx -> {
			String newName;
			if (changeSetIdx == 0) {
				newName = "InsertionPlan-base";
				changeSetIdx++;
			} else {
				newName = String.format("InsertionPlan-%s", (changeSetIdx++));
			}
			return newName;
		});
	}
}
