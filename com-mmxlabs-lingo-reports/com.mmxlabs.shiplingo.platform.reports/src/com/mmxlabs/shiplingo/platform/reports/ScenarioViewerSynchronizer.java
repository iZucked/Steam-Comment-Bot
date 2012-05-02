/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

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

public class ScenarioViewerSynchronizer extends MMXAdapterImpl implements IScenarioServiceSelectionChangedListener {
	private Viewer viewer;
	private IScenarioServiceSelectionProvider selectionProvider;
	private HashSet<ScheduleModel> adaptees = new HashSet<ScheduleModel>();
	private IScenarioInstanceElementCollector collector;
	
	public ScenarioViewerSynchronizer(final Viewer viewer, final IScenarioInstanceElementCollector collector) {
		this.viewer = viewer;
		this.collector = collector;
		this.selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
		if (selectionProvider != null)
			selectionProvider.addSelectionChangedListener(this);
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
			final ScheduleModel scheduleModel = getScheduleModel(instance);
			if (scheduleModel != null) {
				scheduleModel.eAdapters().add(this);
				adaptees.add(scheduleModel);
			}
		}
		refreshViewer();
	}

	@Override
	public void reallyNotifyChanged(Notification notification) {
		if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_InitialSchedule() || notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_OptimisedSchedule()) {
			refreshViewer();
		}
	}

	@Override
	protected void missedNotification() {
		refreshViewer();
	}

	private boolean needsRefresh;
	
	private final Runnable refresh = new Runnable() {
		@Override
		public void run() {
			synchronized (this) {
				while (needsRefresh) {
					final IScenarioViewerSynchronizerOutput data = collectObjects();
					viewer.setInput(data);
					needsRefresh = false;
				}
			}
		}		
	};

	private void refreshViewer() {
		needsRefresh = true;
		Display.getDefault().asyncExec(refresh);
	}
	
	private IScenarioViewerSynchronizerOutput collectObjects() {
		final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
		
		final HashMap<Object, Pair<ScenarioInstance, MMXRootObject>> sourceByElement = new HashMap<Object, Pair<ScenarioInstance, MMXRootObject>>();
		final ArrayList<Object> selectedObjects = new ArrayList<Object>();
		for (final ScenarioInstance job : selectionProvider.getSelection()) {
			final IScenarioService scenarioService = (IScenarioService) job.getAdapters().get(IScenarioService.class);

			EObject instance = null;
			try {
				instance = scenarioService.load(job);
			} catch (IOException e) {}

			if (instance instanceof MMXRootObject) {
				final Collection<? extends Object> viewerContent = collector.collectElements((MMXRootObject) instance);
				for (final Object o : viewerContent) {
					sourceByElement.put(o, new Pair<ScenarioInstance, MMXRootObject>(job, (MMXRootObject) instance));
				}
				selectedObjects.addAll(viewerContent);
			}
		}
		
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
}
