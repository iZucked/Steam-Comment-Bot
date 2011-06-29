package com.mmxlabs.lngscheduler.emf.editor.actions;

import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import scenario.Scenario;
import scenario.presentation.ScenarioEditor;

/**
 * An action which deletes all solutions from a scenario, so you can re-evaluate
 * it from scratch.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ClearSolutionsAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public ClearSolutionsAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if (window.getActivePage().getActiveEditor() instanceof ScenarioEditor) {
			final ScenarioEditor editor = (ScenarioEditor) window
					.getActivePage().getActiveEditor();
			final Scenario scenario = editor.getScenario();

			final MessageDialog dialog = new MessageDialog(
					window.getShell(),
					"Clear Solutions",
					null,
					"Are you sure you want to delete all solutions and vessel assignments from the scenario "
							+ scenario.getName() // TODO resource name?
							+ "?", MessageDialog.QUESTION, new String[] {
							"Clear Solutions", "Cancel" }, 0);

			if (dialog.open() == Window.OK) {
				final EditingDomain editingDomain = editor.getEditingDomain();
				editingDomain.getCommandStack().execute(
						DeleteCommand.create(editingDomain, scenario
								.getScheduleModel().getSchedules()));
			}
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}