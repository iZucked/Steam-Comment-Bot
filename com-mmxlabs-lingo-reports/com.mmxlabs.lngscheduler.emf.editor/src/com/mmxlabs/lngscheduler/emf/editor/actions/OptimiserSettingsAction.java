package com.mmxlabs.lngscheduler.emf.editor.actions;

import java.util.Collections;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import scenario.Scenario;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LSOSettings;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.dialogs.OptimisationSettingsDialog;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class OptimiserSettingsAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public OptimiserSettingsAction() {
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

			final LSOSettings settings = (LSOSettings) scenario
					.getOptimisation().getCurrentSettings();

			final OptimisationSettingsDialog osd = new OptimisationSettingsDialog(
					window.getShell(), editor.getEditingDomain(),
					window.getActivePage(), editor);

			if (osd.open(settings) == Window.OK) {
				// replace settings
				final EditingDomain editingDomain = editor.getEditingDomain();
				final CompoundCommand cc = new CompoundCommand();
				cc.append(DeleteCommand.create(editingDomain,
						Collections.singleton(settings)));
				final LSOSettings newSettings = osd.getOutput();
				cc.append(AddCommand.create(editingDomain, scenario
						.getOptimisation(), OptimiserPackage.eINSTANCE
						.getOptimisation_AllSettings(), Collections
						.singleton(newSettings)));
				cc.append(SetCommand.create(editingDomain, scenario
						.getOptimisation(), OptimiserPackage.eINSTANCE
						.getOptimisation_CurrentSettings(), newSettings));
				editingDomain.getCommandStack().execute(cc);
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