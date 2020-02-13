/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
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

public class InsertSlotContextMenuExtension implements ITradesTableContextMenuExtension {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final String LBL_KEPT_OPEN = "(Kept open)";
	private static final String LBL_LOCKED = "(Locked)";

	private static final Logger log = LoggerFactory.getLogger(InsertSlotContextMenuExtension.class);

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		if (slot instanceof SpotSlot) {
			return;
		}

		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIONISER)) {
			return;
		}

		if (slot.getCargo() == null) {
			final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, Collections.singletonList(slot));

			if (slot.isLocked()) {
				action.setEnabled(false);
				action.setText(String.format("%s %s", action.getText(), LBL_KEPT_OPEN));
			}

			menuManager.add(action);
			return;
		} else {
			final List<Slot<?>> slots = filterSpotSlots(slot.getCargo().getSortedSlots());
			if (slots.isEmpty()) {
				return;
			}

			final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, slots);

			for (final Slot<?> slot2 : slots) {
				if (slot2.isLocked()) {
					action.setEnabled(false);
					action.setText(String.format("%s %s", action.getText(), LBL_KEPT_OPEN));

				}
			}
			if (slot.getCargo().isLocked()) {
				action.setEnabled(false);
				action.setText(String.format("%s %s", action.getText(), LBL_LOCKED));

			}
			menuManager.add(action);
		}
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIONISER)) {
			return;
		}
		{
			List<Slot<?>> slots = new LinkedList<>();
			final Iterator<?> itr = selection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof RowData) {
					final RowData rowData = (RowData) obj;
					final LoadSlot load = rowData.getLoadSlot();
					if (load != null) {
						slots.add(load);
					}
					final DischargeSlot discharge = rowData.getDischargeSlot();
					if (discharge != null) {
						slots.add(discharge);
					}
				} else if (obj instanceof Row) {
					final Row rowData = (Row) obj;
					final LoadSlot load = rowData.getLoadSlot();
					if (load != null) {
						slots.add(load);
					}
					final DischargeSlot discharge = rowData.getDischargeSlot();
					if (discharge != null) {
						slots.add(discharge);
					}
				}
			}

			slots = filterSpotSlots(slots);

			if (!slots.isEmpty() && slots.size() <= 2) {
				final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, slots);

				for (final Slot<?> slot : slots) {
					if (slot.isLocked()) {
						action.setEnabled(false);
						action.setText(String.format("%s %s", action.getText(), LBL_KEPT_OPEN));

						break;
					}
				}
				menuManager.add(action);
			}
		}
	}

	private List<Slot<?>> filterSpotSlots(Collection<Slot<?>> slots) {
		final List<Slot<?>> l = new LinkedList<>();
		for (Slot<?> s : slots) {
			if (s instanceof SpotSlot) {
				continue;
			}
			l.add(s);
		}
		return l;
	}

	private static class InsertSlotAction extends Action {

		private final List<Slot<?>> targetSlots;
		private final IScenarioEditingLocation scenarioEditingLocation;

		public InsertSlotAction(final IScenarioEditingLocation scenarioEditingLocation, final List<Slot<?>> targetSlots) {
			super(AnalyticsSolutionHelper.generateInsertionName(targetSlots));
			this.scenarioEditingLocation = scenarioEditingLocation;
			this.targetSlots = targetSlots;
		}

		@Override
		public void run() {

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();

			final ScenarioInstance original = scenarioEditingLocation.getScenarioInstance();
			UserSettings userSettings = null;
			final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(original);
			String taskName = AnalyticsSolutionHelper.generateInsertionName(targetSlots);
			{

				try (final ModelReference modelReference = originalModelRecord.aquireReference("InsertSlotContextMenuExtension:1")) {

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
			final String pTaskName = taskName;
			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> {
				return new LNGSlotInsertionJobDescriptor(pTaskName, original, pUserSettings, targetSlots, Collections.emptyList());
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
