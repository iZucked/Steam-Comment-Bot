/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * TODO: Handle new services being added. TODO: Common navigator only updates with a single column! TODO: Common navigator does not update until refreshed.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidatingDecorator extends LabelProvider implements ILightweightLabelDecorator {

	private final Map<ScenarioInstance, EContentAdapter> listenerRefs = new WeakHashMap<ScenarioInstance, EContentAdapter>();

	public ValidatingDecorator() {
	}

	@Override
	public void dispose() {

		for (final Map.Entry<ScenarioInstance, EContentAdapter> entry : listenerRefs.entrySet()) {
			// Remove adapter to new input
			entry.getKey().eAdapters().remove(entry.getValue());
		}

	}

	@Override
	public void decorate(final Object element, final IDecoration decoration) {

		if (element instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) element;
			if (scenarioInstance.getValidationStatusCode() == IStatus.ERROR) {
				decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/error.gif"), IDecoration.BOTTOM_RIGHT);
			} else if (scenarioInstance.getValidationStatusCode() == IStatus.WARNING) {
				decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/warning.gif"), IDecoration.BOTTOM_RIGHT);
			}
			// else if (validationStatus.getSeverity() == IStatus.INFO) {
			// images.add(getResourceLocator().getImage("overlays/info.gif"));
			// }
			if (!listenerRefs.containsKey(scenarioInstance)) {
				addContentAdapter(scenarioInstance);
			}
		}
	}

	private void addContentAdapter(final ScenarioInstance scenarioInstance) {

		final MMXRootObject rootObject = (MMXRootObject) scenarioInstance.getInstance();
		if (rootObject == null) {
			return;
		}

		final EContentAdapter validationAdapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode()) {
					final LabelProviderChangedEvent event = new LabelProviderChangedEvent(ValidatingDecorator.this, scenarioInstance);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							fireLabelProviderChanged(event);
						}
					});
				}
			};

		};

		// Add adapter to new input
		scenarioInstance.eAdapters().add(validationAdapter);

		listenerRefs.put(scenarioInstance, validationAdapter);
	}
}
