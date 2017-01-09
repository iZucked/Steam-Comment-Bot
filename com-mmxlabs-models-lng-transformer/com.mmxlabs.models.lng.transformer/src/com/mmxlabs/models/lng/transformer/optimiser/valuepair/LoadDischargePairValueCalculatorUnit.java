/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class LoadDischargePairValueCalculatorUnit {

//	@NonNull
//	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull final LoadDischargePairStage stageSettings,
//			@Nullable final ExecutorService executorService, final int progressTicks) {
//		final IChainLink link = new IChainLink() {
//
//			@Override
//			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
//				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
//
//				@NonNull
//				final Collection<@NonNull String> hints = new HashSet<>(dt.getHints());
//				if (userSettings.isGenerateCharterOuts()) {
//					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
//				} else {
//					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
//				}
//				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);
//
//				final LoadDischargePairValueCalculatorUnit t = new LoadDischargePairValueCalculatorUnit(dt, phase, userSettings, stageSettings, executorService, initialSequences.getSequences(),
//						inputState, hints);
//				t.run(monitor, new ProfitAndLossExtractor((loadOption, dischargeOption, value) -> {
//					// Record the output
//				}));
//				return inputState;
//			}
//
//			@Override
//			public int getProgressTicks() {
//				return progressTicks;
//			}
//		};
//		chainBuilder.addLink(link);
//		return link;
//	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private final Map<Thread, LoadDischargePairValueCalculator> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private IVesselAvailability nominalMarketAvailability;

	@SuppressWarnings("null")
	public LoadDischargePairValueCalculatorUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
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

			}

			@Provides
			private LoadDischargePairValueCalculator providePerThreadBagMover(@NonNull final Injector injector) {

				LoadDischargePairValueCalculator bagMover = threadCache.get(Thread.currentThread());
				if (bagMover == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					bagMover = new LoadDischargePairValueCalculator();
					injector.injectMembers(bagMover);
					threadCache.put(Thread.currentThread(), bagMover);
					// System.out.println("thread:" + Thread.currentThread().getId());
				}
				return bagMover;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public void run(final @NonNull CharterInMarket nominalMarket, @NonNull final IProgressMonitor monitor, final ProfitAndLossExtractor recorder) {
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			try {
				final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);
				final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
				final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

				final List<ILoadOption> loads = LoadDischargePairValueCalculator.findPurchases(optimisationData, portSlotProvider);
				final List<IDischargeOption> discharges = LoadDischargePairValueCalculator.findSales(optimisationData, portSlotProvider);

				// final CharterInMarket nominalMarket = stageSettings.getCharterInMarket();
				final ISpotCharterInMarket o_nominalMarket = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(nominalMarket, ISpotCharterInMarket.class);
				final Pair<CharterInMarket, Integer> key = new Pair<>(nominalMarket, -1);
				for (final IResource resource : optimisationData.getResources()) {
					final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
					if (vesselAvailability.getSpotCharterInMarket() == o_nominalMarket && vesselAvailability.getSpotIndex() == -1) {
						nominalMarketAvailability = vesselAvailability;
						break;
					}
				}
				assert nominalMarketAvailability != null;

				final int vesselCount = 1;
				final int totalWork = loads.size() * discharges.size() * vesselCount;
				monitor.beginTask("Generate cost pairs", totalWork);
				try {
					final List<Future<?>> futures = new LinkedList<>();
					for (final ILoadOption loadOption : loads) {
						for (final IDischargeOption dischargeOption : discharges) {
							futures.add(executorService.submit(() -> {
								try {
									final LoadDischargePairValueCalculator calculator = injector.getInstance(LoadDischargePairValueCalculator.class);
									calculator.generate(loadOption, dischargeOption, nominalMarketAvailability, recorder);
								} finally {
									monitor.worked(1);
								}
							}));
						}
					}
					executorService.shutdown();
					// Block until all futures completed
					for (final Future<?> f : futures) {
						try {
							f.get();
						} catch (final InterruptedException e) {
							e.printStackTrace();
						} catch (final ExecutionException e) {
							e.printStackTrace();
						}
					}
				} finally {
					monitor.done();
				}

			} finally {
				// Clean up thread-locals created in the scope object
				for (final Thread thread : threadCache.keySet()) {
					scope.exit(thread);
				}
				threadCache.clear();
			}
		}
	}
}
