/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;

import com.google.common.io.Files;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard.exportInformation;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class IOTestUtil {
	private IOTestUtil() {
	}

	/**
	 * Converts EList<Fitness> into long[] without headers for easy comparison.
	 * 
	 * @param fitnesses
	 *            - Input EList of fitness values
	 * @return array - Output array of fitness longs
	 */
	public static long[] fitnessListToArrayValues(final EList<Fitness> fitnesses) {

		final int size = fitnesses.size();
		final long[] array = new long[size];

		int i = 0;
		for (final Fitness temp : fitnesses) {
			array[i] = (temp.getFitnessValue());
			i += 1;
		}
		return array;

	}

	/**
	 * Deletes the temporary directory and all its contents
	 */
	public static void tempDirectoryTeardown(final File tempDir) {

		final String[] entries = tempDir.list();
		if (entries != null) {
			for (final String s : entries) {
				final File currentFile = new File(tempDir.getPath(), s);
				currentFile.delete();
			}
		}

		tempDir.delete();
	}

	public static URL directoryFileToURL(final File directory) throws MalformedURLException {

		return directory.toURI().toURL();
	}

	/**
	 * Produces an EList of the fitnesses from the passed LNGScenerioModel
	 * 
	 * @param model
	 *            - LNGScenarioModel to acquire fitnesses of
	 * @return - EList<Fitness> of the associated fitnesses.
	 */
	public static EList<Fitness> scenarioModeltoFitnessList(@NonNull final IScenarioDataProvider scenarioDataProvider) {

		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		// Run twice as some fitnesses are based on initial state which may not exist straight after csv reimport
		LNGScenarioRunnerCreator.withOptimisationRunner(scenarioDataProvider, optimisationPlan, runner -> {
			// We just want to evaluate with fitnesses...
		});
		LNGScenarioRunnerCreator.withOptimisationRunner(scenarioDataProvider, optimisationPlan, runner -> {
			// We just want to evaluate with fitnesses...
		});
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioDataProvider);
		final Schedule schedule = scheduleModel.getSchedule();

		Assertions.assertNotNull(schedule);

		return schedule.getFitnesses();
	}

	public static Schedule scenarioModeltoSchedule(final @NonNull IScenarioDataProvider scenarioDataProvider) {

		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
		LNGScenarioRunnerCreator.withOptimisationRunner(scenarioDataProvider, optimisationPlan, runner -> {
			// We just want to evaluate with fitnesses...
		});
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioDataProvider);

		return scheduleModel.getSchedule();
	}

	/**
	 * Exports the passed LNGScenarioModel to a temporary directory
	 * 
	 * @param model
	 *            - the LNGScenarioModel to be written to the temporary directory
	 * @return - URL by which to locate the temporary directory
	 * @throws MalformedURLException
	 */
	public static File exportTestCase(final IScenarioDataProvider model) {

		final ExportCSVWizard ECSVW = new ExportCSVWizard();

		final exportInformation info = ECSVW.new exportInformation();

		final File tempDir = Files.createTempDir();

		info.outputDirectory = tempDir;
		info.delimiter = ',';
		info.decimalSeparator = '.';

		ECSVW.exportScenario(model, info, false, "");

		return tempDir;
	}

}
