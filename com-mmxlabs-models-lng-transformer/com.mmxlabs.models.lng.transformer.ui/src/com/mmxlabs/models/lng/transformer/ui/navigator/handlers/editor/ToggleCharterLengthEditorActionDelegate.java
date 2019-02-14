/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ToggleCharterLengthEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {
	protected IEditorPart targetEditor;
	protected IAction action;
	protected LNGScenarioModel currentModel;
	protected UserSettings currentSettings;
	protected EditingDomain editingDomain;

	protected AdapterImpl notificationAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			super.notifyChanged(msg);
			if (msg.isTouch()) {
				return;
			}
			if (msg.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings()) {
				updateState();
			}
			if (msg.getFeature() == ParametersPackage.eINSTANCE.getUserSettings_WithCharterLength()) {
				updateState();
			}
		};
	};

	@Override
	public synchronized void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

		if (this.targetEditor != null) {
			// Remove notifications
		}
		if (currentModel != null) {
			currentModel.eAdapters().remove(notificationAdapter);
		}
		currentModel = null;
		if (currentSettings != null) {
			currentSettings.eAdapters().remove(notificationAdapter);
		}
		currentSettings = null;

		this.editingDomain = null;
		this.targetEditor = targetEditor;
		this.action = action;

		if (this.targetEditor != null) {
			final IEditorInput editorInput = targetEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();

				final @NonNull ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("ToggleCharterLengthEditorActionDelegate")) {
					this.currentModel = sdp.getTypedScenario(LNGScenarioModel.class);
					this.currentSettings = currentModel.getUserSettings();
					this.editingDomain = sdp.getEditingDomain();
				}
			}
		}
		updateState();
	}

	private void updateState() {
		if (currentSettings != null) {
			currentSettings.eAdapters().remove(notificationAdapter);
		}
		if (currentModel != null) {
			currentSettings = currentModel.getUserSettings();
		}
		if (currentSettings != null) {
			currentSettings.eAdapters().add(notificationAdapter);
			action.setEnabled(true);
			action.setChecked(currentSettings.isWithCharterLength());
		} else {
			action.setEnabled(false);
			action.setChecked(false);
		}
	}

	@Override
	public void dispose() {
		action = null;
		targetEditor = null;
		currentModel = null;
	}

	@Override
	public synchronized void run(final IAction action) {
		if (editingDomain == null || currentModel == null) {
			return;
		}
		final CompoundCommand cmd = new CompoundCommand("Toggle charter length");

		UserSettings settings = currentModel.getUserSettings();
		if (settings == null) {
			settings = ScenarioUtils.createDefaultUserSettings();
			cmd.append(SetCommand.create(editingDomain, currentModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), settings));
		}
		cmd.append(SetCommand.create(editingDomain, settings, ParametersPackage.eINSTANCE.getUserSettings_WithCharterLength(), !settings.isWithCharterLength()));

		editingDomain.getCommandStack().execute(cmd);
	}

}