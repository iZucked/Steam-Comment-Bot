/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

public class ScenarioViewerSynchronizer implements IScenarioServiceSelectionChangedListener, CommandStackListener {
	private static final Logger log = LoggerFactory.getLogger(ScenarioViewerSynchronizer.class);
	private Viewer viewer;
	private IScenarioServiceSelectionProvider selectionProvider;
	private final Set<CommandStack> commandStacks = new HashSet<>();
	private final IScenarioInstanceElementCollector collector;

	private final Map<ScenarioInstance, ModelReference> scenarioReferenes = new HashMap<>();

	public ScenarioViewerSynchronizer(final Viewer viewer, final IScenarioInstanceElementCollector collector) {
		this.viewer = viewer;
		this.collector = collector;
		this.selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
		if (selectionProvider != null) {
			selectionProvider.addSelectionChangedListener(this);
			for (final ScenarioInstance instance : selectionProvider.getSelection()) {
				adaptInstance(instance);
			}
		} else {
			log.warn("Viewer synchronizer for " + viewer.getClass() + " didn't find a selection provider");
		}

		refreshViewer(false);
	}

	public void dispose() {
		this.viewer = null;
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(this);
			selectionProvider = null;
		}
		for (final CommandStack commandStack : commandStacks) {
			commandStack.removeCommandStackListener(this);
		}
	}

	@Override
	public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected, boolean block) {
		for (final ScenarioInstance instance : deselected) {
			final CommandStack commandStack = getCommandStack(instance);
			if (commandStack != null) {
				commandStack.removeCommandStackListener(this);
				commandStacks.remove(commandStack);
			}
			final ModelReference ref = scenarioReferenes.remove(instance);
			if (ref != null) {
				ref.close();
			}
		}
		refreshViewer(block);
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

	@Override
	public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected, boolean block) {
		for (final ScenarioInstance instance : selected) {
			adaptInstance(instance);
		}
		refreshViewer(block);
	}

	private void adaptInstance(final ScenarioInstance instance) {
		final CommandStack commandStack = getCommandStack(instance);
		if (commandStack != null) {
			commandStack.addCommandStackListener(this);
			commandStacks.add(commandStack);
		}
		scenarioReferenes.put(instance, instance.getReference());
	}

	private boolean needsRefresh;

	private final Runnable refresh = new Runnable() {
		@Override
		public void run() {
			synchronized (this) {
				while (needsRefresh) {
					try {
						if (viewer != null) {
							final Control control = viewer.getControl();
							if (control != null && !control.isDisposed()) {
								final IScenarioViewerSynchronizerOutput data = collectObjects();
								viewer.setInput(data);
							}
						}
					} finally {
						needsRefresh = false;
					}
				}
			}
		}
	};

	/**
	 */
	public void refreshViewer(boolean block) {
		synchronized (this) {
			needsRefresh = true;
			if (block) {
				Display.getDefault().syncExec(refresh);
			} else {
				Display.getDefault().asyncExec(refresh);
			}
		}
	}

	private IScenarioViewerSynchronizerOutput collectObjects() {
		final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();

		final HashMap<Object, Pair<ScenarioInstance, LNGScenarioModel>> sourceScenarioByElement = new HashMap<Object, Pair<ScenarioInstance, LNGScenarioModel>>();
		final HashMap<Object, Pair<ScenarioInstance, LNGPortfolioModel>> sourcePortfolioByElement = new HashMap<Object, Pair<ScenarioInstance, LNGPortfolioModel>>();
		final ArrayList<Object> selectedObjects = new ArrayList<Object>();
		final List<LNGScenarioModel> scenarioModels = new ArrayList<LNGScenarioModel>();
		final List<LNGPortfolioModel> portfolioModels = new ArrayList<LNGPortfolioModel>();
		final boolean hasPinnedScenario = selectionProvider.getPinnedInstance() != null;
		collector.beginCollecting();

		// Sort pinned scenario first.
		final List<ScenarioInstance> selectedInstances = new ArrayList<>(selectionProvider.getSelection());
		final ScenarioInstance pinnedInstance = selectionProvider.getPinnedInstance();
		if (pinnedInstance != null) {
			selectedInstances.remove(pinnedInstance);
			selectedInstances.add(0, pinnedInstance);
		}

		for (final ScenarioInstance job : selectedInstances) {
			final IScenarioService scenarioService = job.getScenarioService();
			final boolean isPinned = pinnedInstance == job;
			if (scenarioService == null) {
//				continue;
			}

			final ModelReference modelReference = scenarioReferenes.get(job);
			if (modelReference != null && !modelReference.getScenarioInstance().isLoadFailure()) {
				final EObject instance = modelReference.getInstance();

				if (instance instanceof LNGScenarioModel) {
					final LNGScenarioModel rootObject = (LNGScenarioModel) instance;
					scenarioModels.add(rootObject);
					portfolioModels.add(rootObject.getPortfolioModel());
					final Collection<? extends Object> viewerContent = collector.collectElements(job, rootObject, isPinned);
					for (final Object o : viewerContent) {
						sourceScenarioByElement.put(o, new Pair<ScenarioInstance, LNGScenarioModel>(job, rootObject));
						sourcePortfolioByElement.put(o, new Pair<ScenarioInstance, LNGPortfolioModel>(job, rootObject.getPortfolioModel()));
					}
					selectedObjects.addAll(viewerContent);
				}
			}
		}

		collector.endCollecting();

		return new IScenarioViewerSynchronizerOutput() {
			@Override
			public ScenarioInstance getScenarioInstance(final Object object) {
				return sourceScenarioByElement.get(object).getFirst();
			}

			@Override
			public LNGScenarioModel getLNGScenarioModel(final Object object) {
				return sourceScenarioByElement.get(object).getSecond();
			}

			@Override
			public LNGPortfolioModel getLNGPortfolioModel(final Object object) {
				return sourcePortfolioByElement.get(object).getSecond();
			}

			@Override
			public Collection<Object> getCollectedElements() {
				return selectedObjects;
			}

			@Override
			public Collection<LNGScenarioModel> getLNGScenarioModels() {
				return scenarioModels;
			}

			@Override
			public Collection<LNGPortfolioModel> getLNGPortfolioModels() {
				return portfolioModels;
			}

			@Override
			public boolean isPinned(final Object object) {
				return ScenarioViewerSynchronizer.this.selectionProvider.getPinnedInstance() == (getScenarioInstance(object));
			}

			@Override
			public boolean hasPinnedScenario() {
				return hasPinnedScenario;
			}
		};
	}

	/**
	 * Creates a {@link IJobManagerListener} to call {@link Viewer#setInput(Object)} when the currently selected {@link IManagedJob}s state or progress changes for jobs registered with the
	 * {@link IJobManager}. Call {@link #deregisterView(IJobManagerListener)} with the listener when it is no longer required. This method will call {@link Viewer#setInput(Object)} immediately with
	 * the current job selection.
	 * 
	 * @param c
	 * @return
	 */
	public static ScenarioViewerSynchronizer registerView(final Viewer c, final IScenarioInstanceElementCollector collector) {
		return new ScenarioViewerSynchronizer(c, collector);
	}

	public static void deregisterView(final ScenarioViewerSynchronizer l) {
		if (l != null) {
			l.dispose();
		}
	}

	@Override
	public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin, boolean block) {
		refreshViewer(block);
	}

	/**
	 * Command stack listener method, cause the linked viewer to refresh on command execution
	 * 
	 */
	@Override
	public void commandStackChanged(final EventObject event) {

		// Only react to changes involving the ScheduleModel
		final CommandStack commandStack = (CommandStack) event.getSource();
		final Command mostRecentCommand = commandStack.getMostRecentCommand();
		if (mostRecentCommand != null) {
			final Collection<?> result = mostRecentCommand.getResult();
			for (final Object o : result) {
				if (o instanceof ScheduleModel || o instanceof Schedule) {
					refreshViewer(false);
					return;
				}
			}
		}
	}
}
