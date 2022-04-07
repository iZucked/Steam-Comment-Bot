/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.CSVImporter;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserSettings;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public class HeadlessOptioniserRunner {

	public static class Options {

		public LocalDate periodStartDate;
		public YearMonth periodEnd;

		public List<String> loadIds = new LinkedList<>();
		public List<String> dischargeIds = new LinkedList<>();
		public List<String> eventsIds = new LinkedList<>();

		public String filename;
		public int minOptioniserThreads = 1;
		public int maxOptioniserThreads = 1;
		public int minWorkerThreads = 1;
		public int maxWorkerThreads = 1;
		public int iterations = 1_000_000;

		public boolean exportResults = false;
		public boolean turnPerfOptsOn = true;

		public boolean outputToJSON = true;
	}

	/**
	 * Runs the optioniser on the specified scenario, using the specified starting seed, with the specified options. The optioniser output is logged to the logger, which can then be used to extract
	 * information about the optioniser run.
	 * 
	 * @param startTry
	 * @param lingoFile
	 * @param logger
	 * @param options
	 * @param completedHook
	 * @throws Exception
	 */

	public void run(final int startTry, final File lingoFile, final SlotInsertionOptimiserLogger logger, final HeadlessOptioniserRunner.Options options,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook) throws Exception {
		// Get the root object
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(lingoFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
			run(logger, options, modelRecord, scenarioDataProvider, completedHook, new NullProgressMonitor());
		});
	}

	public void runFromCSVDirectory(final int startTry, final File csvDirectory, final SlotInsertionOptimiserLogger logger, final HeadlessOptioniserRunner.Options options,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook) throws Exception {

		CSVImporter.runFromCSVDirectory(csvDirectory, (mr, sdp) -> run(logger, options, null, sdp, completedHook, new NullProgressMonitor()));
	}

	public void runFromCsvZipFile(final int startTry, final File zipFile, final SlotInsertionOptimiserLogger logger, final HeadlessOptioniserRunner.Options options,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook) throws Exception {

		CSVImporter.runFromCSVZipFile(zipFile, (mr, sdp) -> run(logger, options, null, sdp, completedHook, new NullProgressMonitor()));
	}

	public SlotInsertionOptions run(final SlotInsertionOptimiserLogger logger, final OptioniserSettings options, final ScenarioModelRecord scenarioModelRecord,
			@NonNull final IScenarioDataProvider sdp, final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, final IProgressMonitor monitor) {

		final UserSettings userSettings = options.userSettings;
		//

		final List<Slot<?>> targetSlots = new LinkedList<>();
		final List<VesselEvent> targetEvents = new LinkedList<>();

		final CargoModelFinder cargoFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(sdp));
		options.loadIds.forEach(id -> targetSlots.add(cargoFinder.findLoadSlot(id)));
		options.dischargeIds.forEach(id -> targetSlots.add(cargoFinder.findDischargeSlot(id)));
		options.eventsIds.forEach(id -> targetEvents.add(cargoFinder.findVesselEvent(id)));

		final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(null, // ScenarioInstance
				sdp, sdp.getEditingDomain(), userSettings, //
				targetSlots, targetEvents, //
				null, // Optional extra data provider.
				null, // Alternative initial solution provider
				null);

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		final IMultiStateResult results = insertionRunner.runInsertion(logger, subMonitor.split(90));

		final SlotInsertionOptions result = insertionRunner.exportSolutions(results, subMonitor.split(10));

		if (completedHook != null) {
			completedHook.accept(scenarioModelRecord, sdp);
		}

		return result;
	}

	public void run(final SlotInsertionOptimiserLogger logger, final HeadlessOptioniserRunner.Options options, final ScenarioModelRecord scenarioModelRecord, @NonNull final IScenarioDataProvider sdp,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, final IProgressMonitor monitor) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = UserSettingsHelper.promptForInsertionUserSettings(lngScenarioModel, false, false, false, null, null);

		if (options.periodStartDate != null) {
			userSettings.setPeriodStartDate(options.periodStartDate);
		}
		if (options.periodEnd != null) {
			userSettings.setPeriodEnd(options.periodEnd);
		}

		final List<Slot<?>> targetSlots = new LinkedList<>();
		final List<VesselEvent> targetEvents = new LinkedList<>();

		final CargoModelFinder cargoFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(sdp));
		options.loadIds.forEach(id -> targetSlots.add(cargoFinder.findLoadSlot(id)));
		options.dischargeIds.forEach(id -> targetSlots.add(cargoFinder.findDischargeSlot(id)));
		options.eventsIds.forEach(id -> targetEvents.add(cargoFinder.findVesselEvent(id)));

		final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(null, // ScenarioInstance
				sdp, sdp.getEditingDomain(), userSettings, //
				targetSlots, targetEvents, //
				null, // Optional extra data provider.
				null, // Alternative initial solution provider
				builder -> {
					if (options.maxWorkerThreads > 0) {
						builder.withThreadCount(options.maxWorkerThreads);
					}
				});

		// Override iterations
		if (options.iterations > 0) {
			insertionRunner.setIterations(options.iterations);
		}

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		final IMultiStateResult results = insertionRunner.runInsertion(logger, subMonitor.split(90));

		if (options.exportResults) {
			insertionRunner.exportSolutions(results, subMonitor.split(10));
		}

		if (completedHook != null) {
			completedHook.accept(scenarioModelRecord, sdp);
		}
	}

	

}
