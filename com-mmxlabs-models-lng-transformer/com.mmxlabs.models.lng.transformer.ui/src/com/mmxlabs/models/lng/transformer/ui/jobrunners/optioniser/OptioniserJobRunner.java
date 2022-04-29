/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public class OptioniserJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "optioniser";
	private OptioniserSettings optioniserSettings;
	private HeadlessOptioniserJSON loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		optioniserSettings = mapper.readValue(json, OptioniserSettings.class);
	}

	@Override
	public @Nullable SlotInsertionOptions run(final int threadsAvailable, final IProgressMonitor monitor) {
		if (optioniserSettings == null) {
			throw new IllegalStateException("Optioniser parameters have not been set");
		}
		IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			final HeadlessOptioniserJSONTransformer transformer = new HeadlessOptioniserJSONTransformer();
			final HeadlessOptioniserJSON json = transformer.createJSONResultObject(optioniserSettings);
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);

			loggingData = json;

		}
		return doJobRun(pSDP, optioniserSettings, threadsAvailable, SubMonitor.convert(monitor));
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

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);

		}
		throw new IllegalStateException("Logging not configured");
	}

	private @NonNull SlotInsertionOptions doJobRun(final @NonNull IScenarioDataProvider sdp, final @NonNull OptioniserSettings optioniserSettings, final int threadsAvailable,
			@NonNull final IProgressMonitor monitor) {

		final int startTry = 0;

		final UserSettings userSettings = optioniserSettings.userSettings;

		final Collection<Slot<?>> targetSlots = new LinkedHashSet<>();
		final Collection<VesselEvent> targetEvents = new LinkedHashSet<>();

		final CargoModelFinder cargoFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(sdp));
		optioniserSettings.loadIds.forEach(id -> targetSlots.add(cargoFinder.findLoadSlot(id)));
		optioniserSettings.dischargeIds.forEach(id -> targetSlots.add(cargoFinder.findDischargeSlot(id)));
		optioniserSettings.eventsIds.forEach(id -> targetEvents.add(cargoFinder.findVesselEvent(id)));

		optioniserSettings.loadUUIDs.forEach(id -> targetSlots.add(cargoFinder.findLoadSlotByUUID(id)));
		optioniserSettings.dischargeUUIDs.forEach(id -> targetSlots.add(cargoFinder.findDischargeSlotByUUID(id)));
		optioniserSettings.eventsUUIDs.forEach(id -> targetEvents.add(cargoFinder.findVesselEventByUUID(id)));

		int threadCount = threadsAvailable;

		if (optioniserSettings.numThreads != null && optioniserSettings.numThreads > 0) {
			threadCount = optioniserSettings.numThreads;
		}

		if (threadCount < 1) {
			threadCount = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		}

		final int threadsToUse = threadCount;

		final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(null, // ScenarioInstance
				sdp, sdp.getEditingDomain(), userSettings, //
				new LinkedList<>(targetSlots), new LinkedList<>(targetEvents), //
				null, // Optional extra data provider.
				null, // Alternative initial solution provider
				builder -> {
					builder.withThreadCount(threadsToUse);
				});

		// if (optioniserSettings instanceof HeadlessOptioniserRunner.Options options) {
		// // Override iterations
		// if (options.iterations > 0) {
		// insertionRunner.setIteration(options.iterations);
		// }
		// }
		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		final SlotInsertionOptimiserLogger logger = loggingData == null ? null : new SlotInsertionOptimiserLogger();

		final IMultiStateResult results = insertionRunner.runInsertion(logger, subMonitor.split(90));

		final SlotInsertionOptions result = insertionRunner.exportSolutions(results, subMonitor.split(10));

		if (loggingData != null && logger != null) {
			loggingData.getParams().setCores(threadsToUse);
			loggingData.getParams().getOptioniserProperties().setIterations(insertionRunner.getIterations());

			HeadlessOptioniserJSONTransformer.addRunResult(startTry, logger, loggingData);
		}

		return result;
	}
}
