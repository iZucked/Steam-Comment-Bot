/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

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

public class SelectedScenariosService {
	private static final Logger log = LoggerFactory.getLogger(SelectedScenariosService.class);

	private final Set<CommandStack> commandStacks = new HashSet<>();
	private final Map<ScenarioInstance, ModelReference> scenarioReferences = new HashMap<>();
	private final Map<CommandStack, ScenarioInstance> commandStackMap = new HashMap<>();
	// TODO: Create an explicitly remove/updateRecord method set to ensure record.dispose() is called.
	private final Map<ScenarioInstance, KeyValueRecord> scenarioRecords = new HashMap<>();

	private IScenarioServiceSelectionProvider selectionProvider;

	private ISelectedDataProvider currentSelectedDataProvider;

	private final Set<ISelectedScenariosServiceListener> listeners = new HashSet<>();

	/**
	 * Special counter to try and avoid multiple update requests happening at once. TODO: What happens if we hit Integer.MAX_VALUE?
	 */
	private AtomicInteger counter = new AtomicInteger();

	/**
	 * Command stack listener method, cause the linked viewer to refresh on command execution
	 * 
	 */
	private final CommandStackListener commandStackListener = new CommandStackListener() {

		@Override
		public void commandStackChanged(final EventObject event) {
			// Only react to changes involving the ScheduleModel
			final CommandStack commandStack = (CommandStack) event.getSource();
			final Command mostRecentCommand = commandStack.getMostRecentCommand();
			if (mostRecentCommand != null) {
				final Collection<?> result = mostRecentCommand.getResult();
				for (final Object o : result) {
					if (o instanceof ScheduleModel || o instanceof Schedule) {
						updateSelectedScenarios(false);
						KeyValueRecord record = scenarioRecords.remove(commandStackMap.get(commandStack));
						if (record != null) {
							record.dispose();
						}
						return;
					}
				}
			}
		}
	};

	private class OnLoadScenarioServiceListener implements IScenarioServiceListener {

		private final ScenarioInstance targetInstance;

		public OnLoadScenarioServiceListener(@NonNull final ScenarioInstance targetInstance) {
			this.targetInstance = targetInstance;
		}

		@Override
		public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			if (scenarioInstance == targetInstance) {
				scenarioService.removeScenarioServiceListener(this);
				detachScenarioInstance(scenarioInstance);
				updateSelectedScenarios(false);
			}
		}

		@Override
		public void onPreScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			if (scenarioInstance == targetInstance) {
				scenarioService.removeScenarioServiceListener(this);
				detachScenarioInstance(scenarioInstance);
				updateSelectedScenarios(false);
			}
		}

		@Override
		public void onPostScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		}

		@Override
		public void onPostScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			if (scenarioInstance == targetInstance) {
				detachScenarioInstance(scenarioInstance);
				attachScenarioInstance(scenarioInstance);
				scenarioService.removeScenarioServiceListener(this);
				updateSelectedScenarios(false);
			}
		}

		@Override
		public void onPostScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}
	};

	private final IScenarioServiceSelectionChangedListener scenarioServiceSelectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected, final boolean block) {
			for (final ScenarioInstance instance : selected) {
				assert instance != null;
				attachScenarioInstance(instance);
			}
			// updateSelectedScenarios(block);
		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin, final boolean block) {
			// updateSelectedScenarios(block);
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected, final boolean block) {
			for (final ScenarioInstance instance : deselected) {
				assert instance != null;
				detachScenarioInstance(instance);
			}
			// updateSelectedScenarios(block);
		}

		@Override
		public void selectionChanged(ScenarioInstance pinned, Collection<ScenarioInstance> others, boolean block) {
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
			for (final ScenarioInstance instance : this.selectionProvider.getSelection()) {
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
				final Map.Entry<ScenarioInstance, ModelReference> e = scenarioReferences.entrySet().iterator().next();
				final ScenarioInstance key = e.getKey();
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
	}

	private CommandStack getCommandStack(final ScenarioInstance instance) {
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

	private void attachScenarioInstance(@NonNull final ScenarioInstance instance) {

		if (this.scenarioReferences.containsKey(instance)) {
			return;
		}

		final CommandStack commandStack = getCommandStack(instance);
		if (commandStack != null) {
			commandStack.addCommandStackListener(this.commandStackListener);
			this.commandStacks.add(commandStack);
			commandStackMap.put(commandStack, instance);
		} else {
			// No command stack? Probably not loaded, so register a listener
			final IScenarioService scenarioService = instance.getScenarioService();
			if (scenarioService != null) {
				scenarioService.addScenarioServiceListener(new OnLoadScenarioServiceListener(instance));
			}
		}
		this.scenarioReferences.put(instance, instance.getReference());
	}

	private void detachScenarioInstance(@NonNull final ScenarioInstance instance) {
		final CommandStack commandStack = getCommandStack(instance);
		if (commandStack != null) {
			commandStack.removeCommandStackListener(this.commandStackListener);
			this.commandStacks.remove(commandStack);
			commandStackMap.remove(commandStack);
		}
		final ModelReference ref = scenarioReferences.remove(instance);
		if (ref != null) {
			ref.close();
		}
		final KeyValueRecord record = scenarioRecords.remove(instance);
		if (record != null) {
			record.dispose();
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

			final LinkedHashSet<ScenarioInstance> others = new LinkedHashSet<>(selectionProvider.getSelection());
			ScenarioInstance pinnedInstance = selectionProvider.getPinnedInstance();
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
		private final ScenarioInstance scenarioInstance;

		@NonNull
		private final LNGScenarioModel scenarioModel;

		@Nullable
		private final Schedule schedule;

		@NonNull
		private final Collection<EObject> children;

		private ModelReference ref;

		public KeyValueRecord(@NonNull final ScenarioInstance scenarioInstance, @NonNull final LNGScenarioModel scenarioModel, @Nullable final Schedule schedule,
				@NonNull final Collection<EObject> children) {
			this.scenarioInstance = scenarioInstance;
			this.scenarioModel = scenarioModel;
			this.schedule = schedule;
			this.children = children;
			this.ref = scenarioInstance.getReference();
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
		public ScenarioInstance getScenarioInstance() {
			return scenarioInstance;
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
	private KeyValueRecord createKeyValueRecord(@NonNull final ScenarioInstance scenarioInstance) {

		try (ModelReference modelReference = scenarioInstance.getReference()) {
			final EObject instance = modelReference.getInstance();

			if (instance instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) instance;
				final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
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
				return new KeyValueRecord(scenarioInstance, scenarioModel, schedule, children);
			}
		}
		return null;
	}

	@NonNull
	private SelectedDataProviderImpl createSelectedDataProvider() {

		final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
		for (final ScenarioInstance scenarioInstance : selectionProvider.getSelection()) {
			assert scenarioInstance != null;
			final KeyValueRecord record;
			if (scenarioRecords.containsKey(scenarioInstance)) {
				record = scenarioRecords.get(scenarioInstance);
			} else {
				record = createKeyValueRecord(scenarioInstance);
				KeyValueRecord oldRecord = scenarioRecords.put(scenarioInstance, record);
				if (oldRecord != null) {
					oldRecord.dispose();
				}
			}
			assert record != null;
			provider.addScenario(record.getScenarioInstance(), record.getScenarioModel(), record.getSchedule(), record.getChildren());
		}
		provider.setPinnedScenarioInstance(selectionProvider.getPinnedInstance());
		return provider;
	}

	public void triggerListener(@NonNull final ISelectedScenariosServiceListener l, final boolean block) {
		final SelectedDataProviderImpl selectedDataProvider = createSelectedDataProvider();

		final LinkedHashSet<ScenarioInstance> others = new LinkedHashSet<>(selectionProvider.getSelection());
		ScenarioInstance pinnedInstance = selectionProvider.getPinnedInstance();
		// If there is only the pinned scenario, pretend it is just selected.
		// If there is a pin and other scenarios, remove the pin from the others list
		if (others.size() < 2) {
			pinnedInstance = null;
		} else {
			others.remove(pinnedInstance);
		}
		try {
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
	public ScenarioInstance getPinnedScenario() {
		final IScenarioServiceSelectionProvider pProvider = selectionProvider;
		if (pProvider != null) {
			return pProvider.getPinnedInstance();
		}
		return null;
	}
}
