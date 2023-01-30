/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.json.simple.JSONArray;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator.BreakEvenMode;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ExistingBaseCaseToScheduleSpecification;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotSlot;
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
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SandboxManualRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SandboxOptimiserRunner;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.LSOLoggingExporter;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.ScenarioMetaUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.EvaluationSettingsOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LNGOptimisationOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LoggingModule;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserSettings;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

@NonNullByDefault
public class SandboxJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "sandbox";
	public static final String JOB_DISPLAY_NAME = "Sandbox";

	private @Nullable SandboxSettings sandboxSettings;
	private @Nullable Object loggingData;

	public void withParams(final SandboxSettings settings) {
		sandboxSettings = settings;
	}

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		sandboxSettings = mapper.readValue(json, SandboxSettings.class);
	}

	@Override
	public @Nullable AbstractSolutionSet run(final int threadsAvailable, final IProgressMonitor monitor) {
		final SandboxSettings pSandboxSettings = sandboxSettings;
		if (pSandboxSettings == null) {
			throw new IllegalStateException("Optimiser parameters have not been set");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		return doJobRun(pSDP, pSandboxSettings, threadsAvailable, SubMonitor.convert(monitor));
	}

	@Override
	public void saveLogs(final File file) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(new FileOutputStream(file))) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public void saveLogs(final OutputStream os) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(os)) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public String saveLogsAsString() throws IOException {
		if (enableLogging && loggingData != null) {

			// HeadlessRunnerUtils.renameInvalidBsonFields(loggingData.getMetrics().getStages());

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			final CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
			resolver.init(JsonTypeInfo.Id.CLASS, null);
			resolver.inclusion(JsonTypeInfo.As.PROPERTY);
			resolver.typeProperty("@class");
			mapper.setDefaultTyping(resolver);

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);
		}
		throw new IllegalStateException("Logging not configured");
	}

	public @Nullable AbstractSolutionSet doJobRun(final IScenarioDataProvider sdp, final SandboxSettings sandboxSettings, final int numThreads, final IProgressMonitor monitor) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = sandboxSettings.getUserSettings();

		final OptionAnalysisModel model = findSandboxModel(sandboxSettings, lngScenarioModel);

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		int threadsToUse = numThreads;
		if (threadsToUse < 1) {
			threadsToUse = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		}

		final int mode = model.getMode();
		switch (mode) {
		case SandboxModeConstants.MODE_OPTIONISE:
			return runSandboxInsertion(threadsToUse, sdp, null, model, userSettings, subMonitor);
		case SandboxModeConstants.MODE_OPTIMISE:
			return runSandboxOptimisation(threadsToUse, sdp, null, model, userSettings, subMonitor);
		case SandboxModeConstants.MODE_DERIVE:
			return runSandboxOptions(threadsToUse, sdp, null, model, userSettings, subMonitor);
		}

		throw new IllegalArgumentException("Unknown sandbox mode");
	}

	private OptionAnalysisModel findSandboxModel(final SandboxSettings sandboxSettings, final LNGScenarioModel lngScenarioModel) {
		OptionAnalysisModel model = null;
		for (final OptionAnalysisModel oam : lngScenarioModel.getAnalyticsModel().getOptionModels()) {
			// Prefer UUID
			if (sandboxSettings.getSandboxUUID() != null) {
				if (Objects.equals(sandboxSettings.getSandboxUUID(), oam.getUuid())) {
					model = oam;
					break;
				}
			} else if (sandboxSettings.getSandboxName() != null) {
				if (Objects.equals(sandboxSettings.getSandboxName(), oam.getName())) {
					model = oam;
					break;
				}
			}
		}
		if (model == null) {
			if (sandboxSettings.getSandboxUUID() != null) {
				throw new IllegalArgumentException("Missing sandbox " + sandboxSettings.getSandboxUUID());
			} else if (sandboxSettings.getSandboxName() != null) {
				throw new IllegalArgumentException("Missing sandbox " + sandboxSettings.getSandboxName());
			}
			throw new IllegalArgumentException("Missing sandbox");
		}
		return model;
	}

	public @Nullable AbstractSolutionSet runSandboxInsertion(final int numThreads, final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final UserSettings userSettings, final IProgressMonitor progressMonitor) {

		return createSandboxInsertionFunction(numThreads, scenarioDataProvider, scenarioInstance, userSettings, model).apply(progressMonitor);

	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptionsFunction(final int threadsAvailable, final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance,
			final UserSettings userSettings, final OptionAnalysisModel model) {

		final SandboxResult sandboxResult = AnalyticsFactory.eINSTANCE.createSandboxResult();
		sandboxResult.setUseScenarioBase(false);

		if (enableLogging) {

			final HeadlessSandboxJSONTransformer transformer = new HeadlessSandboxJSONTransformer();
			final HeadlessSandboxJSON json = transformer.createJSONResultObject(sandboxSettings);
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);
			// Should grab this from the task
			json.setType("sandbox:define");
			loggingData = json;
		}
		boolean  allowCaching = false;
		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final SandboxManualRunner deriveRunner = new SandboxManualRunner(scenarioInstance, sdp, userSettings, mapper, model);

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return deriveRunner.getBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					final long startTime = System.currentTimeMillis();
					try {
						return deriveRunner.runSandbox(monitor);
					} finally {
						final long runTime = System.currentTimeMillis() - startTime;

						if (loggingData instanceof final HeadlessSandboxJSON json) {
							json.getMetrics().setRuntime(runTime);

							final ScenarioMeta scenarioMeta = ScenarioMetaUtils.writeOptimisationMetrics( //
									deriveRunner.getBridge().getOptimiserScenario(), //
									userSettings);

							json.setScenarioMeta(scenarioMeta);

						}
					}
				}
			};
		}, allowCaching);
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxInsertionFunction(final int threadsAvailable, final IScenarioDataProvider sdp,
			final @Nullable ScenarioInstance scenarioInstance, final UserSettings userSettings, final OptionAnalysisModel model) {

		final SlotInsertionOptions sandboxResult = AnalyticsFactory.eINSTANCE.createSlotInsertionOptions();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final List<Slot<?>> targetSlots = new LinkedList<>();
			final List<VesselEvent> targetEvents = new LinkedList<>();

			final List<EObject> objectsToInsert = new LinkedList<>();
			for (final BaseCaseRow row : model.getBaseCase().getBaseCase()) {
				if (!row.isOptionise()) {
					continue;
				}
				if (row.getVesselEventOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getVesselEventOption()));
				}
				if (row.getBuyOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getBuyOption()));
				}
				if (row.getSellOption() != null) {
					objectsToInsert.add(mapper.getOriginal(row.getSellOption()));
				}
			}

			if (objectsToInsert.isEmpty()) {
				throw new UserFeedbackException("No targets are selected to optionise");
			}

			// Created for the logging component.
			final OptioniserSettings optioniserSettings = new OptioniserSettings();
			optioniserSettings.numThreads = threadsAvailable;

			for (final EObject obj : objectsToInsert) {
				if (obj instanceof final Slot<?> slot) {
					if (slot instanceof SpotSlot) {
						// Ignore spot market slots.
						continue;
					}
					targetSlots.add(slot);
					sandboxResult.getSlotsInserted().add(slot);
					if (slot instanceof LoadSlot) {
						optioniserSettings.loadUUIDs.add(slot.getUuid());
					} else if (slot instanceof DischargeSlot) {
						optioniserSettings.dischargeUUIDs.add(slot.getUuid());
					}
				} else if (obj instanceof final VesselEvent vesselEvent) {
					targetEvents.add(vesselEvent);
					sandboxResult.getEventsInserted().add(vesselEvent);
					optioniserSettings.eventsUUIDs.add(vesselEvent.getUuid());
				}
			}

			if (enableLogging) {
				final HeadlessOptioniserJSONTransformer transformer = new HeadlessOptioniserJSONTransformer();
				final HeadlessOptioniserJSON json = transformer.createJSONResultObject(optioniserSettings);
				if (meta != null) {
					json.setMeta(meta);
				}
				json.getParams().setCores(threadsAvailable);
				json.setType("sandbox:" + json.getType());
				loggingData = json;
			}

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final LNGSchedulerInsertSlotJobRunner runner = new LNGSchedulerInsertSlotJobRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, targetSlots, targetEvents,
					extraDataProvider, (mem, data, injector) -> {
						final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
						return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
					}, builder -> {
						builder.withThreadCount(threadsAvailable);
					});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return runner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					final long startTime = System.currentTimeMillis();
					SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();
					try {
						return runner.runInsertion(logger, monitor);
					} finally {
						if (loggingData instanceof final HeadlessOptioniserJSON json) {
							final long runTime = System.currentTimeMillis() - startTime;
							json.getMetrics().setRuntime(runTime);
							json.getParams().getOptioniserProperties().setIterations(runner.getIterations());

							HeadlessOptioniserJSONTransformer.addRunResult(0, logger, json);

						}
					}
				}
			};
		}, solution -> {
			// Aborted, clear reference to inserted elements as they may not really exist
			// (e.g. sandbox)
			if (solution instanceof final SlotInsertionOptions res) {
				res.getSlotsInserted().clear();
				res.getEventsInserted().clear();
			}
		}, false);
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptimiserFunction(final int threadsAvailable, final IScenarioDataProvider sdp,
			final @Nullable ScenarioInstance scenarioInstance, final UserSettings userSettings, final OptionAnalysisModel model) {

		final OptimisationResult sandboxResult = AnalyticsFactory.eINSTANCE.createOptimisationResult();
		sandboxResult.setUseScenarioBase(false);
		
		boolean allowCaching = false; //model.isUseTargetPNL()
		
		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {
			final AbstractRunnerHook runnerHook;
			final IOptimiserInjectorService loggingOverrides;
			if (enableLogging) {
				final HeadlessOptimiserJSONTransformer transformer = new HeadlessOptimiserJSONTransformer();
				final HeadlessOptimiserJSON json = transformer.createJSONResultObject();
				if (meta != null) {
					json.setMeta(meta);
				}
				json.getParams().setCores(threadsAvailable);
				json.setType("sandbox:" + json.getType());

				loggingData = json;

				final JSONArray jsonStagesStorage = new JSONArray(); // array for output data at present

				json.getMetrics().setStages(jsonStagesStorage);

				// Create logging module
				final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();
				runnerHook = createRunnerHook(jsonStagesStorage, phaseToLoggerMap);

				JsonNode injections = null;
				boolean doInjections = false;

				loggingOverrides = createExtraModule(true, phaseToLoggerMap, runnerHook, doInjections, injections);
			} else {
				runnerHook = null;
				loggingOverrides = null;
			}

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final SandboxOptimiserRunner runner = new SandboxOptimiserRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, extraDataProvider, (mem, data, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
			}, builder -> {
				builder.withThreadCount(threadsAvailable);

				if (enableLogging) {
					builder.withRunnerHook(runnerHook);
					builder.withOptimiserInjectorService(loggingOverrides);
					builder.withOptimisationPlanCustomiser(plan -> {
						if (loggingData instanceof HeadlessOptimiserJSON loggingOutput) {
							loggingOutput.setOptimisationPlan(plan);
						}
					});
				}
			});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return runner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					final long startTime = System.currentTimeMillis();
					try {
						return runner.runOptimiser(monitor);
					} finally {

						if (loggingData instanceof final HeadlessOptimiserJSON json) {
							final long runTime = System.currentTimeMillis() - startTime;
							json.getMetrics().setRuntime(runTime);
						}
					}
				}
			};
		}, allowCaching);
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model, final AbstractSolutionSet sandboxResult, final BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction, boolean allowCaching) {
		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, jobAction, null, allowCaching);
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model, final AbstractSolutionSet sandboxResult, final BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction,
			@Nullable final Consumer<AbstractSolutionSet> abortedHandler, boolean allowCaching) {

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
					dualPNLMode ? AnalyticsFactory.eINSTANCE::createDualModeSolutionOption : AnalyticsFactory.eINSTANCE::createSolutionOption, dualPNLMode, 
							true /* enableChangeDescription */);

			exporter.setBreakEvenMode(model.isUseTargetPNL() ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT);
			sandboxResult.setBaseOption(exporter.useAsBaseSolution(baseScheduleSpecification));
			
			// Disable caches for the export phase
			// TODO: Maybe we need a cache-config event?
			List<String> hints = new LinkedList<>(scenarioToOptimiserBridge.getDataTransformer().getHints());
			hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
			scenarioToOptimiserBridge.getDataTransformer().getLifecyleManager().startPhase("export", hints);

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
			for (final SolutionOption opt : sandboxResult.getOptions()) {
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
			}

			if (dualPNLMode) {
				SolutionSetExporterUnit.convertToSimpleResult(sandboxResult, dualPNLMode);
			}

			return sandboxResult;
		};
	}

	public @Nullable AbstractSolutionSet runSandboxOptimisation(final int numThreads, final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final UserSettings userSettings, final IProgressMonitor progressMonitor) {

		return createSandboxOptimiserFunction(numThreads, scenarioDataProvider, scenarioInstance, userSettings, model).apply(progressMonitor);

	}

	public interface SandboxJob {
		LNGScenarioToOptimiserBridge getScenarioRunner();

		IMultiStateResult run(IProgressMonitor monitor);
	}

	/**
	 * Check solution option for any extra un-contained objects. E.g. market slots.
	 * 
	 * @param opt
	 * @param sandboxResult
	 */
	private void addExtraData(final SolutionOption opt, final AbstractSolutionSet solutionSet) {
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

	private void addExtraData(final AbstractSolutionSet solutionSet, final ScheduleSpecification scheduleSpecification) {
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

	public @Nullable AbstractSolutionSet runSandboxOptions(final int numThreads, final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final UserSettings userSettings, final IProgressMonitor progressMonitor) {
		return createSandboxOptionsFunction(numThreads, scenarioDataProvider, scenarioInstance, userSettings, model).apply(progressMonitor);
	}

	private ScheduleSpecification createBaseScheduleSpecification(final IScenarioDataProvider scenarioDataProvider, final OptionAnalysisModel model, final IMapperClass mapper) {
		ScheduleSpecification baseScheduleSpecification;

		if (model.getBaseCase().isKeepExistingScenario()) {
			final ExistingBaseCaseToScheduleSpecification builder = new ExistingBaseCaseToScheduleSpecification(scenarioDataProvider, mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase());
		} else {

			final BaseCaseToScheduleSpecification builder = new BaseCaseToScheduleSpecification(scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), mapper);
			baseScheduleSpecification = builder.generate(model.getBaseCase());
		}
		return baseScheduleSpecification;
	}

	private AbstractRunnerHook createRunnerHook(final JSONArray jsonStagesStorage, final Map<String, LSOLogger> phaseToLoggerMap) {
		return new AbstractRunnerHook() {

			@Override
			protected void doEndStageJob(@NonNull final String stage, final int jobID, @Nullable final Injector injector) {

				final String stageAndJobID = getStageAndJobID();
				final LSOLogger logger = phaseToLoggerMap.remove(stageAndJobID);
				if (logger != null) {
					final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(jsonStagesStorage, stageAndJobID, logger);
					lsoLoggingExporter.exportData("best-fitness", "current-fitness");
				}
			}

		};
	}

	private @Nullable IOptimiserInjectorService createExtraModule(final boolean exportLogs, final Map<String, LSOLogger> phaseToLoggerMap, final AbstractRunnerHook runnerHook,
			final boolean doInjections, @Nullable final JsonNode injections) {
		if (!exportLogs && !doInjections) {
			return null;
		}

		return new IOptimiserInjectorService() {
			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (doInjections) {
					if (moduleType == ModuleType.Module_EvaluationParametersModule) {
						return Collections.<@NonNull Module> singletonList(new EvaluationSettingsOverrideModule(injections));
					}
				}
				// if (moduleType == ModuleType.Module_OptimisationParametersModule) {
				// return Collections.<@NonNull Module> singletonList(new
				// OptimisationSettingsOverrideModule());
				// }
				if (moduleType == ModuleType.Module_Optimisation) {
					final LinkedList<@NonNull Module> modules = new LinkedList<>();
					if (exportLogs) {

						// Default logging parameters
						final LoggingParameters loggingParameters = new LoggingParameters();
						loggingParameters.loggingInterval = 5000;
						loggingParameters.metricsToLog = new String[0];

						modules.add(new LoggingModule(phaseToLoggerMap, runnerHook, loggingParameters));
					}
					if (doInjections) {
						modules.add(new LNGOptimisationOverrideModule(injections));
					}

					return modules;
				}
				return null;
			}
		};
	}
}
