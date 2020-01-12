/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper.NameProvider;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class InsertEventContextMenuExtension implements IVesselEventsTableContextMenuExtension {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final Logger log = LoggerFactory.getLogger(InsertEventContextMenuExtension.class);

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

			if (!events.isEmpty()) {
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
			super(AnalyticsSolutionHelper.generateInsertionName(targetVesselEvents));
			this.scenarioEditingLocation = scenarioEditingLocation;
			this.originalTargetEvents = targetVesselEvents;
		}

		@Override
		public void run() {
			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();

			final ScenarioInstance original = scenarioEditingLocation.getScenarioInstance();
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(original);
			UserSettings userSettings = null;

			String taskName = AnalyticsSolutionHelper.generateInsertionName(originalTargetEvents);
			{

				try (final ModelReference modelReference = originalModelRecord.aquireReference("InsertEventContextMenuExtension:1")) {

					final EObject object = modelReference.getInstance();

					if (object instanceof LNGScenarioModel) {
						final LNGScenarioModel root = (LNGScenarioModel) object;
						Set<String> existingNames = new HashSet<>();
						original.getFragments().forEach(f -> existingNames.add(f.getName()));
						original.getElements().forEach(f -> existingNames.add(f.getName()));
						NameProvider nameProvider = new NameProvider(taskName, existingNames);
						userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, true, false, nameProvider);
						taskName = nameProvider.getNameSuggestion();
					}
				}
			}
			if (userSettings == null) {
				return;
			}
			// Reset settings not supplied to the user
			userSettings.setShippingOnly(false);
			userSettings.setBuildActionSets(false);
			userSettings.setCleanSlateOptimisation(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
			final UserSettings pUserSettings = userSettings;

			final List<VesselEvent> targetEvents = new LinkedList<>();

			final String pTaskName = taskName;
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSlotInsertionJobDescriptor(pTaskName, original, pUserSettings, Collections.emptyList(), targetEvents);
			};

			final TriConsumer<IJobControl, EJobState, IScenarioDataProvider> jobCompletedCallback = (jobControl, newState, sdp) -> {
				if (newState == EJobState.COMPLETED) {
					final SlotInsertionOptions plan = (SlotInsertionOptions) jobControl.getJobOutput();
					if (plan != null) {
						final AnalyticsSolution data = new AnalyticsSolution(original, plan, pTaskName);
						data.open();
					}
				}
			};

			jobRunner.run(taskName, original, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);
		}
	}
}
