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
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.concurrent.SimpleCleanableExecutorService;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.models.lng.transformer.optimiser.common.SlotValueHelper;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.SequencesToPortSlotsUtils;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.transformerunits.TransformerUnitsHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class LightWeightSchedulerOptimiserUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LightWeightSchedulerOptimiserUnit.class);

	@NonNull
	private final LNGDataTransformer dataTransformer;

	private final Map<Thread, LightweightSchedulerOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings;

	private @NonNull final UserSettings userSettings;

	private @NonNull final Collection<String> hints;

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final LNGScenarioToOptimiserBridge optimiserBridge, @NonNull final String stage,
			@NonNull final UserSettings userSettings, @NonNull final CleanStateOptimisationStage stageSettings, final int progressTicks, @NonNull final ExecutorService executorService,
			final int seed) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequencesContainer, final IMultiStateResult inputState, final IProgressMonitor monitor) {
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

				try {
					final CleanStateOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
					copyStageSettings.setSeed(seed);

					final LightWeightSchedulerOptimiserUnit t = new LightWeightSchedulerOptimiserUnit(dataTransformer, userSettings, copyStageSettings.getConstraintAndFitnessSettings(),
							executorService, (LNGScenarioModel) (optimiserBridge.getOptimiserScenario().getScenario()), hints);

					final IMultiStateResult result = t.runAll(initialSequencesContainer.getSequences(), monitor);

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

	@SuppressWarnings("null")
	public LightWeightSchedulerOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings, @NonNull final ExecutorService executorService, final LNGScenarioModel initialScenario,
			@NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.userSettings = userSettings;
		this.constraintAndFitnessSettings = constraintAndFitnessSettings;
		this.hints = hints;
	}

	public IMultiStateResult runAll(@NonNull final ISequences referenceSequences, @NonNull final IProgressMonitor monitor) {

		// Convert the given monitor into a progress instance
		final SubMonitor progress = SubMonitor.convert(monitor, 100);

		final Injector stage1Injector = buildStage1Injector(referenceSequences);

		prepareLongTermData(stage1Injector, referenceSequences);
		progress.worked(1);

		final IVesselAvailability pnlVessel = stage1Injector.getInstance(Key.get(IVesselAvailability.class, Names.named(OptimiserConstants.DEFAULT_VESSEL)));

		final ILightWeightOptimisationData lwOptimsdationData = computeStage1Data(stage1Injector, pnlVessel, progress.split(29));

		if (lwOptimsdationData == null) {
			throw new RuntimeException();
		}
		final Injector stage2Injector = buildStage2Injector(stage1Injector, lwOptimsdationData);
		IMultiStateResult result = runStage2(pnlVessel, stage2Injector, progress.split(70));

		// trim out excess slots (i.e. if we created a load that we let the MIP choose)
		result = modifyResult(stage2Injector, result);

		// change providers
		final ILightWeightPostOptimisationStateModifier stateModifier = stage2Injector.getInstance(ILightWeightPostOptimisationStateModifier.class);
		stateModifier.modifyState(result.getBestSolution().getFirst());

		return result;
	}
	
	public boolean[][] runAndGetPairings(@NonNull final ISequences referenceSequences, @NonNull final IProgressMonitor monitor) {

		// Convert the given monitor into a progress instance
		final SubMonitor progress = SubMonitor.convert(monitor, 100);

		final Injector stage1Injector = buildStage1Injector(referenceSequences);

		prepareLongTermData(stage1Injector, referenceSequences);
		progress.worked(1);

		final IVesselAvailability pnlVessel = stage1Injector.getInstance(Key.get(IVesselAvailability.class, Names.named(OptimiserConstants.DEFAULT_VESSEL)));

		final LoadDischargePairValueCalculatorStep calculator = SlotValueHelper.createLoadDischargeCalculatorUnit(dataTransformer);

		final LightWeightScheduler scheduler = stage1Injector.getInstance(LightWeightScheduler.class);

		final CleanableExecutorService executorService = new SimpleCleanableExecutorService(Executors.newFixedThreadPool(1));

		try {
			return scheduler.getSlotPairingMatrix(pnlVessel, calculator, executorService, monitor);
		} catch (Exception e) {
			return null;
		}
		finally {
			executorService.shutdown();
		}
	}


	@NonNullByDefault
	private Injector buildStage2Injector(final Injector stage1Injector, final ILightWeightOptimisationData lwOptimsdationData) {

		return stage1Injector.createChildInjector(new LightWeightSchedulerStage2Module(threadCache), new AbstractModule() {

			@Override
			protected void configure() {
				bind(ILightWeightOptimisationData.class).toInstance(lwOptimsdationData);
			}
		});
	}

	public Injector buildStage1Injector(@NonNull final ISequences referenceSequences) {
		final @NonNull Collection<@NonNull IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(referenceSequences));
		modules.add(new InputSequencesModule(referenceSequences));
		modules.add(new PhaseOptimisationDataModule());

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constraintAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new LightWeightSchedulerStage1Module());

		final Injector injector = dataTransformer.getInjector().createChildInjector(modules);

		return injector;
	}

	public void prepareLongTermData(final Injector stage1Injector, final ISequences referenceSequences) {
		final ILongTermSlotsProviderEditor longTermSlotsProviderEditor = stage1Injector.getInstance(ILongTermSlotsProviderEditor.class);
		final IPortSlotProvider portSlotProvider = stage1Injector.getInstance(IPortSlotProvider.class);

		final Collection<IPortSlot> allPortSlots = SequencesToPortSlotsUtils.getAllPortSlots(referenceSequences, portSlotProvider);

		LightWeightOptimiserHelper.addTargetSlotsToProvider(longTermSlotsProviderEditor, allPortSlots);
		LightWeightOptimiserHelper.addTargetEventsToProvider(longTermSlotsProviderEditor, allPortSlots);

	}

	public ILightWeightOptimisationData computeStage1Data(final Injector stage1Injector, final IVesselAvailability pnlVessel, final IProgressMonitor monitor) {

		final LoadDischargePairValueCalculatorStep calculator = SlotValueHelper.createLoadDischargeCalculatorUnit(dataTransformer);

		final LightWeightScheduler scheduler = stage1Injector.getInstance(LightWeightScheduler.class);

		final CleanableExecutorService executorService = new SimpleCleanableExecutorService(Executors.newFixedThreadPool(1));

		try {
			return scheduler.calculateLightWeightOptimisationData(pnlVessel, calculator, executorService, monitor);
		} finally {
			executorService.shutdown();
		}

	}

	public @NonNull IMultiStateResult runStage2(final @NonNull IVesselAvailability pnlVessel, @NonNull final Injector stage2Injector, final @NonNull IProgressMonitor monitor) {
		try {

			final LightweightSchedulerOptimiser calculator = stage2Injector.getInstance(LightweightSchedulerOptimiser.class);
			final Pair<ISequences, Long> result = calculator.optimise(pnlVessel, monitor);

			final List<NonNullPair<ISequences, Map<String, Object>>> solutions = new LinkedList<>();
			solutions.add(new NonNullPair<>(result.getFirst(), new HashMap<>()));

			return new MultiStateResult(solutions.get(0), solutions);
		} finally {
			monitor.done();
		}
	}

	@NonNullByDefault
	private IMultiStateResult modifyResult(final Injector stage2Injector, final IMultiStateResult result) {
		final ISequenceElementFilter filter = stage2Injector.getInstance(ISequenceElementFilter.class);
		return TransformerUnitsHelper.removeExcessSlots(result, filter);
	}

}
