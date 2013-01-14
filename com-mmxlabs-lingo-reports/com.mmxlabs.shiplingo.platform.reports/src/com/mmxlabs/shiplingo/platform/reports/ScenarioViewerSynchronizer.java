/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.shiplingo.platform.reports.internal.Activator;

public class ScenarioViewerSynchronizer extends MMXAdapterImpl implements IScenarioServiceSelectionChangedListener {
	private static final Logger log = LoggerFactory.getLogger(ScenarioViewerSynchronizer.class);
	private Viewer viewer;
	private IScenarioServiceSelectionProvider selectionProvider;
	private HashSet<ScheduleModel> adaptees = new HashSet<ScheduleModel>();
	private IScenarioInstanceElementCollector collector;

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

		refreshViewer();
	}

	public void dispose() {
		this.viewer = null;
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(this);
			selectionProvider = null;
		}
		for (final ScheduleModel scheduleModel : adaptees) {
			scheduleModel.eAdapters().remove(this);
		}
	}

	@Override
	public void deselected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> deselected) {
		for (final ScenarioInstance instance : deselected) {
			final ScheduleModel scheduleModel = getScheduleModel(instance);
			if (scheduleModel != null) {
				scheduleModel.eAdapters().remove(this);
				adaptees.remove(scheduleModel);
			}
		}
		refreshViewer();
	}

	private ScheduleModel getScheduleModel(ScenarioInstance instance) {
		final IScenarioService scenarioService = instance.getScenarioService();
		if (scenarioService == null) {
			return null;
		}
		EObject object = null;
		try {
			object = scenarioService.load(instance);
			if (object instanceof MMXRootObject) {
				return ((MMXRootObject) object).getSubModel(ScheduleModel.class);
			}
		} catch (IOException e) {

		}

		return null;
	}

	@Override
	public void selected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> selected) {
		for (final ScenarioInstance instance : selected) {
			adaptInstance(instance);
		}
		refreshViewer();
	}

	private void adaptInstance(final ScenarioInstance instance) {
		final ScheduleModel scheduleModel = getScheduleModel(instance);
		if (scheduleModel != null) {
			scheduleModel.eAdapters().add(this);
			adaptees.add(scheduleModel);
		}
	}

	@Override
	public void reallyNotifyChanged(Notification notification) {
		if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_InitialSchedule() || notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_OptimisedSchedule()) {
			refreshViewer();
		}
	}

	@Override
	protected void missedNotifications(final List<Notification> notifications) {
		for (final Notification notification : notifications) {
			if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_InitialSchedule()
					|| notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_OptimisedSchedule()) {
				refreshViewer();
				return;
			}
		}
	}

	private boolean needsRefresh;

	private final Runnable refresh = new Runnable() {
		@Override
		public void run() {
			synchronized (this) {
				while (needsRefresh) {
					if (viewer != null) {
						final Control control = viewer.getControl();
						if (control != null && !control.isDisposed()) {
							final IScenarioViewerSynchronizerOutput data = collectObjects();
							viewer.setInput(data);
						}
					}
					needsRefresh = false;
				}
			}
		}
	};

	private void refreshViewer() {
		synchronized (this) {
			needsRefresh = true;
			Display.getDefault().asyncExec(refresh);
		}
	}

	private IScenarioViewerSynchronizerOutput collectObjects() {
		final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();

		final HashMap<Object, Pair<ScenarioInstance, MMXRootObject>> sourceByElement = new HashMap<Object, Pair<ScenarioInstance, MMXRootObject>>();
		final ArrayList<Object> selectedObjects = new ArrayList<Object>();
		final List<MMXRootObject> rootObjects = new ArrayList<MMXRootObject>();

		collector.beginCollecting();

		for (final ScenarioInstance job : selectionProvider.getSelection()) {
			final IScenarioService scenarioService = job.getScenarioService();
			final boolean isPinned = selectionProvider.getPinnedInstance() == job;
			if (scenarioService == null) {
				continue;
			}
			EObject instance = null;
			try {
				instance = scenarioService.load(job);
			} catch (IOException e) {
			}

			if (instance instanceof MMXRootObject) {
				final MMXRootObject rootObject = (MMXRootObject) instance;
				rootObjects.add(rootObject);
				final Collection<? extends Object> viewerContent = collector.collectElements(rootObject, isPinned);
				for (final Object o : viewerContent) {
					sourceByElement.put(o, new Pair<ScenarioInstance, MMXRootObject>(job, rootObject));
				}
				selectedObjects.addAll(viewerContent);
			}
		}

		collector.endCollecting();

		return new IScenarioViewerSynchronizerOutput() {
			@Override
			public ScenarioInstance getScenarioInstance(Object object) {
				return sourceByElement.get(object).getFirst();
			}

			@Override
			public MMXRootObject getRootObject(Object object) {
				return sourceByElement.get(object).getSecond();
			}

			@Override
			public Collection<Object> getCollectedElements() {
				return selectedObjects;
			}

			@Override
			public Collection<MMXRootObject> getRootObjects() {
				return rootObjects;
			}

			@Override
			public boolean isPinned(Object object) {
				return ScenarioViewerSynchronizer.this.selectionProvider.getPinnedInstance() == (getScenarioInstance(object));
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
	public static ScenarioViewerSynchronizer registerView(final Viewer c, IScenarioInstanceElementCollector collector) {
		return new ScenarioViewerSynchronizer(c, collector);
	}

	public static void deregisterView(final ScenarioViewerSynchronizer l) {
		l.dispose();
	}

	@Override
	public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin) {
		refreshViewer();
	}
}
