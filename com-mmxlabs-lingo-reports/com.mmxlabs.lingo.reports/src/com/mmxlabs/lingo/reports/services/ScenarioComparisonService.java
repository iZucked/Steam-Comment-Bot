/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class ScenarioComparisonService implements IScenarioServiceSelectionProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioComparisonService.class);

	private final WeakHashMap<CommandStack, MyCommandStackListener> commandStacks = new WeakHashMap<>();

	private SelectedDataProviderImpl currentSelectedDataProvider = new SelectedDataProviderImpl();

	private final Collection<ISelectedScenariosServiceListener> listeners = new ConcurrentLinkedQueue<>();
	private final Collection<IScenarioServiceSelectionChangedListener> selectedScenariosListeners = new ConcurrentLinkedQueue<>();

	/**
	 * Special counter to avoid multiple update requests happening at once.
	 */
	private final AtomicInteger counter = new AtomicInteger();

	private AtomicBoolean inSelectionChanged = new AtomicBoolean(false);

	private final DiffOptions diffOptions = ScheduleReportFactory.eINSTANCE.createDiffOptions();

	/** Keep reference to the selection service for the stop() method. */
	private ESelectionService selectionService;

	@Override
	public void select(final ScenarioResult other, final boolean block) {
		if (currentSelectedDataProvider.getAllScenarioResults().contains(other)) {
			return;
		}

		if (currentSelectedDataProvider.getPinnedScenarioResult() != null) {
			scheduleRebuildCompare(currentSelectedDataProvider.getPinnedScenarioResult(), Collections.singleton(other), null, block);
		} else {
			final List<ScenarioResult> others = new LinkedList<>(currentSelectedDataProvider.getOtherScenarioResults());
			others.add(other);
			scheduleRebuildCompare(null, others, null, block);
		}
	}

	public void select(final Collection<ScenarioResult> others, final boolean block) {
		scheduleRebuildCompare(null, others, null, block);

	}

	@Override
	public void setPinned(final ScenarioResult pin, final boolean block) {
		final List<ScenarioResult> others = new LinkedList<>(currentSelectedDataProvider.getAllScenarioResults());
		if (pin != null) {
			others.remove(pin);
		}

		scheduleRebuildCompare(pin, others, null, block);

	}

	@Override
	public void setPinnedPair(final ScenarioResult pin, final ScenarioResult other, final boolean block) {
		scheduleRebuildCompare(pin, Collections.singleton(other), null, block);
	}

	public void setPinnedPair(final ScenarioResult pin, final ScenarioResult other, final ISelection selection, final boolean block) {
		scheduleRebuildCompare(pin, Collections.singleton(other), selection, block);
	}

	@Override
	public void deselect(final ScenarioResult other, final boolean block) {

		final List<ScenarioResult> others = new LinkedList<>(currentSelectedDataProvider.getOtherScenarioResults());
		if (currentSelectedDataProvider.getPinnedScenarioResult() == other) {
			scheduleRebuildCompare(null, others, null, block);
		} else {
			if (others.contains(other)) {
				others.remove(other);
				scheduleRebuildCompare(currentSelectedDataProvider.getPinnedScenarioResult(), others, null, block);
			}
		}

	}

	@Override
	public void deselect(final ScenarioInstance instance, final boolean block) {
		final Set<ScenarioResult> toRemove = new HashSet<>();
		for (final var r : currentSelectedDataProvider.getAllScenarioResults()) {
			if (r.getScenarioInstance() == instance) {
				toRemove.add(r);
			}
		}
		if (!toRemove.isEmpty()) {
			ScenarioResult pinned = currentSelectedDataProvider.getPinnedScenarioResult();
			if (toRemove.contains(pinned)) {
				pinned = null;
			}
			final List<ScenarioResult> others = new LinkedList<>(currentSelectedDataProvider.getOtherScenarioResults());
			others.removeAll(toRemove);
			scheduleRebuildCompare(pinned, others, null, block);
		}
	}

	@Override
	public void deselectAll(final boolean block) {
		scheduleRebuildCompare(null, Collections.emptyList(), null, block);
	}

	@Override
	public Collection<ScenarioResult> getSelection() {
		return currentSelectedDataProvider.getAllScenarioResults();
	}

	@Override
	public @Nullable ScenarioResult getPinned() {
		return currentSelectedDataProvider.getPinnedScenarioResult();
	}

	@Override
	public boolean isSelected(@NonNull final ScenarioResult scenarioResult) {
		return currentSelectedDataProvider.getAllScenarioResults().contains(scenarioResult);
	}

	@Override
	public boolean isSelected(@NonNull final ScenarioInstance instance) {

		for (final var r : currentSelectedDataProvider.getAllScenarioResults()) {
			if (r.getScenarioInstance() == instance) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPinned(@NonNull final ScenarioResult result) {
		return currentSelectedDataProvider.getPinnedScenarioResult() == result;
	}

	@Override
	public boolean isPinned(@NonNull final ScenarioInstance instance) {
		final ScenarioResult pinned = currentSelectedDataProvider.getPinnedScenarioResult();
		return pinned != null && pinned.getScenarioInstance() == instance;
	}

	@Override
	public void addSelectionChangedListener(final IScenarioServiceSelectionChangedListener listener) {
		selectedScenariosListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(final IScenarioServiceSelectionChangedListener listener) {
		selectedScenariosListeners.remove(listener);
	}

	private synchronized void scheduleRebuildCompare(final ScenarioResult pinned, final Collection<ScenarioResult> others, final ISelection selection, final boolean block) {

		// If we have too many other scenarios, then deselect *all* the others. We could pick an arbitrary one,
		// but that could lead to inconsistent UI behaviour

		if (pinned != null && others.size() > 1) {
			others.clear();
		}

		final int value = counter.incrementAndGet();
		if (PlatformUI.isWorkbenchRunning()) {
			RunnerHelper.exec(() -> doRebuildCompare(value, pinned, others, selection, block), block);
		}
	}

	private synchronized void scheduleSelectionRefresh(final ISelection selection, final boolean block) {
		final int value = counter.get();
		if (PlatformUI.isWorkbenchRunning()) {
			RunnerHelper.exec(() -> {
				if (value != counter.get()) {
					return;
				}
				if (inSelectionChanged.compareAndSet(false, true)) {
					try {

						currentSelectedDataProvider.setSelectedObjects(null, selection);

						for (final ISelectedScenariosServiceListener l : listeners) {

							try {
								l.selectedObjectChanged(null, selection);
							} catch (final Exception e) {
								LOGGER.error(e.getMessage(), e);
							}
						}
						PlatformUI.getWorkbench().getService(ESelectionService.class).setPostSelection(selection);
					} catch (final Exception e) {
						LOGGER.error(e.getMessage(), e);
					} finally {
						inSelectionChanged.set(false);
					}
				}
			}, block);
		}

	}

	private final Consumer<ScenarioResult> attachChangeListener = scenarioResult -> {
		final CommandStack commandStack = scenarioResult.getScenarioDataProvider().getCommandStack();
		synchronized (commandStacks) {
			commandStacks.computeIfAbsent(commandStack, MyCommandStackListener::new).add(scenarioResult);
		}

	};

	private void doRebuildCompare(final int value, //
			final @Nullable ScenarioResult pinned, final Collection<ScenarioResult> others, //
			final @Nullable ISelection selection, final boolean block) {
		if (value != counter.get()) {
			return;
		}
		final SelectedDataProviderImpl selectedDataProvider = new SelectedDataProviderImpl();
		if (pinned != null) {
			selectedDataProvider.addScenario(pinned);
			selectedDataProvider.setPinnedScenarioInstance(pinned);
			attachChangeListener.accept(pinned);

		}
		others.forEach(sr -> {
			attachChangeListener.accept(sr);
			selectedDataProvider.addScenario(sr);
		});

		selectedDataProvider.setSelectedObjects(null, selection);

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			// Where to get this from?
			final boolean diffToBase = false;
			ChangeSetTableRoot root = null;
			ChangeSetTableGroup set = null;
			final List<ChangeSetTableRow> rows = new LinkedList<>();

			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				Object o = itr.next();
				if (o instanceof ChangeSetTableRow) {
					final ChangeSetTableRow r = (ChangeSetTableRow) o;
					rows.add(r);
					o = r.eContainer();
				}
				if (o instanceof ChangeSetTableGroup) {
					final ChangeSetTableGroup s = (ChangeSetTableGroup) o;
					set = s;
					o = s.eContainer();
				}
				if (o instanceof ChangeSetTableRoot) {
					final ChangeSetTableRoot r = (ChangeSetTableRoot) o;
					root = r;
				}
			}
			selectedDataProvider.setDiffToBase(diffToBase);
			selectedDataProvider.setChangeSetRoot(root);
			selectedDataProvider.setChangeSet(set);
			selectedDataProvider.setChangeSetRows(rows);
		}

		// TODO: Add in other data elements to

		if (value != counter.get()) {
			return;
		}
		currentSelectedDataProvider = selectedDataProvider;
		for (final IScenarioServiceSelectionChangedListener listener : selectedScenariosListeners) {
			try {
				listener.selectionChanged(pinned, others);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		for (final ISelectedScenariosServiceListener l : listeners) {
			try {
				l.selectedDataProviderChanged(selectedDataProvider, block);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void addListener(@NonNull final ISelectedScenariosServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(@NonNull final ISelectedScenariosServiceListener listener) {
		listeners.remove(listener);
	}

	public void triggerListener(@NonNull final ISelectedScenariosServiceListener l, final boolean block) {
		try {
			l.selectedDataProviderChanged(currentSelectedDataProvider, block);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public ISelectedDataProvider getCurrentSelectedDataProvider() {
		return currentSelectedDataProvider;
	}

	public void setSelection(final ISelection newSelection) {
		scheduleSelectionRefresh(newSelection, false);
	}

	public DiffOptions getDiffOptions() {
		return diffOptions;
	}

	public void toggleDiffOption(final EDiffOption option) {

		boolean newValue = false;
		if (option == EDiffOption.FILTER_SCHEDULE_CHART_BY_SELECTION) {
			diffOptions.setFilterSelectedSequences(!diffOptions.isFilterSelectedSequences());
			newValue = diffOptions.isFilterSelectedSequences();
		}
		if (option == EDiffOption.FILTER_SCHEDULE_SUMMARY_BY_SELECTION) {
			diffOptions.setFilterSelectedElements(!diffOptions.isFilterSelectedElements());
			newValue = diffOptions.isFilterSelectedElements();
		}

		for (final ISelectedScenariosServiceListener l : listeners) {
			try {
				l.diffOptionChanged(option, !newValue, newValue);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Command stack listener method, cause the linked viewer to refresh on command execution
	 *
	 */
	private class MyCommandStackListener implements CommandStackListener {

		private final List<WeakReference<ScenarioResult>> targets = new LinkedList<>();

		public MyCommandStackListener(final CommandStack commandStack) {
			commandStack.addCommandStackListener(this);
		}

		public void add(final ScenarioResult result) {
			targets.add(new WeakReference<>(result));
		}

		@Override
		public void commandStackChanged(final EventObject event) {

			// Find target Schedule models which may have changed via a command. We only care about the top level objects as we don't expect to change a Schedule instance once attached to the scenario
			final Set<Object> e = new HashSet<>();
			for (final var ref : targets) {
				final ScenarioResult sr = ref.get();
				if (sr != null) {

					final ScheduleModel scheduleModel = sr.getTypedResult(ScheduleModel.class);
					e.add(scheduleModel);

					final Schedule schedule = scheduleModel.getSchedule();

					if (schedule != null) {
						e.add(schedule);
					}
				}

			}
			// Quick prune of old entries...
			targets.removeIf(ref -> ref.get() == null);

			// No valid targets? return early
			if (e.isEmpty()) {
				return;
			}

			// Only react to changes involving the ScheduleModel
			final CommandStack eventCommandStack = (CommandStack) event.getSource();
			final Command mostRecentCommand = eventCommandStack.getMostRecentCommand();
			if (mostRecentCommand != null) {
				final Collection<?> commandResult = mostRecentCommand.getResult();
				for (final Object o : commandResult) {
					if (e.contains(o)) {
						// We found at least one match, so trigger a rebuild.
						// Note: We forget selection etc here
						scheduleRebuildCompare(currentSelectedDataProvider.getPinnedScenarioResult(), currentSelectedDataProvider.getOtherScenarioResults(), null, false);
						return;
					}
				}
			}
		}
	}

	private final ISelectionListener selectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final MPart part, final Object selectedObject) {
			if (SelectionServiceUtils.isSelectionValid(part, selectedObject)) {
				// Avoid re-entrant selection changes.
				if (inSelectionChanged.compareAndSet(false, true)) {
					try {
						final ISelection selection = SelectionHelper.adaptSelection(selectedObject);
						setSelection(selection);
					} finally {
						inSelectionChanged.set(false);
					}
				}
			}
		}

	};

	public void start() {
		WellKnownTriggers.WORKSPACE_STARTED.delayUntilTriggered(() -> {
			// #stop() can be called before the workbench has been started. This can happen due to the automem workbench restart. This is a second vm instance so there is no danger of two copies of
			// this service running at once due to *this* specific situation.
			selectionService = PlatformUI.getWorkbench().getService(ESelectionService.class);
			selectionService.addPostSelectionListener(selectionListener);
		});
	}

	public void stop() {
		if (selectionService != null) {
			selectionService.removePostSelectionListener(selectionListener);
		}
		selectionService = null;
	}

	public synchronized void updateActiveEditorScenario(@Nullable ScenarioInstance newInstance, @Nullable ScenarioInstance oldInstance, boolean block) {

		// Same instance? No need to update
		if (oldInstance == newInstance) {
			return;
		}

		final List<ScenarioResult> others = new LinkedList<>(currentSelectedDataProvider.getOtherScenarioResults());

		if (oldInstance != null) {
			others.removeIf(r -> r.getScenarioInstance() == oldInstance);
		}

		if (newInstance != null) {
			try {
				// This line may fail if model cannot be loaded. Wrap everything up in exception handler
				final ScenarioResult scenarioResult = new ScenarioResultImpl(newInstance);
				if (!others.contains(scenarioResult)) {
					others.add(scenarioResult);
				}
			} catch (final Exception e) {
				// We don't care here, load failures will be reported elsewhere
			}
		}

		scheduleRebuildCompare(currentSelectedDataProvider.getPinnedScenarioResult(), others, null, block);
	}
}
