/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.holidaycalendars;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportHolidayCalendarsHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		final ImportHolidayCalendarsWizard wizard = new ImportHolidayCalendarsWizard(editor != null ? editor.getScenarioInstance() : null, "Import holiday calendar");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();

		return null;
	}

}
