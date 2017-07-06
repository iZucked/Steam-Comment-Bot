/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.ScenarioServiceCommandUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class InsertEventContextMenuExtension implements IVesselEventsTableContextMenuExtension {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final Logger log = LoggerFactory.getLogger(InsertEventContextMenuExtension.class);

	public InsertEventContextMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final VesselEvent vesselEvent, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}

		if (!LicenseFeatures.isPermitted("features:options-suggester-events")) {
			return;
		}
		if (vesselEvent instanceof CharterOutEvent) {

			final InsertEventAction action = new InsertEventAction(scenarioEditingLocation, Collections.singletonList(vesselEvent));

			if (vesselEvent.isLocked()) {
				action.setEnabled(false);
				action.setText(action.getText() + " (Locked)");
			}

			menuManager.add(action);
		}
		return;
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}
		if (!LicenseFeatures.isPermitted("features:options-suggester-events")) {
			return;
		}
		{
			final List<VesselEvent> events = new LinkedList<>();
			final Iterator<?> itr = selection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof CharterOutEvent) {
					events.add((CharterOutEvent) obj);
					break;

				}
			}

			if (events.size() > 0) {
				final InsertEventAction action = new InsertEventAction(scenarioEditingLocation, events);

				for (final VesselEvent event : events) {
					if (event.isLocked()) {
						action.setEnabled(false);
						action.setText(action.getText() + " (Locked)");
						break;
					}
				}
				menuManager.add(action);

				return;
			}
		}
	}

	private static class InsertEventAction extends Action {

		private final List<VesselEvent> originalTargetEvents;
		private final IScenarioEditingLocation scenarioEditingLocation;

		public InsertEventAction(final IScenarioEditingLocation scenarioEditingLocation, final List<VesselEvent> targetVesselEvents) {
			super(generateActionName(targetVesselEvents) + " (Beta)");
			this.scenarioEditingLocation = scenarioEditingLocation;
			this.originalTargetEvents = targetVesselEvents;
		}

		@Override
		public void run() {
			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();

			final ScenarioInstance original = scenarioEditingLocation.getScenarioInstance();
			UserSettings userSettings = null;
			{

				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(original);
				try (final ModelReference modelReference = modelRecord.aquireReference("InsertEventContextMenuExtension:1")) {

					final EObject object = modelReference.getInstance();

					if (object instanceof LNGScenarioModel) {
						final LNGScenarioModel root = (LNGScenarioModel) object;

						userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, true, false);
					}
				}
			}
			if (userSettings == null) {
				return;
			}
			// Reset settings not supplied to the user
			userSettings.setShippingOnly(false);
			userSettings.setBuildActionSets(false);
			userSettings.setCleanStateOptimisation(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
			final UserSettings pUserSettings = userSettings;

			final ScenarioInstance duplicate;
			try {
				duplicate = ScenarioServiceCommandUtil.copyTo(original, original, generateActionName(originalTargetEvents));
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			final String taskName = "Insert " + generateActionName(originalTargetEvents);

			final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(duplicate);

			final List<VesselEvent> targetEvents = new LinkedList<>();

			// Map between original and fork
			final BiFunction<ModelReference, LNGScenarioModel, Boolean> prepareCallback = (ref, root) -> {
				// Map between original and fork
				final CargoModelFinder finder = new CargoModelFinder(root.getCargoModel());
				for (final VesselEvent originalEvent : originalTargetEvents) {
					targetEvents.add(finder.findVesselEvent(originalEvent.getName()));
				}
				return true;
			};

			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSlotInsertionJobDescriptor(generateName(targetEvents), duplicate, pUserSettings, Collections.emptyList(), targetEvents);
			};

			final TriConsumer<IJobControl, EJobState, ModelReference> jobCompletedCallback = (jobControl, newState, ref) -> {
				if (newState == EJobState.COMPLETED) {
					try {
						ref.save();
					} catch (final IOException e) {
						e.printStackTrace();
					}

					final SlotInsertionOptions plan = (SlotInsertionOptions) jobControl.getJobOutput();
					if (plan != null) {

						duplicate.setReadonly(true);

						final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
						final AnalyticsSolution data = new AnalyticsSolution(duplicate, plan, generateName(plan));
						data.setCreateInsertionOptions(true);
						eventBroker.post(ChangeSetViewCreatorService_Topic, data);
					}
				}
			};

			jobRunner.run(taskName, duplicate, modelRecord, prepareCallback, createJobDescriptorCallback, jobCompletedCallback);
		}
	}

	private static String generateName(final SlotInsertionOptions plan) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : plan.getEventsInserted()) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateName(final Collection<VesselEvent> events) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : events) {
			names.add(s.getName());
		}

		return "Inserting: " + Joiner.on(", ").join(names);
	}

	private static String generateActionName(final Collection<VesselEvent> events) {

		final List<String> names = new LinkedList<String>();
		for (final VesselEvent s : events) {
			names.add(s.getName());
		}

		return "Insert " + Joiner.on(", ").join(names);
	}
}
