/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Consumer;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGSchedulerInsertSlotJobRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(LNGSchedulerInsertSlotJobRunner.class);

	private final IScenarioDataProvider originalScenarioDataProvider;

	private final LNGScenarioRunner scenarioRunner;

	private final List<Slot<?>> targetSlots;
	private final List<Slot<?>> targetOptimiserSlots;
	private final List<VesselEvent> targetEvents;
	private final List<VesselEvent> targetOptimiserEvents;

	private final EditingDomain originalEditingDomain;

	private static final String[] hint_with_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			LNGTransformerHelper.HINT_OPTIMISE_INSERTION, //
			LNGTransformerHelper.HINT_DISABLE_CACHES, //
			LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN };

	private static final String[] hint_without_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			LNGTransformerHelper.HINT_OPTIMISE_INSERTION };

	private final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private final LNGDataTransformer dataTransformer;

	private boolean performBreakEven;

	private final UserSettings userSettings;

	private OptimisationPlan plan;

	private InsertionOptimisationStage insertionStage;

	public LNGSchedulerInsertSlotJobRunner(@Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider, final EditingDomain editingDomain,
			final UserSettings userSettings, final List<Slot<?>> targetSlots, final List<VesselEvent> targetEvents, @Nullable ExtraDataProvider extraDataProvider,
			@Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider) {
		this(scenarioInstance, scenarioDataProvider, editingDomain, userSettings, targetSlots, targetEvents, extraDataProvider, initialSolutionProvider, null);
	}

	public LNGSchedulerInsertSlotJobRunner(@Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider, final EditingDomain editingDomain,
			final UserSettings userSettings, final List<Slot<?>> targetSlots, final List<VesselEvent> targetEvents, @Nullable ExtraDataProvider extraDataProvider,
			@Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider, @Nullable Consumer<LNGOptimisationBuilder> builderCustomiser) {

		this.originalScenarioDataProvider = scenarioDataProvider;
		this.originalEditingDomain = editingDomain;
		this.userSettings = EcoreUtil.copy(userSettings);
		this.targetSlots = targetSlots;
		this.targetEvents = targetEvents;

		// Reset settings not supplied to the user
		this.userSettings.setShippingOnly(false);
		this.userSettings.setCleanSlateOptimisation(false);
		this.userSettings.setSimilarityMode(SimilarityMode.OFF);

		plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
		plan.setUserSettings(this.userSettings);
		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());
		plan.getStages().add(ScenarioUtils.createDefaultInsertionSettings());

		plan = LNGScenarioRunnerUtils.createExtendedSettings(plan, true, true);

		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof InsertionOptimisationStage) {
				insertionStage = (InsertionOptimisationStage) stage;
				break;
			}
		}
		assert insertionStage != null;

		final IOptimiserInjectorService extraService = buildExtraModules(extraDataProvider, initialSolutionProvider);

		boolean isBreakEven = false;
		for (final Slot<?> slot : targetSlots) {
			if (slot.isSetPriceExpression()) {
				if (slot.getPriceExpression().contains("?")) {
					isBreakEven = true;
					break;
				}
			}
		}

		// TODO: Only disable caches if we do a break-even (caches *should* be ok
		// otherwise?)
		final String[] hints = isBreakEven ? hint_with_breakeven : hint_without_breakeven;
		final LNGOptimisationBuilder builder = LNGOptimisationBuilder.begin(originalScenarioDataProvider, scenarioInstance) //
				.withExtraDataProvider(extraDataProvider) //
				.withOptimisationPlan(plan) //
				.withOptimiserInjectorService(extraService) //
				.withOptimiseHint() //
				.withHints(hints) //
		;

		if (builderCustomiser != null) {
			builderCustomiser.accept(builder);
		}
		final LNGOptimisationRunnerBuilder runner = builder.buildDefaultRunner();
		scenarioRunner = runner.getScenarioRunner();

		if (userSettings.isSetPeriodStartDate() || userSettings.isSetPeriodEnd()) {
			// Map between original and possible period scenario
			targetOptimiserSlots = new LinkedList<>();
			targetOptimiserEvents = new LinkedList<>();
			final CargoModelFinder finder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(scenarioRunner.getScenarioToOptimiserBridge().getOptimiserScenario()));
			ExtraDataProvider edp = scenarioRunner.getScenarioToOptimiserBridge().getOptimiserExtraDataProvider();
			if (edp != null) {
				finder.includeLoads(edp.extraLoads);
			}
			if (edp != null) {
				finder.includeDischarges(edp.extraDischarges);
			}
			for (final Slot<?> original : targetSlots) {
				try {
					if (original instanceof LoadSlot) {
						targetOptimiserSlots.add(finder.findLoadSlot(original.getName()));
					} else if (original instanceof DischargeSlot) {
						targetOptimiserSlots.add(finder.findDischargeSlot(original.getName()));
					}
				} catch (final IllegalArgumentException e) {
					// Slot not found - probably outside of period.
					throw new RuntimeException(String.format("Slot %s not included within period", original.getName()));
				}
			}
			for (final VesselEvent original : targetEvents) {
				try {
					targetOptimiserEvents.add(finder.findVesselEvent(original.getName()));
				} catch (final IllegalArgumentException e) {
					// Slot not found - probably outside of period.
					throw new RuntimeException(String.format("Vessel event %s not included within period", original.getName()));
				}
			}
		} else {
			targetOptimiserSlots = targetSlots;
			targetOptimiserEvents = targetEvents;
		}
		scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		performBreakEven = false;
		for (final Slot<?> slot : targetOptimiserSlots) {
			if (slot.getPriceExpression() != null && slot.getPriceExpression().contains("?")) {
				performBreakEven = true;
			}
		}
	}

	private IOptimiserInjectorService buildExtraModules(ExtraDataProvider extraDataProvider, @Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider) {
		return new IOptimiserInjectorService() {

			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

				if (moduleType == ModuleType.Module_InitialSolution) {
					if (!hints.contains(LNGTransformerHelper.HINT_PERIOD_SCENARIO) && initialSolutionProvider != null) {
						return Collections.singletonList(new PrivateModule() {

							@Override
							protected void configure() {
								// Nothing to do here - see provides methods
							}

							@Provides
							@Singleton
							@Named("EXTERNAL_SOLUTION")
							private ISequences provideSequences(final Injector injector, ModelEntityMap mem, IOptimisationData data) {
								ISequences sequences = initialSolutionProvider.apply(mem, data, injector);
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)
							@Exposed
							private ISequences provideInitialSequences(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
							@Exposed
							private IMultiStateResult provideSolutionPair(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {

								return new MultiStateResult(sequences, new HashMap<>());
							}

						});
					}
				}

				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					List<Module> modules = new LinkedList<>();
					modules.add(new AbstractModule() {

						@Override
						protected void configure() {
							// Only one new option per month
							bind(int.class).annotatedWith(Names.named(LNGScenarioTransformer.LIMIT_SPOT_SLOT_CREATION)).toInstance(1);
						}

					});
					return modules;
				}
				return null;
			}

		};
	}

	public void prepare() {
		scenarioRunner.evaluateInitialState();
	}

	public void setIteration(int iterations) {
		insertionStage.setIterations(iterations);
	}

	public SlotInsertionOptions doRunJob(final IProgressMonitor progressMonitor) {
		final long start = System.currentTimeMillis();
		final SubMonitor subMonitor = SubMonitor.convert(progressMonitor, "Inserting option(s)", 100);
		try {
			final IMultiStateResult results = runInsertion(null, subMonitor.split(90));
			if (results == null) {
				System.out.println("Found no solutions");
				return null;
			}

			if (progressMonitor.isCanceled()) {
				return null;
			}

			final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
			if (solutions.size() < 2) {
				System.out.println("Found no solutions");
				return null;
			}

			return exportSolutions(results, subMonitor.split(10));
		} finally {
			SubMonitor.done(progressMonitor);
			if (true) {
				System.out.println("done in:" + (System.currentTimeMillis() - start));
			}
		}

	}

	public IMultiStateResult runInsertion(final @Nullable SlotInsertionOptimiserLogger logger, final IProgressMonitor progressMonitor) {

		final SlotInsertionOptimiserUnit slotInserter = new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), insertionStage,
				scenarioRunner.getJobExecutorFactory(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());

		return slotInserter.run(targetOptimiserSlots, targetOptimiserEvents, logger, progressMonitor);
	}

	public SlotInsertionOptions exportSolutions(final @NonNull IMultiStateResult results, final @NonNull IProgressMonitor monitor) {
		final Schedule schedule = ScenarioModelUtil.getScheduleModel(scenarioToOptimiserBridge.getOptimiserScenario()).getSchedule();
		final long targetPNL = performBreakEven ? ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule) : 0L;
		return exportSolutions(results, targetPNL, monitor);
	}

	private SlotInsertionOptions exportSolutions(final @NonNull IMultiStateResult results, final long targetPNL, final @NonNull IProgressMonitor monitor) {
		final SlotInsertionOptions slotInsertionOptions = AnalyticsFactory.eINSTANCE.createSlotInsertionOptions();
		// Make sure this is the original, not the optimiser
		slotInsertionOptions.getSlotsInserted().addAll(targetSlots);
		slotInsertionOptions.getEventsInserted().addAll(targetEvents);
		slotInsertionOptions.setUserSettings(EMFCopier.copy(userSettings));

		slotInsertionOptions.setName(AnalyticsSolutionHelper.generateName(slotInsertionOptions));

		// TODO: Take from user settings?
		final boolean dualModeInsertions = userSettings.isDualMode();

		final OptionalLong portfolioBreakEvenTarget = performBreakEven ? OptionalLong.of(targetPNL) : OptionalLong.empty();
		final IChainLink link = SolutionSetExporterUnit.exportMultipleSolutions(null, 1, scenarioRunner.getScenarioToOptimiserBridge(), () -> slotInsertionOptions, dualModeInsertions,
				portfolioBreakEvenTarget);

		LNGDataTransformer dt = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer();
		final SequencesContainer initialSequencesContainer = new SequencesContainer(dt.getInitialResult().getBestSolution());
		link.run(dt, initialSequencesContainer, results, monitor);

		return slotInsertionOptions;
	}

	public LNGScenarioRunner getLNGScenarioRunner() {
		return scenarioRunner;
	}
}