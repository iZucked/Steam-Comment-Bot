/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.parser.series.SeriesType;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.PriceCurveKey;

public class LNGSchedulerPriceCurveSetRunner {

	final IScenarioDataProvider originalScenarioDataProvider;
	final EditingDomain originalEditingDomain;
	final UserSettings userSettings;
	final LNGScenarioToOptimiserBridge bridge;
	// final JobExecutorFactory jobExecutorFactory;
	final Injector injector;
	final OptionAnalysisModel model;

	public LNGSchedulerPriceCurveSetRunner(final ScenarioInstance scenarioInstance, final OptionAnalysisModel model, final IScenarioDataProvider sdp, final EditingDomain editingDomain,
			final UserSettings userSettings, final ExtraDataProvider extraDataProvider, @Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider,
			final Collection<@NonNull String> initialHints) {
		this.originalScenarioDataProvider = sdp;
		this.originalEditingDomain = editingDomain;

		final UserSettings settings = EMFCopier.copy(userSettings);
		settings.unsetPeriodStartDate();
		settings.unsetPeriodEnd();

		this.userSettings = settings;

		@NonNull
		final Collection<@NonNull String> hints = new LinkedList<>(initialHints);
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
		hints.add(LNGTransformerHelper.HINT_EVALUATION_ONLY);

		final SolutionBuilderSettings solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();
		solutionBuilderSettings.setConstraintAndFitnessSettings(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
		// Ignore objectives

		final int cores = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(sdp, //
				scenarioInstance, //
				extraDataProvider, //
				this.userSettings, //
				solutionBuilderSettings, //
				editingDomain, //
				cores, //
				null, //
				null, //
				true, //
				hints.toArray(new String[hints.size()]) //
		);
		final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();
		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(bridge.getDataTransformer().getInitialSequences()));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, solutionBuilderSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		injector = bridge.getDataTransformer().getInjector().createChildInjector(modules);

		this.bridge = bridge;
		this.model = model;
	}

	public LNGScenarioToOptimiserBridge getBridge() {
		return this.bridge;
	}

	public IMultiStateResult run() {
		JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService(LNGScenarioChainBuilder.getNumberOfAvailableCores());
		final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});

		try (JobExecutor jobExecutor = subExecutorFactory.begin()) {
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final EvaluationHelper evaluationHelper = injector.getInstance(EvaluationHelper.class);
				final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(originalScenarioDataProvider);

				// Doing this to populate the base case curves first - order should not matter
				// so this can probably be removed if wanted
				final Set<CommodityCurve> curvesToOverlay = model.getCommodityCurves().stream() //
						.filter(CommodityCurveOverlay.class::isInstance) //
						.map(CommodityCurveOverlay.class::cast) //
						.filter(overlay -> !overlay.getAlternativeCurves().isEmpty()) //
						.map(CommodityCurveOverlay::getReferenceCurve) //
						.collect(Collectors.toSet());

				final Map<String, List<PriceCurveKey>> curveOptions = curvesToOverlay.stream() //
						.map(curve -> curve.getName().toLowerCase()) //
						.collect(Collectors.toMap(Function.identity(), curveName -> new LinkedList<>(Collections.singleton(new PriceCurveKey(curveName, null, SeriesType.COMMODITY)))));

				model.getCommodityCurves().stream() //
						.filter(CommodityCurveOverlay.class::isInstance) //
						.map(CommodityCurveOverlay.class::cast) //
						.filter(overlay -> !overlay.getAlternativeCurves().isEmpty()) //
						.forEach(overlay -> {
							final String referenceCurveName = overlay.getReferenceCurve().getName().toLowerCase();
							overlay.getAlternativeCurves().stream() //
									.forEach(ympContainer -> {
										final String alternativeName = ympContainer.getName().toLowerCase();
										final PriceCurveKey key = new PriceCurveKey(referenceCurveName, alternativeName, SeriesType.COMMODITY);
										curveOptions.get(referenceCurveName).add(key);
									});
						});
				final List<Future<Triple<List<PriceCurveKey>, Pair<ISequences, Long>, @Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState>>>> futures = new LinkedList<>();
				List<Triple<List<PriceCurveKey>, ISequences, Long>> results = new LinkedList<>();
				List<@Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState>> results2 = new LinkedList<>();

				final List<List<PriceCurveKey>> combinations = Lists.cartesianProduct(new ArrayList<>(curveOptions.values()));
				for (final List<PriceCurveKey> combination : combinations) {
					futures.add(jobExecutor.submit(() -> {
						final ISequences sequences = bridge.getDataTransformer().getInitialSequences();
						@Nullable
						Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> result = evaluationHelper.evalulateSequences(sequences, combination);
						final long pnl = evaluationHelper.calculateSchedulePNL(sequences, result.getFirst());
						final Pair<ISequences, Long> firstPair = Pair.of(sequences, pnl);
						return Triple.of(combination, firstPair, result);
					}));
				}

				for (final Future<Triple<List<PriceCurveKey>, Pair<ISequences, Long>, @Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState>>> f : futures) {
					try {
						final Triple<List<PriceCurveKey>, Pair<ISequences, Long>, @Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState>> s = f.get();
						results.add(Triple.of(s.getFirst(), s.getSecond().getFirst(), s.getSecond().getSecond()));
						results2.add(s.getThird());
					} catch (final InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
				int i = 0;
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = new ArrayList<>();
				for (final Triple<List<PriceCurveKey>, ISequences, Long> p : results) {
					final Map<String, Object> map = new HashMap<>();
					map.put("combination", p.getFirst());
					solutions.add(new NonNullPair<>(p.getSecond(), map));
				}
				return new MultiStateResult(solutions.get(0), solutions);
			}
		}
	}

}
