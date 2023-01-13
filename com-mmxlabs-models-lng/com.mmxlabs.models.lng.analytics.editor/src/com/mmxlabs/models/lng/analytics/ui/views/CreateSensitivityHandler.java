/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public class CreateSensitivityHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		final Exception exceptions[] = new Exception[1];
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {
			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof final IStructuredSelection iStructuredSelection) {
					final Iterator<?> itr = iStructuredSelection.iterator();
					while (itr.hasNext()) {
						final Object element = itr.next();
						if (element instanceof final ScenarioInstance scenarioInstance) {
							try {
								createSensitivityModelIfMissing(scenarioInstance);
							} catch (final Exception e) {
								exceptions[0] = e;
							}
						}
					}
				}
			}
		});
		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to create sensitivity models: " + exceptions[0], exceptions[0]);
		}
		return null;
	}

	private void createSensitivityModelIfMissing(final ScenarioInstance scenarioInstance) throws IOException {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("CreateSensitivityModelCommandHandler:createSensitivityModelIfMissing")) {
			final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
			if (lngScenarioModel.getSensitivityModel() != null) {
				// Do not replace
				final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
				boolean eopenEditor = OpenScenarioUtils.closeEditors(editorInput);
				final CompoundCommand cmd = new CompoundCommand("Delete sensitivity");
				final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
				final SensitivityModel sensitivityModel = lngScenarioModel.getSensitivityModel();
				cmd.append(DeleteCommand.create(editingDomain, sensitivityModel));
				editingDomain.getCommandStack().execute(cmd);
				if (eopenEditor) {
					RunnerHelper.asyncExec(() -> {
						try {
							OpenScenarioUtils.openEditor(editorInput);
						} catch (Exception e) {

						}
					});
				}
				return;
			}

			// Close all open editors
			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			boolean eopenEditor = OpenScenarioUtils.closeEditors(editorInput);

			final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
			final SensitivityModel sensitivityModel = AnalyticsFactory.eINSTANCE.createSensitivityModel();
			final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
			model.setName("Sensitivity model");
			final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
			model.setBaseCase(baseCase);
			sensitivityModel.setSensitivityModel(model);
			final CompoundCommand cmd = new CompoundCommand("Create sensitivity");
			cmd.append(SetCommand.create(editingDomain, lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SensitivityModel(), sensitivityModel));
			editingDomain.getCommandStack().execute(cmd);
			if (eopenEditor) {
				RunnerHelper.asyncExec(() -> {
					try {
						OpenScenarioUtils.openEditor(editorInput);
					} catch (Exception e) {

					}
				});
			}
		}
	}
}
