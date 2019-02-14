/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class LoadDischargePairValueCalculatorStep {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	private final Map<Thread, LoadDischargePairValueCalculator> threadCache = new ConcurrentHashMap<>(100);

	@SuppressWarnings("null")
	public LoadDischargePairValueCalculatorStep(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings, @NonNull final ISequences initialSequences, @NonNull final IMultiStateResult inputState,
			@NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.add(new PhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constraintAndFitnessSettings), services,
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
				}
				return bagMover;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);
	}

	public void run(final @NonNull IVesselAvailability nominalMarketAvailability, //
			final ProfitAndLossExtractor recorder, //
			@NonNull final CleanableExecutorService executorService, //
			@NonNull final IProgressMonitor monitor) {
		final IPhaseOptimisationData optimisationData = injector.getInstance(IPhaseOptimisationData.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final List<ILoadOption> loads = LoadDischargePairValueCalculator.findPurchases(optimisationData, portSlotProvider);
		final List<IDischargeOption> discharges = LoadDischargePairValueCalculator.findSales(optimisationData, portSlotProvider);
		final List<IVesselAvailability> vessels = LoadDischargePairValueCalculator.findVessels(optimisationData, vesselProvider);
		
		run(nominalMarketAvailability, loads, discharges, recorder, executorService, monitor, vessels);
	}

	public void run(final @NonNull IVesselAvailability nominalMarketAvailability, //
			final List<ILoadOption> loads, final List<IDischargeOption> discharges, //
			final ProfitAndLossExtractor recorder, //
			@NonNull final CleanableExecutorService executorService, //
			@NonNull final IProgressMonitor monitor,
			final List<IVesselAvailability> vessels) {
		
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			try {
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
									calculator.generate(loadOption, dischargeOption, nominalMarketAvailability, recorder, vessels);
								} finally {
									monitor.worked(1);
								}
							}));
						}
					}
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
				executorService.clean();

				// Clean up thread-locals created in the scope object
				for (final Thread thread : threadCache.keySet()) {
					scope.exit(thread);
				}
				threadCache.clear();
			}
		}
	}
}
