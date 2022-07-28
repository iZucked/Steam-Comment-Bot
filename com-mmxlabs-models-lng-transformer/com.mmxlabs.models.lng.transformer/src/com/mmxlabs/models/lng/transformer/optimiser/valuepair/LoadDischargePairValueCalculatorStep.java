/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.valuepair;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class LoadDischargePairValueCalculatorStep {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

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
			@ThreadLocalScope
			private LoadDischargePairValueCalculator providePerThreadBagMover(@NonNull final Injector injector) {
				LoadDischargePairValueCalculator bagMover = new LoadDischargePairValueCalculator();
				injector.injectMembers(bagMover);
				return bagMover;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);
	}

	public void run(final @NonNull IVesselCharter nominalMarketVesselCharter, //
			final ProfitAndLossExtractor recorder, //
			@NonNull final JobExecutorFactory jobExecutorFactory, //
			@NonNull final IProgressMonitor monitor) {
		final IPhaseOptimisationData optimisationData = injector.getInstance(IPhaseOptimisationData.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final List<ILoadOption> loads = LoadDischargePairValueCalculator.findPurchases(optimisationData, portSlotProvider);
		final List<IDischargeOption> discharges = LoadDischargePairValueCalculator.findSales(optimisationData, portSlotProvider);
		final List<IVesselCharter> vessels = LoadDischargePairValueCalculator.findVessels(optimisationData, vesselProvider);

		run(nominalMarketVesselCharter, loads, discharges, recorder, jobExecutorFactory, monitor, vessels);
	}

	public void run(final @NonNull IVesselCharter nominalMarketVesselCharter, //
			final List<ILoadOption> loads, final List<IDischargeOption> discharges, //
			final ProfitAndLossExtractor recorder, //
			@NonNull final JobExecutorFactory jobExecutorFactory, //
			@NonNull final IProgressMonitor monitor, final List<IVesselCharter> vessels) {

		final JobExecutorFactory subJobExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class);
			scope.enter();
			return scope;
		});

		try (JobExecutor jobExecutor = subJobExecutorFactory.begin()) {

			final int vesselCount = 1;
			final int totalWork = loads.size() * discharges.size() * vesselCount;
			monitor.beginTask("Generate cost pairs", totalWork);
			try {
				final List<Future<?>> futures = new LinkedList<>();
				for (final ILoadOption loadOption : loads) {
					for (final IDischargeOption dischargeOption : discharges) {
						futures.add(jobExecutor.submit(() -> {
							try {
								final LoadDischargePairValueCalculator calculator = injector.getInstance(LoadDischargePairValueCalculator.class);
								calculator.generate(loadOption, dischargeOption, nominalMarketVesselCharter, recorder, vessels);
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
					} catch (final InterruptedException  | ExecutionException e) {
						e.printStackTrace();
					}
				}
			} finally {
				monitor.done();
			}
		}
	}
}
