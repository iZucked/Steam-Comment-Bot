/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceStatusProvider extends DefaultStatusProvider {
	private ScenarioInstance scenarioInstance;
	final AdapterImpl validationAdapter = new AdapterImpl() {
		@Override
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
		if (scenarioInstance != null) {
			scenarioInstance.eAdapters().remove(validationAdapter);
			scenarioInstance = null;
		}

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

}
