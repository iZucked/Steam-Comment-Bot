/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Class to bulk-import information from CSV files.
 * 
 * Contains modified cut&paste code from ImportAction and SimpleImportAction (class was written in a hurry). TODO: Move duplicated code into a common code object.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class BulkImportCSVHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		final BulkImportWizard wizard = new BulkImportWizard(editor != null ? editor.getScenarioInstance() : null, getFieldToImport(), getTitle());
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();

		return null;
	}

	public abstract String getTitle();

	// TODO: replace this by subclass implementations of the getImportAction handler?
	public abstract FieldChoice getFieldToImport();

}
