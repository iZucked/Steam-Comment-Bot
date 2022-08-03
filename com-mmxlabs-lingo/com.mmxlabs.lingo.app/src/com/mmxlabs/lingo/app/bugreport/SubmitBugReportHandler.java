package com.mmxlabs.lingo.app.bugreport;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class SubmitBugReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		final BugReportWizard wizard = new BugReportWizard(editor != null ? editor.getScenarioInstance() : null, "Submit bug report");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog dialog = new WizardDialog(shell, wizard);
		// Limit initial size to force long lines to wrap rather than creating a wider
		// dialog
		dialog.setPageSize(640, 480);
		dialog.create();
		dialog.open();

		return null;
	}

}
