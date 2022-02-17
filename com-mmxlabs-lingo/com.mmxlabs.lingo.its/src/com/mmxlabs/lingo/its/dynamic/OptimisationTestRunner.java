/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lngdataserver.lng.importers.menus.UserSettingsMixin;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class OptimisationTestRunner {
	private OptimisationTestRunner() {

	}

	public static List<DynamicNode> runOptimisationTests(File baseDirectory) {

		final TriConsumer<List<DynamicNode>, File, File> consumer = (cases, scenarioFile, paramsFile) -> {
			cases.add(DynamicTest.dynamicTest(paramsFile.getName(), () -> {
				ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {

					final UserSettings userSettings = OptimisationTestRunner.getUserSettings(paramsFile);
					Assertions.assertNotNull(userSettings);
					assert userSettings != null; // For null analysis

					final ObjectMapper objectMapper = new ObjectMapper();
					final JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);

					final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
					resultsFolder.mkdir();

					final File fitnessesFile = new File(resultsFolder, paramsFile.getName() + ".fitnesses");
					final File comparisonFile = new File(resultsFolder, paramsFile.getName() + ".compare.html");

					final ScenarioFitnessState existingState = fitnessesFile.exists() ? objectMapper.readValue(fitnessesFile, ScenarioFitnessState.class) : null;
					final String existingCompare = comparisonFile.exists() ? Files.readString(comparisonFile.toPath()) : null;
					final BiConsumer<ScenarioFitnessState, String> saver = (fitnesses, comparison) -> {
						try {
							new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fitnessesFile, fitnesses);

							Files.writeString(comparisonFile.toPath(), comparison, StandardCharsets.UTF_8);

						} catch (final Exception e) {
							Assertions.fail(e.getMessage(), e);
						}
					};
					OptimisationTestRunner.runOptimisation(modelRecord, scenarioDataProvider, userSettings, null, existingState, existingCompare, saver);
				});
			}));
		};

		final List<DynamicNode> allCases = new LinkedList<>();
		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final String name = f.getName();
					final File scenario = new File(f, "scenario.lingo");
					if (scenario.exists()) {
						final List<DynamicNode> cases = new LinkedList<>();

						for (final File params : f.listFiles()) {
							if (params.getName().startsWith("optimiser-") && params.getName().endsWith(".json")) {
								consumer.accept(cases, scenario, params);
							}
						}

						if (!cases.isEmpty()) {
							allCases.add(DynamicContainer.dynamicContainer(name, cases));
						}
					}
				}
			}
		}
		return allCases;
	}

	public static @Nullable UserSettings getUserSettings(final File file) {

		if (!file.exists()) {
			return null;
		} else {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.enable(Feature.ALLOW_COMMENTS);
			
			mapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
			mapper.addMixIn(UserSettings.class, UserSettingsMixin.class);

			try {
				return mapper.readValue(file, UserSettings.class);
			} catch (final IOException e) {
				Assertions.fail(e.getMessage(), e);
				return null;
			}
		}

	}

	public static void runOptimisation(final ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider sdp, ///
			@NonNull final UserSettings userSettings, @Nullable final Consumer<OptimisationPlan> planCustomiser, @Nullable final ScenarioFitnessState existingState,
			@Nullable final String existingCompareContent, @Nullable final BiConsumer<ScenarioFitnessState, String> saver) throws Exception {

		final boolean checkFitnesses = TestingModes.OptimisationTestMode == TestMode.Run;
		final boolean saveFitnesses = TestingModes.OptimisationTestMode == TestMode.Generate;

		// Clear existing results
		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
		analyticsModel.getOptimisations().clear();

		// Convert user settings to a optimisation plan
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, sdp.getTypedScenario(LNGScenarioModel.class));
		Assertions.assertNotNull(optimisationPlan);
		// Extend the plan if needed
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);
		Assertions.assertNotNull(optimisationPlan);

		// Give caller opportunity to tweak final plan (e.g. set iterations)
		if (planCustomiser != null) {
			planCustomiser.accept(optimisationPlan);
		}

		// Build a default runner.
		final LNGOptimisationRunnerBuilder builder = LNGOptimisationBuilder.begin(sdp, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.buildDefaultRunner();

		final LNGScenarioRunner runner = builder.getScenarioRunner();

		final ScenarioFitnessState currentState = new ScenarioFitnessState();

		// Compute initial schedule state
		{
			final Schedule intialSchedule = runner.evaluateInitialState();
			Assertions.assertNotNull(intialSchedule);
			currentState.setInitialState(createSolutionStateFromSchedule(intialSchedule));
		}

		// Check and abort
		if (checkFitnesses) {
			Assertions.assertNotNull(existingState);
			Assertions.assertNotNull(existingState.getInitialState());
			Assertions.assertNotNull(existingState.getInitialState().isEquivalent(currentState.getInitialState()));
		}

		// Run the optimisation
		final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

		// Calculate state for all solutions
		if (!result.getSolutions().isEmpty()) {
			for (final NonNullPair<ISequences, Map<String, Object>> p : result.getSolutions()) {
				// Re-evaluate against the initial solution fitness objectives
				final IMultiStateResult r = runner.getScenarioToOptimiserBridge().getDataTransformer().evalWithFitness(p.getFirst());
				currentState.getOtherSolutions().add(createSolutionStateFromExtraAnnotations(r.getBestSolution().getSecond()));
			}
		}

		// "Best" solution
		{
			final IMultiStateResult r = runner.getScenarioToOptimiserBridge().getDataTransformer().evalWithFitness(result.getBestSolution().getFirst());
			currentState.setBestState(createSolutionStateFromExtraAnnotations(r.getBestSolution().getSecond()));
		}

		if (checkFitnesses) {
			Assertions.assertNotNull(existingState);
			// Check intermediate states
			Assertions.assertEquals(existingState.getOtherSolutions().size(), currentState.getOtherSolutions().size());

			for (int i = 0; i < existingState.getOtherSolutions().size(); ++i) {
				Assertions.assertNotNull(existingState.getOtherSolutions().get(i).isEquivalent(currentState.getOtherSolutions().get(i)));
			}

			// Check end state
			Assertions.assertNotNull(existingState.getBestState());
			Assertions.assertNotNull(existingState.getBestState().isEquivalent(currentState.getBestState()));
		}

		Assertions.assertFalse(analyticsModel.getOptimisations().isEmpty());
		final AbstractSolutionSet solutionSet = analyticsModel.getOptimisations().get(0);

		final ScenarioResult pinResult = new ScenarioResultImpl(modelRecord, solutionSet.getBaseOption().getScheduleModel());
		final ScenarioResult refResult = new ScenarioResultImpl(modelRecord, solutionSet.getOptions().get(solutionSet.getOptions().size() - 1).getScheduleModel());

		final String compareViewContent = ReportTester.generatePinDiffReport(ReportTesterHelper.CHANGESET_REPORT_ID, pinResult, refResult);
		Assertions.assertNotNull(compareViewContent);

		if (checkFitnesses) {
			Assertions.assertEquals(existingCompareContent.replaceAll("\\r\\n", "\n"), compareViewContent.replaceAll("\\r\\n", "\n"));
		}

		if (saveFitnesses) {
			saver.accept(currentState, compareViewContent);
		}
	}

	private static SolutionState createSolutionStateFromSchedule(final Schedule schedule) {
		final SolutionState ss = new SolutionState();
		for (final Fitness f : schedule.getFitnesses()) {
			ss.addFitnesses(f.getName(), f.getFitnessValue());
		}
		return ss;
	}

	private static SolutionState createSolutionStateFromExtraAnnotations(@NonNull final Map<String, Object> extraAnnotations) {
		final SolutionState ss = new SolutionState();
		final Map<String, Long> fitnesses = (Map<String, Long>) extraAnnotations.get(OptimiserConstants.G_AI_fitnessComponents);
		if (fitnesses != null) {
			for (final Map.Entry<String, Long> entry : fitnesses.entrySet()) {
				ss.addFitnesses(entry.getKey(), entry.getValue());
			}
		}

		return ss;
	}

}
