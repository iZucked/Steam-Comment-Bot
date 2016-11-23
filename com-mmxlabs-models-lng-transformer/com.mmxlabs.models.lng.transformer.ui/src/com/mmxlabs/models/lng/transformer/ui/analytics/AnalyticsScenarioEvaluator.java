package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class AnalyticsScenarioEvaluator implements IAnalyticsScenarioEvaluator {
	@Override
	public void evaluate(@org.eclipse.jdt.annotation.NonNull final LNGScenarioModel lngScenarioModel, @org.eclipse.jdt.annotation.NonNull final UserSettings userSettings,
			@Nullable final ScenarioInstance parentForFork, final boolean fork, final String forkName) {

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// No optimisation going on, clear stages. Need better OptimisationHelper API?
		optimisationPlan.getStages().clear();

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, null, null,
					false);

			scenarioRunner.evaluateInitialState();
			if (parentForFork != null && fork) {
				final IScenarioService scenarioService = parentForFork.getScenarioService();

				final ScenarioInstance dup = scenarioService.insert(parentForFork, EcoreUtil.copy(lngScenarioModel));
				dup.setName(forkName);

				// Copy across various bits of information
				dup.getMetadata().setContentType(parentForFork.getMetadata().getContentType());
				dup.getMetadata().setCreated(parentForFork.getMetadata().getCreated());
				dup.getMetadata().setLastModified(new Date());

				// Copy version context information
				dup.setVersionContext(parentForFork.getVersionContext());
				dup.setScenarioVersion(parentForFork.getScenarioVersion());

				dup.setClientVersionContext(parentForFork.getClientVersionContext());
				dup.setClientScenarioVersion(parentForFork.getClientScenarioVersion());
			}

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

	@Override
	public void breakEvenEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss,
			BreakEvenMode breakEvenMode) {
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			optimisationPlan.getStages().clear();
			BreakEvenOptimisationStage breakEvenOptimisationStage = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
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
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, null, null,
					false, hints);

			scenarioRunner.evaluateInitialState();
			scenarioRunner.run();
			if (parentForFork != null) {
				final IScenarioService scenarioService = parentForFork.getScenarioService();

				final ScenarioInstance dup = scenarioService.insert(parentForFork, EcoreUtil.copy(lngScenarioModel));
				dup.setName("What if");

				// Copy across various bits of information
				dup.getMetadata().setContentType(parentForFork.getMetadata().getContentType());
				dup.getMetadata().setCreated(parentForFork.getMetadata().getCreated());
				dup.getMetadata().setLastModified(new Date());

				// Copy version context information
				dup.setVersionContext(parentForFork.getVersionContext());
				dup.setScenarioVersion(parentForFork.getScenarioVersion());

				dup.setClientVersionContext(parentForFork.getClientVersionContext());
				dup.setClientScenarioVersion(parentForFork.getClientScenarioVersion());
			}

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

	@Override
	public void multiEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss,
			BreakEvenMode breakEvenMode, List<BaseCase> baseCases, IMapperClass mapper, Map<ShippingOption, VesselAssignmentType> shippingMap, BiConsumer<BaseCase, Schedule> resultHandler) {
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			optimisationPlan.getStages().clear();
			BreakEvenOptimisationStage breakEvenOptimisationStage = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
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
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, null, null,
					false, hints);

			@NonNull
			LNGScenarioToOptimiserBridge bridge = scenarioRunner.getScenarioToOptimiserBridge();

			ModelEntityMap modelEntityMap = bridge.getInjector().getInstance(ModelEntityMap.class);

			List<IPortSlot> unused = new LinkedList<>();
			for (BaseCase baseCase : baseCases) {

				final Map<VesselAssignmentType, List<Slot>> m_ToGenerate = new LinkedHashMap<>();
				final Map<IResource, IModifiableSequence> m_preCreated = new LinkedHashMap<>();
				for (BaseCaseRow row : baseCase.getBaseCase()) {
					LoadSlot load = mapper.get(row.getBuyOption());
					DischargeSlot discharge = mapper.get(row.getSellOption());
					ShippingOption shipping = row.getShipping();

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
						Pair<IResource, IModifiableSequence> p = SequenceHelper.createFOBDESSequence(bridge, load, discharge);
						m_preCreated.put(p.getFirst(), p.getSecond());

						continue;
					}
					ShippingOption copyOfShipping = mapper.getCopy(shipping);
					VesselAssignmentType key = shippingMap.get(copyOfShipping);
					List<Slot> slots = m_ToGenerate.computeIfAbsent(key, k -> new LinkedList<>());
					slots.add(load);
					slots.add(discharge);
				}

				for (Map.Entry<VesselAssignmentType, List<Slot>> e : m_ToGenerate.entrySet()) {
					VesselAssignmentType t = e.getKey();
					if (t instanceof VesselAvailability) {
						VesselAvailability vesselAvailability = (VesselAvailability) t;
						Pair<IResource, IModifiableSequence> p = SequenceHelper.makeSequence(bridge, vesselAvailability, e.getValue());
						ISequence old = m_preCreated.put(p.getFirst(), p.getSecond());
						assert old == null;

					} else if (t instanceof CharterInMarket) {
						CharterInMarket charterInMarket = (CharterInMarket) t;
						Pair<IResource, IModifiableSequence> p = SequenceHelper.makeSequence(bridge, charterInMarket, -1, e.getValue());
						ISequence old = m_preCreated.put(p.getFirst(), p.getSecond());
						assert old == null;
					}
				}
				ModifiableSequences rawSequences = new ModifiableSequences(new ArrayList<>(m_preCreated.keySet()), m_preCreated);
				{
					LNGDataTransformer optimiserDataTransformer = bridge.getDataTransformer();
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

					final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
					{
						final Injector childInjector = evaluationInjector.createChildInjector(new ExporterExtensionsModule());
						childInjector.injectMembers(exporter);
					}
					try (PerChainUnitScopeImpl scope = evaluationInjector.getInstance(PerChainUnitScopeImpl.class)) {
						scope.enter();

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
