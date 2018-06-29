/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.SequencesToPortSlotsUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.transformerunits.TransformerUnitsHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class LightWeightSchedulerOptimiserUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LightWeightSchedulerOptimiserUnit.class);

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	private final Map<Thread, LightweightSchedulerOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private LNGScenarioModel initialScenario;

	private @NonNull ISequences initialSequences;

	@SuppressWarnings("null")
	public LightWeightSchedulerOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			LNGScenarioModel initialScenario, @NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.initialSequences = initialSequences;
		this.initialScenario = initialScenario;
		
		CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.add(new PhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constainAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new LightWeightSchedulerModule(threadCache, charterInMarket, dataTransformer));

		injector = dataTransformer.getInjector().createChildInjector(modules);
	}

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final LNGScenarioToOptimiserBridge optimiserBridge, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final CleanStateOptimisationStage stageSettings, final int progressTicks, @NonNull final ExecutorService executorService, final int seed) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dataTransformer = chainBuilder.getDataTransformer();

				final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
				if (runnerHook != null) {
					runnerHook.beginStage(stage);

					final ISequences preloadedResult = runnerHook.getPrestoredSequences(stage, dataTransformer);
					if (preloadedResult != null) {
						monitor.beginTask("", 1);
						try {
							monitor.worked(1);
							return new MultiStateResult(preloadedResult, new HashMap<>());
						} finally {
							runnerHook.endStage(stage);
							monitor.done();
						}
					}
				}

				@NonNull
				final Collection<@NonNull String> hints = new HashSet<>(dataTransformer.getHints());
				if (userSettings.isGenerateCharterOuts()) {
					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				} else {
					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				}
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				monitor.beginTask("", 100);
				try {
					final CleanStateOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
					copyStageSettings.setSeed(seed);
					final LightWeightSchedulerOptimiserUnit t  = new LightWeightSchedulerOptimiserUnit(dataTransformer, userSettings, 
							copyStageSettings.getConstraintAndFitnessSettings(), executorService, initialSequences.getSequences(),
							(LNGScenarioModel) (optimiserBridge.getOptimiserScenario().getScenario()), inputState, hints);
					
					IMultiStateResult result = t.run(new SubProgressMonitor(monitor, 100));
					
					// trim out excess slots (i.e. if we created a load that we let the MIP choose)
					result = t.modifyResult(result);
					
					// change providers
					t.injector
						.getInstance(ILightWeightPostOptimisationStateModifier.class)
						.modifyState(result.getBestSolution().getFirst());
					
					// Check monitor state
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}

					if (result == null) {
						throw new IllegalStateException("No results generated");
					}
					
					

					if (runnerHook != null) {
						runnerHook.reportSequences(stage, result.getBestSolution().getFirst(), dataTransformer);
					}

					return result;
				} finally {
					if (runnerHook != null) {
						runnerHook.endStage(stage);
					}

					monitor.done();
				}
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
			try {
				@NonNull
				ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
				ILongTermSlotsProviderEditor longTermSlotsProviderEditor = injector.getInstance(ILongTermSlotsProviderEditor.class);
				IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
				Collection<IPortSlot> allPortSlots = SequencesToPortSlotsUtils.getAllPortSlots(initialSequences, portSlotProvider);
				LightWeightOptimiserHelper.addTargetSlotsToProvider(longTermSlotsProviderEditor, allPortSlots);
				LightWeightOptimiserHelper.addTargetEventsToProvider(longTermSlotsProviderEditor, allPortSlots);
				monitor.beginTask("Generate solutions", 100);
				CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);

				final LightweightSchedulerOptimiser calculator = injector.getInstance(LightweightSchedulerOptimiser.class);
				Pair<ISequences, Long> result = calculator.optimise(dataTransformer, charterInMarket);

					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = new LinkedList<>();
					solutions.add(new NonNullPair<ISequences, Map<String, Object>>(result.getFirst(), new HashMap<>()));

					return new MultiStateResult(solutions.get(0), solutions);
				} finally {
					monitor.done();
				}
	}

	private IMultiStateResult modifyResult(IMultiStateResult result) {
		ISequenceElementFilter filter = injector.getInstance(ISequenceElementFilter.class);
		return TransformerUnitsHelper.removeExcessSlots(result, filter);
	}

}
