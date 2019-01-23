/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
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

	private static final Logger log = LoggerFactory.getLogger(InsertSlotContextMenuExtension.class);

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {

		if (slot instanceof SpotSlot) {
			return;
		}

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}

		if (slot.getCargo() == null) {
			final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, Collections.singletonList(slot));

			if (slot.isLocked()) {
				action.setEnabled(false);
				action.setText(action.getText() + " (Kept open)");
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
					action.setText(action.getText() + " (Kept open)");
				}
			}
			if (slot.getCargo().isLocked()) {
				action.setEnabled(false);
				action.setText(action.getText() + " (Locked)");
			}
			menuManager.add(action);
		}
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection selection, @NonNull final MenuManager menuManager) {

		if (!LicenseFeatures.isPermitted("features:options-suggester")) {
			return;
		}
		{
			List<Slot<?>> slots = new LinkedList<>();
			final Iterator<?> itr = selection.iterator();
			boolean noCargoSelected = true;
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof RowData) {
					final RowData rowData = (RowData) obj;
					final LoadSlot load = rowData.getLoadSlot();
					if (load != null) {
						if (load.getCargo() != null) {
							noCargoSelected = false;
						} else {
							slots.add(load);
						}
					}
					final DischargeSlot discharge = rowData.getDischargeSlot();
					if (discharge != null) {
						if (discharge.getCargo() != null) {
							noCargoSelected = false;
						} else {
							slots.add(discharge);
						}
					}
				}
				else	if (obj instanceof Row) {
					final Row rowData = (Row) obj;
					final LoadSlot load = rowData.getLoadSlot();
					if (load != null) {
						if (load.getCargo() != null) {
							noCargoSelected = false;
						} else {
							slots.add(load);
						}
					}
					final DischargeSlot discharge = rowData.getDischargeSlot();
					if (discharge != null) {
						if (discharge.getCargo() != null) {
							noCargoSelected = false;
						} else {
							slots.add(discharge);
						}
					}
				}
			}

			slots = filterSpotSlots(slots);

			if (noCargoSelected && !slots.isEmpty()) {
				if (slots.size() != 2 || (slots.size() == 2
						&& !(slots.stream().filter(s -> (s instanceof LoadSlot)).findAny().isPresent() && slots.stream().filter(s -> (s instanceof DischargeSlot)).findAny().isPresent()))) {
					slots = Lists.newArrayList(slots.get(0));
				}
				final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, slots);

				for (final Slot<?> slot : slots) {
					if (slot.isLocked()) {
						action.setEnabled(false);
						action.setText(action.getText() + " (Kept open)");
						break;
					}
				}
				menuManager.add(action);

				return;
			}
		}
		{
			final Set<Cargo> cargoes = new LinkedHashSet<>();
			final Iterator<?> itr = selection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof RowData) {
					final RowData rowData = (RowData) obj;
					final LoadSlot load = rowData.getLoadSlot();
					if (load != null && load.getCargo() != null) {
						cargoes.add(load.getCargo());
						break;
					}
					final DischargeSlot discharge = rowData.getDischargeSlot();
					if (discharge != null && discharge.getCargo() != null) {
						cargoes.add(discharge.getCargo());
						break;
					}
				}
			}

			if (cargoes.size() == 1) {
				final List<Slot<?>> slots = filterSpotSlots(cargoes.stream().flatMap(c -> c.getSortedSlots().stream()).collect(Collectors.toList()));
				if (slots.isEmpty()) {
					return;
				}
				final InsertSlotAction action = new InsertSlotAction(scenarioEditingLocation, slots);

				for (final Slot<?> slot : slots) {
					if (slot.isLocked()) {
						action.setEnabled(false);
						action.setText(action.getText() + " (Kept open)");
						break;
					}
				}
				for (final Cargo cargo : cargoes) {
					if (cargo.isLocked()) {
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
			userSettings.setCleanStateOptimisation(false);
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

						final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
						final AnalyticsSolution data = new AnalyticsSolution(original, plan, pTaskName);
						data.setCreateInsertionOptions(true);
						data.setCreateDiffToBaseAction(true);
						eventBroker.post(ChangeSetViewCreatorService_Topic, data);
					}
				}
			};

			jobRunner.run(taskName, original, originalModelRecord, null, createJobDescriptorCallback, jobCompletedCallback);

		}
	}
}
