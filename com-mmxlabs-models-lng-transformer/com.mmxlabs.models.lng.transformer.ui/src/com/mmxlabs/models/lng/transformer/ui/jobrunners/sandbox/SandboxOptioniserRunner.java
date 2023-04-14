/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserSettings;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.insertion.OptioniserLogger;

public class SandboxOptioniserRunner {

	public static Function<IProgressMonitor, AbstractSolutionSet> createSandboxJobFunction(final int threadsAvailable, final IScenarioDataProvider sdp,
			final @Nullable ScenarioInstance scenarioInstance, final SandboxSettings sandboxSettings, final OptionAnalysisModel model, @Nullable final Meta meta,
			@Nullable final Consumer<Object> registerLogging) {

		final SlotInsertionOptions sandboxResult = AnalyticsFactory.eINSTANCE.createSlotInsertionOptions();
		sandboxResult.setUseScenarioBase(false);
		final UserSettings userSettings = sandboxSettings.getUserSettings();
		return SandboxRunnerUtil.createSandboxFunction(sdp, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {

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
			final HeadlessOptioniserJSON loggingData;
			if (registerLogging != null) {
				final HeadlessOptioniserJSONTransformer transformer = new HeadlessOptioniserJSONTransformer();
				loggingData = transformer.createJSONResultObject(optioniserSettings);
				if (meta != null) {
					loggingData.setMeta(meta);
				}
				loggingData.getParams().setCores(threadsAvailable);
				loggingData.setType("sandbox:" + loggingData.getType());
				registerLogging.accept(loggingData);
			} else {
				loggingData = null;
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
					final OptioniserLogger logger = new OptioniserLogger();
					try {
						return runner.runInsertion(logger, monitor);
					} finally {
						if (loggingData != null) {
							final long runTime = System.currentTimeMillis() - startTime;
							loggingData.getMetrics().setRuntime(runTime);
							loggingData.getParams().getOptioniserProperties().setIterations(runner.getIterations());

							HeadlessOptioniserJSONTransformer.addRunResult(0, logger, loggingData);

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
}
