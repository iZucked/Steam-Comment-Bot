/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class CreateLongTermCommandHandler extends AbstractHandler {

	@Override
	public void setEnabled(Object evaluationContext) {

		boolean enabled = true;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}
		IWorkbenchPage activePage = window.getActivePage();
		if (activePage == null) {
			return;
		}
		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object element = itr.next();

				if (element instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) element;
					final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

					try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("CreateLongTermCommandHandler:setEnabled")) {
						if (ref != null) {
							final LNGScenarioModel scenarioModel = (LNGScenarioModel) ref.getInstance();
							enabled = !scenarioModel.isLongTerm() && scenarioModel.getAdpModel() == null;
							break;
						}
					}
				}
			}
		}

		setBaseEnabled(enabled);
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception exceptions[] = new Exception[1];

		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object element = itr.next();

						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;
							try {
								setLongTermMode(instance);
							} catch (final Exception e) {
								exceptions[0] = e;
							}
						}
					}
				}
			}

		});

		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to set LT mode: " + exceptions[0], exceptions[0]);
		}

		return null;

	}

	private void setLongTermMode(final ScenarioInstance scenarioInstance) throws IOException {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("CreateLongTermCommandHandler:setLongTermMode")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
			if (scenarioModel.isLongTerm() || scenarioModel.getAdpModel() != null) {
				// Do not replace
				return;
			}

			final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
			final CompoundCommand cmd = new CompoundCommand("Enable Long Term mode");
			cmd.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_LongTerm(), Boolean.TRUE));

			editingDomain.getCommandStack().execute(cmd);
		}
	}
}
