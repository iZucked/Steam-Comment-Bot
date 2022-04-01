/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.validation.IValidationApplicableProvider;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDirtyListener;
import com.mmxlabs.scenario.service.model.manager.IScenarioLockListener;
import com.mmxlabs.scenario.service.model.manager.IScenarioValidationListener;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ValidatingDecorator extends LabelProvider implements ILightweightLabelDecorator {

	private final @NonNull IScenarioValidationListener validationListener = new IScenarioValidationListener() {

		@Override
		public void validationChanged(final ScenarioModelRecord scenarioModelRecord, @NonNull final IStatus status) {
			if (scenarioModelRecord != null) {
				List<Object> targets = new LinkedList<>();
				targets.add(scenarioModelRecord);
				targets.addAll(scenarioModelRecord.getScenarioInstance().getFragments());
				final LabelProviderChangedEvent event = new LabelProviderChangedEvent(ValidatingDecorator.this, targets.toArray());
				RunnerHelper.asyncExec(() -> fireLabelProviderChanged(event));
			}
		}
	};
	private final @NonNull IScenarioDirtyListener dirtyListener = new IScenarioDirtyListener() {

		@Override
		public void dirtyStatusChanged(@NonNull final ModelRecord modelRecord, final boolean isDirty) {
			if (modelRecord instanceof ScenarioModelRecord scenarioModelRecord) {
				final LabelProviderChangedEvent event = new LabelProviderChangedEvent(ValidatingDecorator.this, scenarioModelRecord.getScenarioInstance());
				RunnerHelper.asyncExec(() -> fireLabelProviderChanged(event));
			}
		}

	};
	private final @NonNull IScenarioLockListener lockListener = new IScenarioLockListener() {
		@Override
		public void lockStateChanged(@NonNull final ModelRecord modelRecord, final boolean writeLocked) {
			if (modelRecord instanceof ScenarioModelRecord scenarioModelRecord) {
				final LabelProviderChangedEvent event = new LabelProviderChangedEvent(ValidatingDecorator.this, scenarioModelRecord.getScenarioInstance());
				RunnerHelper.asyncExec(() -> fireLabelProviderChanged(event));
			}
		}

	};

	private final Collection<ScenarioModelRecord> listenerRefs = new ConcurrentLinkedQueue<>();

	@Override
	public void dispose() {

		while (!listenerRefs.isEmpty()) {
			final ScenarioModelRecord entry = listenerRefs.iterator().next();
			listenerRefs.remove(entry);
			// Remove adapter to new input
			entry.removeValidationListener(validationListener);
			entry.removeDirtyListener(dirtyListener);
			entry.removeLockListener(lockListener);
		}
	}

	@Override
	public void decorate(final Object element, final IDecoration decoration) {
		if (element instanceof ScenarioService scenarioService) {
//			if (!scenarioService.isLocal()) {
//				decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/remote.gif"), IDecoration.BOTTOM_RIGHT);
//			}
		} else if (element instanceof ScenarioInstance scenarioInstance) {

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			if (modelRecord != null) {

				if (scenarioInstance.isCloudLocked()) {

					// This trumps the other icons
//					decoration.addOverlay(CommonImages.getImageDescriptor(IconPaths.CloudPlay_16, IconMode.Enabled), IDecoration.TOP_LEFT);
//					return;
				}

				if (modelRecord.getValidationStatusSeverity() == IStatus.ERROR) {
					decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/error.gif"), IDecoration.BOTTOM_RIGHT);
				} else if (modelRecord.getValidationStatusSeverity() == IStatus.WARNING) {
					decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/warning.gif"), IDecoration.BOTTOM_RIGHT);
				} else if (modelRecord.getValidationStatusSeverity() == IStatus.INFO) {
					decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/info.gif"), IDecoration.BOTTOM_RIGHT);
				}

				else {
					try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ValidatingDecorator")) {
						if (ref != null && ref.isLocked()) {
							decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/lock_optimising.gif"), IDecoration.TOP_LEFT);

						}
					}
				}

				if (!listenerRefs.contains(modelRecord)) {
					addContentAdapter(modelRecord);
				}
			}
		} else if (element instanceof ScenarioFragment scenarioFragment) {
			ScenarioInstance scenarioInstance = scenarioFragment.getScenarioInstance();
			if (scenarioInstance != null) {

				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

				if (modelRecord != null) {

					int validationStatus = getObjectToStatus(modelRecord.getValidationStatus(), IStatus.OK, scenarioFragment.getFragment());

					if (validationStatus == IStatus.ERROR) {
						decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/error.gif"), IDecoration.BOTTOM_RIGHT);
					} else if (validationStatus == IStatus.WARNING) {
						decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/warning.gif"), IDecoration.BOTTOM_RIGHT);
					} else if (validationStatus == IStatus.INFO) {
						decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/info.gif"), IDecoration.BOTTOM_RIGHT);
					}
				}
			}

		}
	}

	private void addContentAdapter(final ScenarioModelRecord modelRecord) {

		// Add adapter to new input
		modelRecord.addValidationListener(validationListener);
		modelRecord.addDirtyListener(dirtyListener);
		modelRecord.addLockListener(lockListener);

		listenerRefs.add(modelRecord);
	}

	private int getObjectToStatus(IStatus status, int severity, EObject target) {
		if (status == null) {
			return severity;
		}
		int newSeverity = severity;
		if (status.isMultiStatus()) {
			for (IStatus s : status.getChildren()) {
				newSeverity = Math.max(newSeverity, getObjectToStatus(s, newSeverity, target));
			}
		}
		if (status instanceof IValidationApplicableProvider p) {
			if (p.doesValidationApplyToChildOf(target)) {
				newSeverity = Math.max(newSeverity, status.getSeverity());
			}
		}
		return newSeverity;
	}
}
