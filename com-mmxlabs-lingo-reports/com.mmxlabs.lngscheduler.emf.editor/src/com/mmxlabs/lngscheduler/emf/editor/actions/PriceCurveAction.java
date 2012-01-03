/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.actions;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import scenario.Scenario;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.OptimiserPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.curveeditor.CurveDialog;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class PriceCurveAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public PriceCurveAction() {
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

			final DiscountCurve current = scenario.getOptimisation()
					.getCurrentSettings().getDefaultDiscountCurve();
			final CurveDialog curveDialog = new CurveDialog(window.getShell());
			if (curveDialog.open(current) == CurveDialog.OK) {
				// if (current == null) {
				final CompoundCommand cc = new CompoundCommand();
				final DiscountCurve newCurve = curveDialog.createNewCurve();

				cc.append(SetCommand.create(
						editor.getEditingDomain(),
						scenario.getOptimisation().getCurrentSettings(),
						OptimiserPackage.eINSTANCE
								.getOptimisationSettings_DefaultDiscountCurve(),
						newCurve));

				editor.getEditingDomain().getCommandStack().execute(cc);
				// } else {
				// editor.getEditingDomain().getCommandStack().execute(
				// curveDialog.createUpdateCommand(editor.getEditingDomain())
				// );
				// }
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