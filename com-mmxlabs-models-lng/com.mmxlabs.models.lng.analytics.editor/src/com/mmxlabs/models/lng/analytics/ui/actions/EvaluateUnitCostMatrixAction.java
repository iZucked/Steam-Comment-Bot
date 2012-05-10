/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.actions;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.transformer.IAnalyticsTransformer;
import com.mmxlabs.models.lng.analytics.transformer.impl.AnalyticsTransformer;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * An action which updates the values in a unit cost matrix or matrices
 * 
 * @author hinton
 *
 */
public class EvaluateUnitCostMatrixAction extends ScenarioModifyingAction {
	final IAnalyticsTransformer transformer = new AnalyticsTransformer();
	private IScenarioEditingLocation part;
	
	public EvaluateUnitCostMatrixAction(final IScenarioEditingLocation part) {
		this.part = part;
		setText("Evaluate Cost Matrix");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", 
				"$nl$/icons/recompute.gif"));
	}
	
	@Override
	public void run() {
		final UnitCostMatrix matrix = (UnitCostMatrix) ((IStructuredSelection) getLastSelection()).getFirstElement();
		recomputeSettings(matrix);
	}
	
	public void recomputeSettings(final UnitCostMatrix matrix) {
		final Job job = new Job("Recompute cost matrix " + matrix.getName()) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				final List<UnitCostLine> newLines = transformer.createCostLines(part.getRootObject(), matrix, monitor);
				final EditingDomain d = part.getEditingDomain();
				final CompoundCommand replace = new CompoundCommand();
				final Command rc = RemoveCommand.create(d, matrix, AnalyticsPackage.eINSTANCE.getUnitCostMatrix_CostLines(), matrix.getCostLines());
				replace.append(rc);
				final Command ac = AddCommand.create(d, matrix, AnalyticsPackage.eINSTANCE.getUnitCostMatrix_CostLines(), newLines);
				replace.append(ac);

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							part.setDisableUpdates(true);
							d.getCommandStack().execute(replace);
						} finally {
							part.setDisableUpdates(false);
						}
					}
				});
				return Status.OK_STATUS;

			}
			
		};
		
		try {
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ProgressView");
		} catch (PartInitException e) {
		}
		
		job.schedule();
	}

	@Override
	protected boolean isApplicableToSelection(ISelection selection) {
		return (selection instanceof IStructuredSelection)
				&& (((IStructuredSelection) selection).toList().size() == 1) &&
				(((IStructuredSelection) selection).getFirstElement()) instanceof UnitCostMatrix;
	}
}
