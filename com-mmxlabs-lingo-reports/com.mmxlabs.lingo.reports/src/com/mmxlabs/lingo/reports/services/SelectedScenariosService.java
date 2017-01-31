/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class SelectedScenariosService {
	private static final Logger log = LoggerFactory.getLogger(SelectedScenariosService.class);

	private final Map<ScenarioResult, MyCommandStackListener> commandStacks = new HashMap<>();
	private final Map<ScenarioResult, ModelReference> scenarioReferences = new HashMap<>();
	// private final Map<CommandStack, ScenarioResult> commandStackMap = new HashMap<>();
	// TODO: Create an explicitly remove/updateRecord method set to ensure record.dispose() is called.
	private final Map<ScenarioResult, KeyValueRecord> scenarioRecords = new HashMap<>();

	private IScenarioServiceSelectionProvider selectionProvider;

	private ISelectedDataProvider currentSelectedDataProvider;

	private final Set<ISelectedScenariosServiceListener> listeners = new HashSet<>();
	private final Map<IScenarioService, IScenarioServiceListener> unloadlisteners = new HashMap<>();

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
		private CommandStack commandStack;
		private ScenarioResult result;

		public MyCommandStackListener(CommandStack commandStack, ScenarioResult result) {
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
			final CommandStack commandStack = (CommandStack) event.getSource();
			final Command mostRecentCommand = commandStack.getMostRecentCommand();
			if (mostRecentCommand != null) {
				final Collection<?> result = mostRecentCommand.getResult();
				for (final Object o : result) {
					if (o == scheduleModel.get() || (o instanceof Schedule && ((Schedule) o).eContainer() == scheduleModel.get())) {
						updateSelectedScenarios(false);
						final KeyValueRecord record = scenarioRecords.remove(result);
						if (record != null) {
							record.dispose();
						}
						return;
					}
				}
			}
		}
	};

	private class OnUnloadScenarioServiceListener implements IScenarioServiceListener {

		@Override
		public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			detachScenarioInstance(scenarioInstance);
			updateSelectedScenarios(false);
		}

		@Override
		public void onPreScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			detachScenarioInstance(scenarioInstance);
			updateSelectedScenarios(false);
		}

		@Override
		public void onPostScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		}

		@Override
		public void onPostScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}
	};

	private final IScenarioServiceSelectionChangedListener scenarioServiceSelectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> selected, final boolean block) {
			for (final ScenarioResult instance : selected) {
				assert instance != null;
				attachScenarioInstance(instance);
			}
			// updateSelectedScenarios(block);
		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioResult oldPin, final ScenarioResult newPin, final boolean block) {
			// updateSelectedScenarios(block);
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> deselected, final boolean block) {
			for (final ScenarioResult instance : deselected) {
				assert instance != null;
				detachScenarioInstance(instance);
			}
			// updateSelectedScenarios(block);
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

	public void dispose() {
		if (this.selectionProvider != null) {
			unbindScenarioServiceSelectionProvider(this.selectionProvider);
		}
		for (final Map.Entry<IScenarioService, IScenarioServiceListener> e : unloadlisteners.entrySet()) {
			e.getKey().removeScenarioServiceListener(e.getValue());
		}
		listeners.clear();
	}

	private CommandStack getCommandStack(final ScenarioResult result) {
		final ScenarioInstance instance = result.getScenarioInstance();
		final IScenarioService scenarioService = instance.getScenarioService();
		if (scenarioService == null) {
			return null;
		}

		final Map<Class<?>, Object> adapters = instance.getAdapters();
		if (adapters != null) {
			final Object object = adapters.get(BasicCommandStack.class);
			if (object instanceof CommandStack) {
				return (CommandStack) object;
			}
		}

		return null;
	}

	private void attachScenarioInstance(@NonNull final ScenarioResult instance) {

		if (this.scenarioReferences.containsKey(instance)) {
			return;
		}
		final IScenarioService scenarioService = instance.getScenarioInstance().getScenarioService();
		if (!unloadlisteners.containsKey(scenarioService)) {
			final IScenarioServiceListener l = new OnUnloadScenarioServiceListener();
			unloadlisteners.put(scenarioService, l);
		}

		final CommandStack commandStack = getCommandStack(instance);
		if (commandStack != null) {
			MyCommandStackListener l = new MyCommandStackListener(commandStack, instance);
			this.commandStacks.put(instance, l);

		}
		this.scenarioReferences.put(instance, instance.getScenarioInstance().getReference("SelectedScenariosService:1"));
	}

	private void detachScenarioInstance(@NonNull final ScenarioInstance instance) {
		{
			final Iterator<Map.Entry<ScenarioResult, KeyValueRecord>> itr = scenarioRecords.entrySet().iterator();
			while (itr.hasNext()) {
				final Map.Entry<ScenarioResult, KeyValueRecord> e = itr.next();
				if (e.getKey().getScenarioInstance() == instance) {
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
				if (e.getKey().getScenarioInstance() == instance) {
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
			this.ref = scenarioResult.getScenarioInstance().getReference("SelectedScenariosService:2");
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
				ref = null;
			}
		};

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

	@Nullable
	private KeyValueRecord createKeyValueRecord(@NonNull final ScenarioResult scenarioResult) {

		try (ModelReference modelReference = scenarioResult.getScenarioInstance().getReference("SelectedScenariosService:3")) {
			final EObject instance = modelReference.getInstance();

			if (instance instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) instance;
				final ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);
				Schedule schedule = null;
				if (scheduleModel != null) {
					schedule = scheduleModel.getSchedule();
				}

				final Collection<EObject> children = new HashSet<>();
				final Iterator<EObject> itr = scenarioModel.eAllContents();
				while (itr.hasNext()) {
					children.add(itr.next());
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
		for (final ScenarioResult scenarioInstance : selectionProvider.getSelection()) {
			assert scenarioInstance != null;
			final KeyValueRecord record;
			if (scenarioRecords.containsKey(scenarioInstance)) {
				record = scenarioRecords.get(scenarioInstance);
			} else {
				record = createKeyValueRecord(scenarioInstance);
				final KeyValueRecord oldRecord = scenarioRecords.put(scenarioInstance, record);
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
}
