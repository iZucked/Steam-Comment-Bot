/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.util.scheduling.WrappedAssignableElement;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.BreakEvenOptimiser;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class AnalyticsScenarioEvaluator implements IAnalyticsScenarioEvaluator {
	@Override
	public void evaluate(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final UserSettings userSettings, @Nullable final Container parentForFork, final boolean fork,
			final String forkName) {

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, (LNGScenarioModel) scenarioDataProvider.getScenario());
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// No optimisation going on, clear stages. Need better OptimisationHelper API?
		optimisationPlan.getStages().clear();

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, scenarioDataProvider, null, optimisationPlan, scenarioDataProvider.getEditingDomain(), null, null, null,
					false);

			scenarioRunner.evaluateInitialState();
			if (parentForFork != null && fork) {
				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(parentForFork);
				scenarioService.copyInto(parentForFork, scenarioDataProvider, forkName);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

	@Override
	public void breakEvenEvaluate(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final UserSettings userSettings, @Nullable final Container parentForFork,
			final long targetProfitAndLoss, final BreakEvenMode breakEvenMode) {
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, (LNGScenarioModel) scenarioDataProvider.getScenario());
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			optimisationPlan.getStages().clear();
			final BreakEvenOptimisationStage breakEvenOptimisationStage = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
			breakEvenOptimisationStage.setTargetProfitAndLoss(targetProfitAndLoss);
			optimisationPlan.getStages().add(breakEvenOptimisationStage);
		}
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			String[] hints;
			if (breakEvenMode == BreakEvenMode.POINT_TO_POINT) {
				hints = new String[] {};
			} else {
				hints = new String[] { LNGTransformerHelper.HINT_DISABLE_CACHES };
			}
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, scenarioDataProvider, null, optimisationPlan, scenarioDataProvider.getEditingDomain(), null, null, null,
					false, hints);

			scenarioRunner.evaluateInitialState();
			scenarioRunner.runAndApplyBest();

			if (parentForFork != null) {
				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(parentForFork);
				scenarioService.copyInto(parentForFork, scenarioDataProvider, "What if");
			}

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

	@Override
	public void multiEvaluate(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final UserSettings userSettings, @Nullable final Container parentForFork,
			final long targetProfitAndLoss, final BreakEvenMode breakEvenMode, final List<BaseCase> baseCases, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap,
			final BiConsumer<BaseCase, Schedule> resultHandler) {
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, (LNGScenarioModel) scenarioDataProvider.getScenario());
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			optimisationPlan.getStages().clear();
			final BreakEvenOptimisationStage breakEvenOptimisationStage = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
			breakEvenOptimisationStage.setTargetProfitAndLoss(targetProfitAndLoss);
			optimisationPlan.getStages().add(breakEvenOptimisationStage);
		}
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			String[] hints;
			if (breakEvenMode == BreakEvenMode.POINT_TO_POINT) {
				hints = new String[] {};
			} else {
				hints = new String[] { LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN, LNGTransformerHelper.HINT_DISABLE_CACHES };
			}
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, scenarioDataProvider, null, optimisationPlan, scenarioDataProvider.getEditingDomain(), null, null, null,
					false, hints);

			@NonNull
			final LNGScenarioToOptimiserBridge bridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ModelEntityMap modelEntityMap = bridge.getInjector().getInstance(ModelEntityMap.class);
			final PortModel portModel = ScenarioModelUtil.getPortModel(bridge.getScenarioDataProvider());
			final ModelDistanceProvider modelDistanceProvider = ScenarioModelUtil.getModelDistanceProvider(bridge.getScenarioDataProvider());

			final List<IPortSlot> unused = new LinkedList<>();
			for (final BaseCase baseCase : baseCases) {

				final Map<VesselAssignmentType, List<WrappedAssignableElement>> m_ToGenerate = new LinkedHashMap<>();
				final Map<WrappedAssignableElement, List<Slot>> assignableMap = new LinkedHashMap<>();
				final Map<IResource, IModifiableSequence> m_preCreated = new LinkedHashMap<>();

				for (final BaseCaseRow row : baseCase.getBaseCase()) {
					final LoadSlot load = mapper.getOriginal(row.getBuyOption());
					final DischargeSlot discharge = mapper.getOriginal(row.getSellOption());
					final ShippingOption shipping = row.getShipping();

					if (row.getBuyOption() == null || row.getSellOption() == null) {

						if (row.getBuyOption() != null) {
							unused.add(modelEntityMap.getOptimiserObjectNullChecked(load, IPortSlot.class));
						}
						if (row.getSellOption() != null) {
							unused.add(modelEntityMap.getOptimiserObjectNullChecked(discharge, IPortSlot.class));
						}
						continue;
					}

					if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
						final Pair<IResource, IModifiableSequence> p = SequenceHelper.createFOBDESSequence(bridge.getDataTransformer(), load, discharge);
						m_preCreated.put(p.getFirst(), p.getSecond());

						continue;
					}
					final ShippingOption copyOfShipping = mapper.getCopy(shipping);
					final VesselAssignmentType key = shippingMap.get(copyOfShipping);

					Vessel vessel = null;
					if (key instanceof VesselAvailability) {
						final VesselAvailability vesselAvailability = (VesselAvailability) key;
						vessel = vesselAvailability.getVessel();
					} else if (key instanceof CharterInMarket) {
						final CharterInMarket charterInMarket = (CharterInMarket) key;
						vessel = charterInMarket.getVessel();
					}

					final List<WrappedAssignableElement> sortableElements = m_ToGenerate.computeIfAbsent(key, k -> new LinkedList<>());
					final List<Slot> sortedSlots = Lists.newArrayList(load, discharge);

					final WrappedAssignableElement e = new WrappedAssignableElement(sortedSlots, vessel, portModel, modelDistanceProvider, null);
					sortableElements.add(e);

					assignableMap.put(e, sortedSlots);
				}

				for (final Map.Entry<VesselAssignmentType, List<WrappedAssignableElement>> e : m_ToGenerate.entrySet()) {
					final List<Slot> orderedSlots = new LinkedList<>();
					CollectedAssignment.sortWrappedAssignableElements(e.getValue());
					for (final WrappedAssignableElement w : e.getValue()) {
						orderedSlots.addAll(assignableMap.get(w));
					}

					final VesselAssignmentType t = e.getKey();
					if (t instanceof VesselAvailability) {
						final VesselAvailability vesselAvailability = (VesselAvailability) t;
						final Pair<IResource, IModifiableSequence> p = SequenceHelper.makeSequence(bridge.getDataTransformer(), vesselAvailability, orderedSlots);
						final ISequence old = m_preCreated.put(p.getFirst(), p.getSecond());
						assert old == null;

					} else if (t instanceof CharterInMarket) {
						final CharterInMarket charterInMarket = (CharterInMarket) t;
						final Pair<IResource, IModifiableSequence> p = SequenceHelper.makeSequence(bridge.getDataTransformer(), charterInMarket, -1, orderedSlots);
						final ISequence old = m_preCreated.put(p.getFirst(), p.getSecond());
						assert old == null;
					}
				}
				final ModifiableSequences rawSequences = new ModifiableSequences(new ArrayList<>(m_preCreated.keySet()), m_preCreated);
				{
					final LNGDataTransformer optimiserDataTransformer = bridge.getDataTransformer();
					final Injector evaluationInjector;
					{
						final Collection<@NonNull IOptimiserInjectorService> services = optimiserDataTransformer.getModuleServices();
						final List<Module> modules = new LinkedList<>();
						modules.add(new InitialSequencesModule(optimiserDataTransformer.getInitialSequences()));
						modules.add(new InputSequencesModule(rawSequences));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
								new LNGParameters_EvaluationSettingsModule(optimiserDataTransformer.getUserSettings(),
										optimiserDataTransformer.getSolutionBuilderSettings().getConstraintAndFitnessSettings()),
								services, IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, optimiserDataTransformer.getHints()));
						modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(optimiserDataTransformer.getHints()), services,
								IOptimiserInjectorService.ModuleType.Module_Evaluation, optimiserDataTransformer.getHints()));
						evaluationInjector = optimiserDataTransformer.getInjector().createChildInjector(modules);
					}

					try (PerChainUnitScopeImpl scope = evaluationInjector.getInstance(PerChainUnitScopeImpl.class)) {
						scope.enter();

						if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
							final BreakEvenOptimiser instance = evaluationInjector.getInstance(BreakEvenOptimiser.class);
							instance.optimise(rawSequences, OptimiserUnitConvertor.convertToInternalFixedCost(targetProfitAndLoss));
						}
						final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
						{
							final Injector childInjector = evaluationInjector.createChildInjector(new ExporterExtensionsModule());
							childInjector.injectMembers(exporter);
						}

						final IOptimisationData optimisationData = evaluationInjector.getInstance(IOptimisationData.class);
						final IAnnotatedSolution solution = LNGSchedulerJobUtils.evaluateCurrentState(evaluationInjector, optimisationData, rawSequences).getFirst();

						final Schedule schedule = exporter.exportAnnotatedSolution(modelEntityMap, solution);
						resultHandler.accept(baseCase, schedule);
					}
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

}
