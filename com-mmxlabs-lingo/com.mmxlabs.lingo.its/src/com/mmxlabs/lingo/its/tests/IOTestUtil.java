/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.google.common.io.Files;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard.exportInformation;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;

public class IOTestUtil {

	// public static String[] ListToArray(final EList<CargoAllocation> originalCargoAllocations) {
	//
	// final int size = originalCargoAllocations.size();
	// final String[] array = new String[size];
	//
	// int i = 0;
	// for (final CargoAllocation temp : originalCargoAllocations) {
	// array[i] = (temp.getName());
	// i += 1;
	// }
	// return array;
	//
	// }

	// public static ArrayList makeSlotAllocationArray(final EList<CargoAllocation> originalCargoAllocations) {
	//
	// final ArrayList output = new ArrayList();
	//
	// for (final CargoAllocation ca : originalCargoAllocations) {
	// final EList<SlotAllocation> allocations = ca.getSlotAllocations();
	//
	// for (final SlotAllocation sa : allocations) {
	// output.add(sa.getPort().getLocation());
	// // output.add(sa.getSlot().getCargo().getLoadName());
	// }
	// }
	// return output;
	// }

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

		final URL url = directory.toURI().toURL();

		return url;
	}

	// /**
	// * Takes the URL of a scenario and returns the associated LNGScenarioModel
	// *
	// * @param url
	// * - URL of the scenario files
	// * @return - LNGScenarioModel object created from the target files
	// * @throws MalformedURLException
	// */
	// public static LNGScenarioModel URLtoScenarioCSV(final URL url) throws MalformedURLException {
	//
	// final CSVTestDataProvider csv_tdp = new CSVTestDataProvider(url);
	//
	// final LNGScenarioModel testCase = csv_tdp.getScenarioModel();
	//
	// return testCase;
	//
	// }
	//
	// public static LNGScenarioModel URLtoScenarioLiNGO(final URL url) throws Exception {
	//
	// final AbstractOptimisationResultTester AORT = new AbstractOptimisationResultTester();
	//
	// final ScenarioInstance instance = AORT.loadScenario(url);
	//
	// final LNGScenarioModel testCase = (LNGScenarioModel) instance.getInstance();
	//
	// return testCase;
	//
	// }

	/**
	 * Produces an EList of the fitnesses from the passed LNGScenerioModel
	 * 
	 * @param model
	 *            - LNGScenarioModel to acquire fitnesses of
	 * @return - EList<Fitness> of the associated fitnesses.
	 */
	public static EList<Fitness> ScenarioModeltoFitnessList(@NonNull final LNGScenarioModel model) {

		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		LNGScenarioRunnerCreator.withOptimisationRunner(model, optimisationPlan, runner -> {
			// We just want to evaluate with fitnesses...
		});
		ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(model);
		final Schedule schedule = scheduleModel.getSchedule();

		Assert.assertNotNull(schedule);

		final EList<Fitness> fitnesses = schedule.getFitnesses();

		return fitnesses;
	}

	public static Schedule ScenarioModeltoSchedule(final LNGScenarioModel model) {

		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
		LNGScenarioRunnerCreator.withOptimisationRunner(model, optimisationPlan, runner -> {
			// We just want to evaluate with fitnesses...
		});
		ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(model);

		final Schedule schedule = scheduleModel.getSchedule();

		return schedule;
	}

	/**
	 * Exports the passed LNGScenarioModel to a temporary directory
	 * 
	 * @param model
	 *            - the LNGScenarioModel to be written to the temporary directory
	 * @return - URL by which to locate the temporary directory
	 * @throws MalformedURLException
	 */
	public static File exportTestCase(final LNGScenarioModel model) throws MalformedURLException {

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
