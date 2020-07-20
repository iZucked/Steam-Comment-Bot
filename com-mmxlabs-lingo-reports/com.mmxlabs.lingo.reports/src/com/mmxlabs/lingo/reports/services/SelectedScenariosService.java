/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.SSDataManager.PostChangeHookPhase;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class SelectedScenariosService {
	private static final Logger log = LoggerFactory.getLogger(SelectedScenariosService.class);

	private final Map<ScenarioResult, MyCommandStackListener> commandStacks = new HashMap<>();
	private final Map<ScenarioResult, ModelReference> scenarioReferences = new HashMap<>();
	// TODO: Create an explicitly remove/updateRecord method set to ensure
	// record.dispose() is called.
	private final Map<ScenarioResult, KeyValueRecord> scenarioRecords = new HashMap<>();

	private IScenarioServiceSelectionProvider selectionProvider;

	private ISelectedDataProvider currentSelectedDataProvider;

	private final Set<ISelectedScenariosServiceListener> listeners = new HashSet<>();

	/**
	 * Special counter to try and avoid multiple update requests happening at once. TODO: What happens if we hit Integer.MAX_VALUE?
	 */
	private final AtomicInteger counter = new AtomicInteger();

	/**
	 * Command stack listener method, cause the linked viewer to refresh on command execution
	 * 
	 */
	private class MyCommandStackListener implements CommandStackListener {
		private final WeakReference<ScheduleModel> scheduleModel;
		private final CommandStack commandStack;
		private final ScenarioResult result;

		public MyCommandStackListener(final CommandStack commandStack, final ScenarioResult result) {
			this.commandStack = commandStack;
			this.result = result;
			this.scheduleModel = new WeakReference<ScheduleModel>(result.getTypedResult(ScheduleModel.class));
			commandStack.addCommandStackListener(this);

		}

		public void dispose() {
			commandStack.removeCommandStackListener(this);
		}

		@Override
		public void commandStackChanged(final EventObject event) {
			// Only react to changes involving the ScheduleModel
			final CommandStack eventCommandStack = (CommandStack) event.getSource();
			final Command mostRecentCommand = eventCommandStack.getMostRecentCommand();
			if (mostRecentCommand != null) {
				final Collection<?> commandResult = mostRecentCommand.getResult();
				for (final Object o : commandResult) {
					if (o == scheduleModel.get() || (o instanceof Schedule && ((Schedule) o).eContainer() == scheduleModel.get())) {
						final KeyValueRecord record = scenarioRecords.remove(result);
						if (record != null) {
							record.dispose();
						}
						updateSelectedScenarios(false);
						return;
					}
				}
			}
		}
	}

	private final @NonNull IScenarioServiceSelectionChangedListener scenarioServiceSelectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> selected, final boolean block) {
			for (final ScenarioResult instance : selected) {
				if (instance != null) {
					attachScenarioInstance(instance);
				}
			}
		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioResult oldPin, final ScenarioResult newPin, final boolean block) {
			// Do nothing
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> deselected, final boolean block) {
			for (final ScenarioResult instance : deselected) {
				if (instance != null) {
					detachScenarioInstance(instance);
				}
			}
		}

		@Override
		public void selectionChanged(final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			updateSelectedScenarios(block);
		}
	};

	public void bindScenarioServiceSelectionProvider(@Nullable final IScenarioServiceSelectionProvider selectionProvider) {
		if (this.selectionProvider != null) {
			unbindScenarioServiceSelectionProvider(this.selectionProvider);
		}

		this.selectionProvider = selectionProvider;

		if (this.selectionProvider != null) {
			this.selectionProvider.addSelectionChangedListener(scenarioServiceSelectionChangedListener);
			// Get initial selection
			for (final ScenarioResult instance : this.selectionProvider.getSelection()) {
				assert instance != null;
				attachScenarioInstance(instance);
			}
		}

		updateSelectedScenarios(false);
	}

	public void unbindScenarioServiceSelectionProvider(@Nullable final IScenarioServiceSelectionProvider selectionProvider) {
		if (this.selectionProvider == null) {
			return;
		}
		if (this.selectionProvider == selectionProvider) {
			this.selectionProvider.removeSelectionChangedListener(scenarioServiceSelectionChangedListener);
			this.selectionProvider = null;

			// Clean up scenarios.
			while (!scenarioReferences.isEmpty()) {
				final Map.Entry<ScenarioResult, ModelReference> e = scenarioReferences.entrySet().iterator().next();
				final ScenarioResult key = e.getKey();
				assert key != null;
				detachScenarioInstance(key);
			}
		}
		assert commandStacks.isEmpty();
	}

	IPostChangeHook unloadHook = modelRecord -> {
		detachScenarioInstance(modelRecord);
		updateSelectedScenarios(false);
	};

	public void init() {
		SSDataManager.Instance.registerChangeHook(unloadHook, PostChangeHookPhase.ON_UNLOAD);

	}

	public void dispose() {
		if (this.selectionProvider != null) {
			unbindScenarioServiceSelectionProvider(this.selectionProvider);
		}

		SSDataManager.Instance.removeChangeHook(unloadHook, PostChangeHookPhase.ON_UNLOAD);

		listeners.clear();
	}

	private void attachScenarioInstance(@NonNull final ScenarioResult scenarioResult) {

		if (this.scenarioReferences.containsKey(scenarioResult)) {
			return;
		}

		@NonNull
		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
		final ModelReference modelReference = modelRecord.aquireReference("SelectedScenariosService:1");

		final CommandStack commandStack = modelReference.getCommandStack();
		if (commandStack != null) {
			final MyCommandStackListener l = new MyCommandStackListener(commandStack, scenarioResult);
			this.commandStacks.put(scenarioResult, l);

		}
		this.scenarioReferences.put(scenarioResult, modelReference);
	}

	private void detachScenarioInstance(@NonNull final ScenarioModelRecord modelRecord) {
		{
			final Iterator<Map.Entry<ScenarioResult, KeyValueRecord>> itr = scenarioRecords.entrySet().iterator();
			while (itr.hasNext()) {
				final Map.Entry<ScenarioResult, KeyValueRecord> e = itr.next();
				if (e.getKey().getModelRecord() == modelRecord) {
					final KeyValueRecord record = e.getValue();
					if (record != null) {
						record.dispose();
					}
					itr.remove();
				}
			}
		}
		{
			final Iterator<Map.Entry<ScenarioResult, ModelReference>> itr = scenarioReferences.entrySet().iterator();
			while (itr.hasNext()) {
				final Map.Entry<ScenarioResult, ModelReference> e = itr.next();
				if (e.getKey().getModelRecord() == modelRecord) {
					final ModelReference ref = e.getValue();
					if (ref != null) {
						ref.close();
					}
					itr.remove();
				}
			}
		}

	}

	private void detachScenarioInstance(@NonNull final ScenarioResult result) {

		// FAIL! This is null, put store with the scenario instance.

		final MyCommandStackListener l = commandStacks.remove(result);
		if (l != null) {
			l.dispose();
		}

		final KeyValueRecord record = scenarioRecords.remove(result);
		if (record != null) {
			record.dispose();
		}
		final ModelReference ref = scenarioReferences.remove(result);
		if (ref != null) {
			ref.close();
		}
	}

	public void addListener(@NonNull final ISelectedScenariosServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(@NonNull final ISelectedScenariosServiceListener listener) {
		listeners.remove(listener);
	}

	private void updateSelectedScenarios(final boolean block) {
		// Null out until new version is ready
		currentSelectedDataProvider = null;
		final int value = counter.incrementAndGet();
		if (PlatformUI.isWorkbenchRunning()) {

			// Viewer helper!
			RunnerHelper.exec(() -> {
				// Mismatch, assume pending job
				if (value != counter.get()) {
					return;
				}
				doUpdateSelectedScenarios(value, block);
			}, block);
		}
	}

	/**
	 */
	public void doUpdateSelectedScenarios(final int value, final boolean block) {
		synchronized (this) {
			final ISelectedDataProvider selectedDataProvider = createSelectedDataProvider();

			final LinkedHashSet<ScenarioResult> others = new LinkedHashSet<>(selectionProvider.getSelection());
			ScenarioResult pinnedInstance = selectionProvider.getPinnedInstance();
			// If there is only the pinned scenario, pretend it is just selected.
			// If there is a pin and other scenarios, remove the pin from the others list
			if (others.size() < 2) {
				pinnedInstance = null;
			} else {
				others.remove(pinnedInstance);
			}

			currentSelectedDataProvider = selectedDataProvider;
			for (final ISelectedScenariosServiceListener l : listeners) {
				try {
					l.selectionChanged(currentSelectedDataProvider, pinnedInstance, others, block);
				} catch (final Exception e) {
					if (value == counter.get()) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private static final class KeyValueRecord {
		@NonNull
		private final ScenarioResult scenarioResult;

		@NonNull
		private final LNGScenarioModel scenarioModel;

		@Nullable
		private final Schedule schedule;

		@NonNull
		private final Collection<EObject> children;

		private ModelReference ref;

		public KeyValueRecord(@NonNull final ScenarioResult scenarioResult, @NonNull final LNGScenarioModel scenarioModel, @Nullable final Schedule schedule,
				@NonNull final Collection<EObject> children) {
			this.scenarioResult = scenarioResult;
			this.scenarioModel = scenarioModel;
			this.schedule = schedule;
			this.children = children;
			this.ref = scenarioResult.getModelRecord().aquireReference("SelectedScenariosService:2");
		}

		public void dispose() {
			if (ref != null) {
				ref.close();
				ref = null;
			}

		}

		@Override
		protected void finalize() throws Throwable {
			if (ref != null) {
				ref.close();
			}
			super.finalize();
		}

		@NonNull
		public ScenarioResult getScenarioResult() {
			return scenarioResult;
		}

		@NonNull
		public LNGScenarioModel getScenarioModel() {
			return scenarioModel;
		}

		@Nullable
		public Schedule getSchedule() {
			return schedule;
		}

		@NonNull
		public Collection<EObject> getChildren() {
			return children;
		}
	}

	/**
	 * 
	 * @param scenarioResult
	 * @return
	 */
	@Nullable
	private static KeyValueRecord createKeyValueRecord(@NonNull final ScenarioResult scenarioResult) {

		@NonNull
		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
		try (ModelReference modelReference = modelRecord.aquireReference("SelectedScenarioService:3")) {
			final EObject instance = modelReference.getInstance();

			if (instance instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) instance;
				final ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);
				Schedule schedule = null;
				if (scheduleModel != null) {
					schedule = scheduleModel.getSchedule();
				}

				final Collection<EObject> children = new HashSet<>();
				{
					final TreeIterator<EObject> itr = scenarioModel.eAllContents();
					while (itr.hasNext()) {
						EObject next = itr.next();
						if (next instanceof ScheduleModel) {
							itr.prune();
						} else {
							children.add(next);
						}
					}
				}
				if (scheduleModel != null) {
					final TreeIterator<EObject> itr = scheduleModel.eAllContents();
					while (itr.hasNext()) {
						EObject next = itr.next();
						children.add(next);
					}
				}
				children.add(scenarioModel);
				return new KeyValueRecord(scenarioResult, scenarioModel, schedule, children);
			}
		}
		return null;

	}

	@NonNull
	private SelectedDataProviderImpl createSelectedDataProvider() {

		final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
		for (final ScenarioResult scenarioResult : selectionProvider.getSelection()) {
			if (scenarioResult == null) {
				continue;
			}
			final KeyValueRecord record;
			// Cannot reuse existing record as we do not know if it has changed or not.
			// If we re-evaluate, the ScenarioResult is the same, the ScenarioInstance is
			// the same and the ScheduleModel is the same!
			if (false && scenarioRecords.containsKey(scenarioResult)) {
				record = scenarioRecords.get(scenarioResult);
			} else {
				try {
					record = createKeyValueRecord(scenarioResult);
				} catch (Exception e) {
					// Failed to load - this can happen when the scenario is removed after getting
					// the current selection.
					continue;
				}
				final KeyValueRecord oldRecord = scenarioRecords.put(scenarioResult, record);
				if (oldRecord != null) {
					oldRecord.dispose();
				}
			}
			assert record != null;
			provider.addScenario(record.getScenarioResult(), record.getSchedule(), record.getChildren());
		}
		provider.setPinnedScenarioInstance(selectionProvider.getPinnedInstance());
		return provider;
	}

	public void triggerListener(@NonNull final ISelectedScenariosServiceListener l, final boolean block) {
		try {
			final SelectedDataProviderImpl selectedDataProvider = createSelectedDataProvider();

			final LinkedHashSet<ScenarioResult> others = new LinkedHashSet<>(selectionProvider.getSelection());
			ScenarioResult pinnedInstance = selectionProvider.getPinnedInstance();
			// If there is only the pinned scenario, pretend it is just selected.
			// If there is a pin and other scenarios, remove the pin from the others list
			if (others.size() < 2) {
				pinnedInstance = null;
			} else {
				others.remove(pinnedInstance);
			}

			l.selectionChanged(selectedDataProvider, pinnedInstance, others, block);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Nullable
	public ISelectedDataProvider getCurrentSelectedDataProvider() {
		return currentSelectedDataProvider;
	}

	/**
	 * Wrapped around {@link IScenarioServiceSelectionProvider#getPinnedInstance()}
	 * 
	 * @return The current pinned instance or null
	 */
	@Nullable
	public ScenarioResult getPinnedScenario() {
		final IScenarioServiceSelectionProvider pProvider = selectionProvider;
		if (pProvider != null) {
			return pProvider.getPinnedInstance();
		}
		return null;
	}

	public static ISelectedDataProvider createTestingSelectedDataProvider(final @NonNull ScenarioResult pin, final @NonNull ScenarioResult other) {

		final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
		{
			final KeyValueRecord record = createKeyValueRecord(pin);
			if (record != null) {
				provider.addScenario(record.getScenarioResult(), record.getSchedule(), record.getChildren());
				provider.setPinnedScenarioInstance(pin);
			}
		}
		{
			final KeyValueRecord record = createKeyValueRecord(other);
			if (record != null) {
				provider.addScenario(record.getScenarioResult(), record.getSchedule(), record.getChildren());
			}

		}
		return provider;
	}
}
