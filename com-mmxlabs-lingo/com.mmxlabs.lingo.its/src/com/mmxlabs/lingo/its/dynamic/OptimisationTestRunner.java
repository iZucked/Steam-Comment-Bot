/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimisationJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJobRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

public class OptimisationTestRunner {
	private OptimisationTestRunner() {

	}

	public static List<DynamicNode> runOptimisationTests(final File baseDirectory) {

		final TriConsumer<List<DynamicNode>, File, File> consumer = (cases, scenarioFile, paramsFile) -> {
			cases.add(DynamicTest.dynamicTest(paramsFile.getName(), () -> {
				CheckedBiConsumer<ScenarioModelRecord, IScenarioDataProvider, Exception> action = (modelRecord, scenarioDataProvider) -> {
					final ObjectMapper objectMapper = new ObjectMapper();

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
					OptimisationTestRunner.runOptimisation(modelRecord, scenarioDataProvider, paramsFile, existingState, existingCompare, saver);
				};

				ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), action);
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

	public static List<DynamicNode> runSandboxTests(final File baseDirectory) {

		final TriConsumer<List<DynamicNode>, File, File> consumer = (cases, scenarioFile, paramsFile) -> {
			cases.add(DynamicTest.dynamicTest(paramsFile.getName(), () -> {
				ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {

					final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
					resultsFolder.mkdir();

					final File comparisonFile = new File(resultsFolder, paramsFile.getName() + ".compare.html");

					final String existingCompare = comparisonFile.exists() ? Files.readString(comparisonFile.toPath()) : null;
					final Consumer<String> saver = comparison -> {
						try {
							Files.writeString(comparisonFile.toPath(), comparison, StandardCharsets.UTF_8);

						} catch (final Exception e) {
							Assertions.fail(e.getMessage(), e);
						}
					};
					OptimisationTestRunner.runSandbox(modelRecord, scenarioDataProvider, paramsFile, existingCompare, saver);
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
							if (params.getName().startsWith("sandbox-") && params.getName().endsWith(".json")) {
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

	public static void runOptimisation(final ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider sdp, ///
			@NonNull final File paramsFile, @Nullable final ScenarioFitnessState existingState, @Nullable final String existingCompareContent,
			@Nullable final BiConsumer<ScenarioFitnessState, String> saver) throws Exception {

		final boolean checkFitnesses = TestingModes.OptimisationTestMode == TestMode.Run;
		final boolean saveFitnesses = TestingModes.OptimisationTestMode == TestMode.Generate;

		// Clear existing results
		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
		analyticsModel.getOptimisations().clear();

		final OptimisationJobRunner runner = new OptimisationJobRunner();
		runner.withParams(paramsFile);
		runner.withScenario(sdp);

		// Run the optimisation
		AbstractSolutionSet solutionSet = runner.run(0, new NullProgressMonitor());

		final ScenarioFitnessState currentState = new ScenarioFitnessState();

		// Compute initial schedule state
		{
			final Schedule intialSchedule = solutionSet.getBaseOption().getScheduleModel().getSchedule();
			Assertions.assertNotNull(intialSchedule);
			currentState.setInitialState(createSolutionStateFromSchedule(intialSchedule));
		}

		// Check and abort
		if (checkFitnesses) {
			Assertions.assertNotNull(existingState);
			Assertions.assertNotNull(existingState.getInitialState());
			Assertions.assertNotNull(existingState.getInitialState().isEquivalent(currentState.getInitialState()));
		}

		// Calculate state for all solutions
		if (!solutionSet.getOptions().isEmpty()) {
			for (final var p : solutionSet.getOptions()) {
				// Re-evaluate against the initial solution fitness objectives
				currentState.getSolutions().add(createSolutionStateFromSchedule(p.getScheduleModel().getSchedule()));
			}
		}

		if (checkFitnesses) {
			Assertions.assertNotNull(existingState);
			// Check intermediate states
			Assertions.assertEquals(existingState.getSolutions().size(), currentState.getSolutions().size());

			for (int i = 0; i < existingState.getSolutions().size(); ++i) {
				Assertions.assertNotNull(existingState.getSolutions().get(i).isEquivalent(currentState.getSolutions().get(i)));
			}

		}

		final AnalyticsSolution solution = new AnalyticsSolution(modelRecord, solutionSet, "optimisation");
		final String compareViewContent = ReportTester.generateAnalyticsSolutionReport(ReportTesterHelper.CHANGESET_REPORT_ID, solution);
		Assertions.assertNotNull(compareViewContent);

		if (checkFitnesses) {
			Assertions.assertEquals(existingCompareContent.replaceAll("\\r\\n", "\n"), compareViewContent.replaceAll("\\r\\n", "\n"));
		}

		if (saveFitnesses) {
			saver.accept(currentState, compareViewContent);
		}
	}

	public static void runSandbox(final ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider sdp, ///
			@NonNull final File paramsFile, @Nullable final String existingCompareContent, @Nullable final Consumer<String> saver) throws Exception {

		final boolean checkResults = TestingModes.OptimisationTestMode == TestMode.Run;
		final boolean saveResults = TestingModes.OptimisationTestMode == TestMode.Generate;

		final SandboxJobRunner runner = new SandboxJobRunner();
		runner.withScenario(sdp);
		runner.withParams(paramsFile);
		final AbstractSolutionSet solutionSet = runner.run(0, new NullProgressMonitor());

		final AnalyticsSolution solution = new AnalyticsSolution(modelRecord, solutionSet, "sandbox");
		final String compareViewContent = ReportTester.generateAnalyticsSolutionReport(ReportTesterHelper.CHANGESET_REPORT_ID, solution);
		Assertions.assertNotNull(compareViewContent);

		if (checkResults) {
			Assertions.assertEquals(existingCompareContent.replaceAll("\\r\\n", "\n"), compareViewContent.replaceAll("\\r\\n", "\n"));
		}

		if (saveResults) {
			saver.accept(compareViewContent);
		}
	}

	private static SolutionState createSolutionStateFromSchedule(final Schedule schedule) {
		final SolutionState ss = new SolutionState();
		for (final Fitness f : schedule.getFitnesses()) {
			ss.addFitnesses(f.getName(), f.getFitnessValue());
		}
		return ss;
	}
}
