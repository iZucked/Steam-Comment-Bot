package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
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
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSandboxJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SandboxManualRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.SandboxOptimiserRunner;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

@NonNullByDefault
public class SandboxJobRunner extends AbstractJobRunner {

	private @Nullable SandboxSettings sandboxSettings;
	private @Nullable IScenarioDataProvider sdp;
	private @Nullable HeadlessSandboxJSON loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		sandboxSettings = mapper.readValue(json, SandboxSettings.class);
	}

	@Override
	public void withParams(final File file) throws IOException {

		try (FileInputStream is = new FileInputStream(file)) {
			final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			withParams(text);
		}
	}

	@Override
	public void withParams(final InputStream is) throws IOException {
		final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		withParams(text);
	}

	@Override
	public void withScenario(final IScenarioDataProvider sdp) {
		this.sdp = sdp;
	}

	@Override
	public AbstractSolutionSet run(final int threadsAvailable, final IProgressMonitor monitor) {
		final SandboxSettings pSandboxSettings = sandboxSettings;
		if (pSandboxSettings == null) {
			throw new IllegalStateException("Optimiser parameters have not been set");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			final HeadlessSandboxJSONTransformer transformer = new HeadlessSandboxJSONTransformer();
			final HeadlessSandboxJSON json = transformer.createJSONResultObject(sandboxSettings);
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);

			loggingData = json;

		}

		return doJobRun(pSDP, pSandboxSettings, loggingData, threadsAvailable, SubMonitor.convert(monitor));
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

	public AbstractSolutionSet doJobRun(final IScenarioDataProvider sdp, final SandboxSettings sandboxSettings, final @Nullable Object loggingData, final int numThreads,
			final IProgressMonitor monitor) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = sandboxSettings.getUserSettings();

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

		final AbstractSolutionSet[] results = new AbstractSolutionSet[1];
		final Consumer<AbstractSolutionSet> action = sandboxResult -> results[0] = sandboxResult;

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		final int mode = model.getMode();
		switch (mode) {
		case SandboxModeConstants.MODE_OPTIONISE -> runSandboxInsertion(sdp, null, model, userSettings, action, false, subMonitor);
		case SandboxModeConstants.MODE_OPTIMISE -> runSandboxOptimisation(sdp, null, model, userSettings, action, false, subMonitor);
		case SandboxModeConstants.MODE_DERIVE -> runSandboxOptions(sdp, null, model, userSettings, action, false, subMonitor);
		}
		if (results[0] == null) {
			throw new RuntimeException("No results model found");
		}
		return results[0];

	}

	public void runSandboxInsertion(final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			@Nullable UserSettings userSettings, final Consumer<AbstractSolutionSet> action, final boolean runAsync, IProgressMonitor progressMonitor) {

		final String taskName = "Sandbox result";

		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user
		userSettings.setShippingOnly(false);
		userSettings.setCleanSlateOptimisation(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> new LNGSandboxJobDescriptor(pTaskName, scenarioInstance,
					createSandboxInsertionFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model), model);

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			final AbstractSolutionSet result = createSandboxInsertionFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(progressMonitor);
			action.accept(result);
		}

	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptionsFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance,
			final UserSettings userSettings, final OptionAnalysisModel model) {

		final SandboxResult sandboxResult = AnalyticsFactory.eINSTANCE.createSandboxResult();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final SandboxManualRunner insertionRunner = new SandboxManualRunner(scenarioInstance, sdp, userSettings, mapper, model);

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return insertionRunner.getBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					return insertionRunner.runSandbox(monitor);
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxInsertionFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance,
			final UserSettings userSettings, final OptionAnalysisModel model) {

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

			for (final EObject obj : objectsToInsert) {
				if (obj instanceof final Slot<?> slot) {
					if (slot instanceof SpotSlot) {
						// Ignore spot market slots.
						continue;
					}
					targetSlots.add(slot);
					sandboxResult.getSlotsInserted().add(slot);
				} else if (obj instanceof final VesselEvent vesselEvent) {
					targetEvents.add(vesselEvent);
					sandboxResult.getEventsInserted().add(vesselEvent);
				}
			}
			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final LNGSchedulerInsertSlotJobRunner runner = new LNGSchedulerInsertSlotJobRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, targetSlots, targetEvents,
					extraDataProvider, (mem, data, injector) -> {
						final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
						return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
					});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return runner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					return runner.runInsertion(new SlotInsertionOptimiserLogger(), monitor);
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxOptimiserFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance,
			final UserSettings userSettings, final OptionAnalysisModel model) {

		final OptimisationResult sandboxResult = AnalyticsFactory.eINSTANCE.createOptimisationResult();
		sandboxResult.setUseScenarioBase(false);

		return createSandboxFunction(sdp, scenarioInstance, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final SandboxOptimiserRunner runner = new SandboxOptimiserRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, extraDataProvider, (mem, data, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
			});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return runner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					return runner.runOptimiser(monitor);
				}
			};
		});
	}

	public Function<IProgressMonitor, AbstractSolutionSet> createSandboxFunction(final IScenarioDataProvider sdp, final @Nullable ScenarioInstance scenarioInstance, final UserSettings userSettings,
			final OptionAnalysisModel model, final AbstractSolutionSet sandboxResult, final BiFunction<IMapperClass, ScheduleSpecification, SandboxJob> jobAction) {

		return monitor -> {

			final boolean dualPNLMode = false;

			final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
			final IMapperClass mapper = new Mapper(lngScenarioModel, false);
			final ScheduleSpecification baseScheduleSpecification = createBaseScheduleSpecification(sdp, model, mapper);

			final SandboxJob sandboxJob = jobAction.apply(mapper, baseScheduleSpecification);

			final IMultiStateResult results = sandboxJob.run(monitor);

			if (results == null) {
				sandboxResult.setName("SandboxResult");
				sandboxResult.setHasDualModeSolutions(dualPNLMode);
				sandboxResult.setUserSettings(EMFCopier.copy(userSettings));
				return sandboxResult;
			}

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = sandboxJob.getScenarioRunner();
			final JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();

			final SolutionSetExporterUnit.Util<SolutionOption> exporter = new SolutionSetExporterUnit.Util<>(scenarioToOptimiserBridge, userSettings, AnalyticsFactory.eINSTANCE::createSolutionOption,
					dualPNLMode, true /* enableChangeDescription */);

			exporter.setBreakEvenMode(model.isUseTargetPNL() ? BreakEvenMode.PORTFOLIO : BreakEvenMode.POINT_TO_POINT);
			sandboxResult.setBaseOption(exporter.useAsBaseSolution(baseScheduleSpecification));

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

			for (final VesselAvailability va : extraDataProvider.extraVesselAvailabilities) {
				if (va != null && va.eContainer() == null) {
					sandboxResult.getExtraVesselAvailabilities().add(va);
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
			}

			if (dualPNLMode) {
				SolutionSetExporterUnit.convertToSimpleResult(sandboxResult, dualPNLMode);
			}

			return sandboxResult;
		};
	}

	public void runSandboxOptimisation(@NonNull final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			@Nullable UserSettings userSettings, final Consumer<AbstractSolutionSet> action, final boolean runAsync, IProgressMonitor progressMonitor) {

		final String taskName = "Sandbox result";

		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> new LNGSandboxJobDescriptor(pTaskName, scenarioInstance,
					createSandboxOptimiserFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model), model);

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			final AbstractSolutionSet result = createSandboxOptimiserFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(progressMonitor);
			action.accept(result);
		}
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
			if (opt instanceof DualModeSolutionOption dualModeSolutionOption) {
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
			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);
			scheduleSpecification.getNonShippedCargoSpecifications().stream() //
					.flatMap(s -> s.getSlotSpecifications().stream()) //
					.forEach(action);
			scheduleSpecification.getOpenEvents().stream() //
					.filter(SlotSpecification.class::isInstance) //
					.map(SlotSpecification.class::cast) //
					.forEach(action);

			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.flatMap(s -> s.getEvents().stream()) //
					.filter(VesselEventSpecification.class::isInstance) //
					.map(VesselEventSpecification.class::cast) //
					.forEach(e -> {
						final VesselEvent s = e.getVesselEvent();
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselEvents().add(s);
						}
					});
			scheduleSpecification.getVesselScheduleSpecifications().stream() //
					.map(VesselScheduleSpecification::getVesselAllocation) //
					.filter(VesselAvailability.class::isInstance) //
					.map(VesselAvailability.class::cast) //
					.forEach(s -> {
						if (s.eContainer() == null) {
							solutionSet.getExtraVesselAvailabilities().add(s);
						}
					});
		}
	}

	public void runSandboxOptions(final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance, final OptionAnalysisModel model,
			@Nullable UserSettings userSettings, final Consumer<AbstractSolutionSet> action, final boolean runAsync, IProgressMonitor progressMonitor) {

		final String taskName = "Sandbox result";

		final EObject object = scenarioDataProvider.getScenario();
		if (userSettings == null && object instanceof LNGScenarioModel root) {

			UserSettings previousSettings = null;
			if (model.getResults() != null) {
				previousSettings = model.getResults().getUserSettings();
			}
			if (previousSettings == null) {
				previousSettings = root.getUserSettings();
			}
			final boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;

			userSettings = UserSettingsHelper.promptForSandboxUserSettings(root, false, promptUser, false, null, previousSettings);
		}
		if (userSettings == null) {
			return;
		}
		// Reset settings not supplied to the user
		userSettings.setShippingOnly(false);
		userSettings.setCleanSlateOptimisation(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final String pTaskName = taskName;
		final UserSettings pUserSettings = userSettings;

		if (runAsync) {
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> new LNGSandboxJobDescriptor(pTaskName, scenarioInstance,
					createSandboxOptionsFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model), model);

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					action.accept((AbstractSolutionSet) jobControl.getJobOutput());
				}
			};
			assert scenarioInstance != null;

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);
			jobRunner.run(taskName, scenarioInstance, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		} else {
			final AbstractSolutionSet result = createSandboxOptionsFunction(scenarioDataProvider, scenarioInstance, pUserSettings, model).apply(progressMonitor);
			action.accept(result);
		}

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
}
