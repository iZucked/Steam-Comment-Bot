/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceStatusProvider implements IStatusProvider {
	private final Set<IStatusChangedListener> listeners = new HashSet<IStatusChangedListener>();
	private ScenarioInstance scenarioInstance;
	final EContentAdapter validationAdapter = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode()) {
				fireStatusChanged(getStatus());
			}
		};

	};

	public ScenarioInstanceStatusProvider(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		addContentAdapter(scenarioInstance);
	}

	public void dispose() {

		scenarioInstance.eAdapters().remove(validationAdapter);
		scenarioInstance = null;

	}

	private void addContentAdapter(final ScenarioInstance scenarioInstance) {
		// Add adapter to new input
		scenarioInstance.eAdapters().add(validationAdapter);
	}

	@Override
	public IStatus getStatus() {
		// Oops, must be called after dispose....
		if (scenarioInstance == null) {
			return Status.OK_STATUS;
		}
		return (IStatus) scenarioInstance.getAdapters().get(IStatus.class);
	}

	@Override
	public void addStatusChangedListener(final IStatusChangedListener l) {
		listeners.add(l);

	}

	@Override
	public void removeStatusChangedListener(final IStatusChangedListener l) {
		listeners.remove(l);
	}

	private void fireStatusChanged(final IStatus status) {
		final List<IStatusChangedListener> copy = new ArrayList<IStatusChangedListener>(listeners);
		for (final IStatusChangedListener l : copy) {
			l.onStatusChanged(this, status);
		}
	}
}
