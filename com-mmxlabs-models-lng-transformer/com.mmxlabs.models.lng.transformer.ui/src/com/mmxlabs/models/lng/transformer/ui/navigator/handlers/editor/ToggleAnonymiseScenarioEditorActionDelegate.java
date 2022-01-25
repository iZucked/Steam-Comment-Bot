/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationMapIO;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationRecord;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ToggleAnonymiseScenarioEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {

	protected IEditorPart targetEditor;
	protected IAction action;
	protected LNGScenarioModel currentModel;
	protected EditingDomain editingDomain;
	private static final Set<String> usedIDStrings = new HashSet<>();

	protected AdapterImpl notificationAdapter = new SafeAdapterImpl() {

		@Override
		protected void safeNotifyChanged(Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			if (msg.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Anonymised()) {
				updateState();
			}
		}
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
		usedIDStrings.clear();

		this.editingDomain = null;
		this.targetEditor = targetEditor;
		this.action = action;

		if (this.targetEditor != null) {
			final IEditorInput editorInput = targetEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();

				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord != null) {
					try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("ToggleAnonymiseScenarioEditorActionDelegate")) {
						this.currentModel = sdp.getTypedScenario(LNGScenarioModel.class);
						this.currentModel.eAdapters().add(notificationAdapter);
						this.editingDomain = sdp.getEditingDomain();
					}
				}
			}
		}
		updateState();
	}

	private void updateState() {
		if (currentModel != null) {
			if (!currentModel.eAdapters().contains(notificationAdapter)) {
				currentModel.eAdapters().add(notificationAdapter);
			}
			action.setEnabled(true);
			action.setChecked(currentModel.isAnonymised());
		} else {
			action.setEnabled(false);
			action.setChecked(false);
		}
	}

	@Override
	public void dispose() {
		action = null;
		targetEditor = null;
		if (currentModel != null) {
			currentModel.eAdapters().remove(notificationAdapter);
		}
		currentModel = null;
	}

	@Override
	public synchronized void run(final IAction action) {
		if (editingDomain == null || currentModel == null) {
			return;
		}
		final LNGScenarioModel scenarioModel = currentModel;
		final EditingDomain ed = editingDomain;
		final List<AnonymisationRecord> records = AnonymisationMapIO.read(AnonymisationMapIO.anonyMapFile);
		if (records.isEmpty()) {
			// Show dialog which says that automatic naming will be applied
			final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			final MessageDialog dialog = new MessageDialog(shell, "Anonymisation", null, "Default naming generation will be applied!", 0, 0, "OK", "Cancel");
			dialog.create();
			// TODO: change to IDialog.Cancel
			if (dialog.open() == 1) {
				return;
			}
		}
		usedIDStrings.clear();
		for (final AnonymisationRecord r : records) {
			usedIDStrings.add(r.newName);
		}
		
		final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, ed, usedIDStrings, records, !currentModel.isAnonymised());
		if (!cmd.isEmpty())
			ed.getCommandStack().execute(cmd);
	}
	
	

	

	


}