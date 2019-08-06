package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public class HeadlessOptioniserRunner {

	public static class Options {

		public LocalDate periodStart;
		public YearMonth periodEnd;

		public List<String> loadIds = new LinkedList<>();
		public List<String> dischargeIds = new LinkedList<>();
		public List<String> eventsIds = new LinkedList<>();

		public String filename;
		public int numRuns = 1;
		public int minOptioniserThreads = 1;
		public int maxOptioniserThreads = 1;
		public int minWorkerThreads = 1;
		public int maxWorkerThreads = 1;
		public int iterations = 1_000_000;

		public boolean exportResults = false;
		public boolean turnPerfOptsOn = true;
		
		public boolean outputToJSON = true;
	}

	public void run(final int startTry, final File lingoFile, final SlotInsertionOptimiserLogger logger, final Options options, final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook)
			throws Exception {
		// Get the root object
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(lingoFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
			run(startTry, logger, options, modelRecord, scenarioDataProvider, completedHook);
		});
	}

	public void run(final int startTry, final SlotInsertionOptimiserLogger logger, final Options options, final ScenarioModelRecord scenarioModelRecord, @NonNull final IScenarioDataProvider sdp,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = OptimisationHelper.promptForInsertionUserSettings(lngScenarioModel, false, false, false, null, null);

		// Reset settings not supplied to the user
		userSettings.setShippingOnly(false);
		userSettings.setBuildActionSets(false);
		userSettings.setCleanStateOptimisation(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		if (options.periodStart != null) {
			userSettings.setPeriodStartDate(options.periodStart);
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
		insertionRunner.setIteration(options.iterations);

		final IMultiStateResult results = insertionRunner.runInsertion(logger, new NullProgressMonitor());
		// Includes starting solution, so take off one.
		logger.setSolutionsFound(results.getSolutions().size() - 1);

		if (options.exportResults) {
			insertionRunner.exportSolutions(results, 0L, new NullProgressMonitor());
		}

		if (completedHook != null) {
			completedHook.accept(scenarioModelRecord, sdp);
		}

		insertionRunner.dispose();
	}

}
