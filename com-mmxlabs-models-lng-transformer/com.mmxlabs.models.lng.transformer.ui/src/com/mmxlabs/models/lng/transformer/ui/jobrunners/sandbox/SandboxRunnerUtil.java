/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator.BreakEvenMode;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ExistingBaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SandboxReference;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

public class SandboxRunnerUtil {

	private SandboxRunnerUtil() {

	}

	public static Function<IProgressMonitor, AbstractSolutionSet> createSandboxFunction(final IScenarioDataProvider sdp, final UserSettings userSettings, final OptionAnalysisModel model,
			final AbstractSolutionSet sandboxResult, final BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction, @Nullable final Consumer<AbstractSolutionSet> abortedHandler,
			final boolean allowCaching) {

		return monitor -> {

			final boolean dualPNLMode = false;

			final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
			final IMapperClass mapper = new Mapper(lngScenarioModel, false);
			final ScheduleSpecification baseScheduleSpecification = createBaseScheduleSpecification(sdp, model, mapper);

			final SandboxJob sandboxJob = jobAction.apply(mapper, baseScheduleSpecification);

			final IMultiStateResult results = sandboxJob.run(monitor);

			if (results == null || monitor.isCanceled()) {
				sandboxResult.setName("SandboxResult");
				sandboxResult.setHasDualModeSolutions(dualPNLMode);
				sandboxResult.setUserSettings(EMFCopier.copy(userSettings));

				if (abortedHandler != null) {
					abortedHandler.accept(sandboxResult);
				}

				return sandboxResult;
			}

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = sandboxJob.getScenarioRunner();
			final JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();

			final SolutionSetExporterUnit.Util<SolutionOption> exporter = new SolutionSetExporterUnit.Util<>(scenarioToOptimiserBridge, userSettings,
					dualPNLMode ? AnalyticsFactory.eINSTANCE::createDualModeSolutionOption : AnalyticsFactory.eINSTANCE::createSolutionOption, dualPNLMode, true /* enableChangeDescription */);

			exporter.setBreakEvenMode(model.isUseTargetPNL() ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT);
			sandboxResult.setBaseOption(exporter.useAsBaseSolution(baseScheduleSpecification));

			// Disable caches for the export phase
			// TODO: Maybe we need a cache-config event?
			final List<String> hints = new LinkedList<>(scenarioToOptimiserBridge.getDataTransformer().getHints());
			hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
			scenarioToOptimiserBridge.getFullDataTransformer().getLifecyleManager().startPhase("export", hints);

			try (JobExecutor jobExecutor = jobExecutorFactory.begin()) {

				final List<Future<@Nullable SolutionOption>> jobs = new LinkedList<>();

				if (results != null) {
					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
					for (final NonNullPair<ISequences, Map<String, Object>> p : solutions) {
						jobs.add(jobExecutor.submit(() -> {
							return exporter.computeOption(p.getFirst());
						}));
					}

					jobs.forEach(f -> {
						try {
							sandboxResult.getOptions().add(f.get());
						} catch (final Exception e) {
							// Ignore exceptions;
						}
					});
				}
				exporter.applyPostTasks(sandboxResult);
			}

			sandboxResult.setName("SandboxResult");
			sandboxResult.setHasDualModeSolutions(dualPNLMode);
			sandboxResult.setUserSettings(EMFCopier.copy(userSettings));
			sandboxResult.setPortfolioBreakEvenMode(model.isUseTargetPNL());

			// Request this now one all other parts have run to get correct data.
			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			sandboxResult.getCharterInMarketOverrides().addAll(extraDataProvider.extraCharterInMarketOverrides);
			sandboxResult.getExtraCharterInMarkets().addAll(extraDataProvider.extraCharterInMarkets);

			for (final VesselCharter va : extraDataProvider.extraVesselCharters) {
				if (va != null && va.eContainer() == null) {
					sandboxResult.getExtraVesselCharters().add(va);
				}
			}
			sandboxResult.getExtraSlots().addAll(extraDataProvider.extraLoads);
			sandboxResult.getExtraSlots().addAll(extraDataProvider.extraDischarges);
			sandboxResult.getExtraVesselEvents().addAll(extraDataProvider.extraVesselEvents);

			// Check that all the spot slot references are properly contained. E.g. Schedule
			// specification may have extra spot slot instances not used in the schedule
			// models.
			addExtraData(sandboxResult.getBaseOption(), sandboxResult);

			final Consumer<SolutionOption> sandboxRefAction = opt -> {
				addExtraData(opt, sandboxResult);

				final ScheduleModel scheduleModel = opt.getScheduleModel();
				final Schedule schedule = scheduleModel.getSchedule();

				// Create a map of sandbox generated positions to the original object...
				final Map<EObject, EObject> m = new HashMap<>();
				for (final var sandbox : model.getBuys()) {
					m.put(mapper.getOriginal(sandbox), sandbox);
					if (mapper.isCreateBEOptions()) {
						m.put(mapper.getBreakEven(sandbox), sandbox);
						m.put(mapper.getChangable(sandbox), sandbox);
					}
				}
				for (final var sandbox : model.getSells()) {
					m.put(mapper.getOriginal(sandbox), sandbox);
					if (mapper.isCreateBEOptions()) {
						m.put(mapper.getBreakEven(sandbox), sandbox);
						m.put(mapper.getChangable(sandbox), sandbox);
					}
				}
				for (final var sandbox : model.getVesselEvents()) {
					m.put(mapper.getOriginal(sandbox), sandbox);
				}
				// .... then add in a SandboxReference to the schedule object to allow lookup
				// later (e.g. fore createSandboxFromSolution actions)
				// We add to the SlotAllocation rather than the Slot to reduce the chances of
				// dangling references. E.g. exporting the solution would need to clean up the
				// slots
				schedule.eAllContents().forEachRemaining(eObj -> {
					if (eObj instanceof final SlotAllocation sa) {
						final EObject eObject = m.get(sa.getSlot());
						if (eObject != null) {
							final SandboxReference ref = ScheduleFactory.eINSTANCE.createSandboxReference();
							ref.setReference(eObject);
							sa.getExtensions().add(ref);
						}
					} else if (eObj instanceof final OpenSlotAllocation sa) {
						final EObject eObject = m.get(sa.getSlot());
						if (eObject != null) {
							final SandboxReference ref = ScheduleFactory.eINSTANCE.createSandboxReference();
							ref.setReference(eObject);
							sa.getExtensions().add(ref);
						}
					} else if (eObj instanceof final VesselEventVisit sa) {
						final EObject eObject = m.get(sa.getVesselEvent());
						if (eObject != null) {
							final SandboxReference ref = ScheduleFactory.eINSTANCE.createSandboxReference();
							ref.setReference(eObject);
							sa.getExtensions().add(ref);
						}
					}
				});
			};

			sandboxRefAction.accept(sandboxResult.getBaseOption());
			sandboxResult.getOptions().forEach(sandboxRefAction);

			if (dualPNLMode) {
				SolutionSetExporterUnit.convertToSimpleResult(sandboxResult, dualPNLMode);
			}

			return sandboxResult;
		};
	}

	private static ScheduleSpecification createBaseScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, final OptionAnalysisModel model, final IMapperClass mapper) {
		ScheduleSpecification baseScheduleSpecification;

		if (model.getBaseCase().isKeepExistingScenario()) {
			final ExistingBaseCaseToScheduleSpecification builder = new ExistingBaseCaseToScheduleSpecification(scenarioDataProvider, mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase(), model.getMode() == SandboxModeConstants.MODE_OPTIONISE);
		} else {

			final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase(), model.getMode() == SandboxModeConstants.MODE_OPTIONISE);
		}
		return baseScheduleSpecification;
	}

	/**
	 * Check solution option for any extra un-contained objects. E.g. market slots.
	 * 
	 * @param opt
	 * @param sandboxResult
	 */
	private static void addExtraData(final SolutionOption opt, final AbstractSolutionSet solutionSet) {
		if (opt != null) {
			final ScheduleSpecification scheduleSpecification = opt.getScheduleSpecification();
			addExtraData(solutionSet, scheduleSpecification);
			if (opt instanceof final DualModeSolutionOption dualModeSolutionOption) {
				if (dualModeSolutionOption.getMicroBaseCase() != null) {
					addExtraData(solutionSet, dualModeSolutionOption.getMicroBaseCase().getScheduleSpecification());
				}
				if (dualModeSolutionOption.getMicroTargetCase() != null) {
					addExtraData(solutionSet, dualModeSolutionOption.getMicroTargetCase().getScheduleSpecification());
				}
			}
		}
	}

	private static void addExtraData(final AbstractSolutionSet solutionSet, final ScheduleSpecification scheduleSpecification) {
		if (scheduleSpecification != null) {
			final Consumer<SlotSpecification> action = e -> {
				final Slot<?> s = e.getSlot();
				if (s.eContainer() == null) {
					solutionSet.getExtraSlots().add(s);
				}
			};
			scheduleSpecification.getVesselScheduleSpecifications()
					.stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);
			scheduleSpecification.getNonShippedCargoSpecifications()
					.stream() //
					.flatMap(s -> s.getSlotSpecifications().stream()) //
					.forEach(action);
			scheduleSpecification.getOpenEvents()
					.stream() //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);

			scheduleSpecification.getVesselScheduleSpecifications()
					.stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(VesselEventSpecification.class::isInstance) //
					.map(VesselEventSpecification.class::cast) //
					.forEach(e -> {
						final VesselEvent s = e.getVesselEvent();
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselEvents().add(s);
						}
					});
			scheduleSpecification.getVesselScheduleSpecifications()
					.stream() //
					.map(VesselScheduleSpecification::getVesselAllocation) //
					.filter(VesselCharter.class::isInstance) //
					.map(VesselCharter.class::cast) //
					.forEach(s -> {
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselCharters().add(s);
						}
					});
		}
	}
}
