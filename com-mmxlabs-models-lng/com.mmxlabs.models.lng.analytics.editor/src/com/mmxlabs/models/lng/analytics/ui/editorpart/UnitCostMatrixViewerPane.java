/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.ui.actions.EvaluateUnitCostMatrixAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;

/**
 * A viewer pane for editing unit cost matrix definitions
 * @author hinton
 *
 */
public class UnitCostMatrixViewerPane extends ScenarioTableViewerPane {
	public UnitCostMatrixViewerPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");
		// etc
		addTypicalColumn("Hire Rate", 
				new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_NotionalDayRate(), getEditingDomain()));
		
		final EvaluateUnitCostMatrixAction evaluateAction = new EvaluateUnitCostMatrixAction(getJointModelEditorPart());
		getToolBarManager().appendToGroup(EDIT_GROUP, evaluateAction);
		getToolBarManager().update(true);
		getJointModelEditorPart().addSelectionChangedListener(evaluateAction);
		defaultSetTitle("Unit Cost Matrices");
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					if (!selection.isEmpty()) {
						if (((IStructuredSelection) selection).getFirstElement() instanceof UnitCostMatrix) {
							final UnitCostMatrix matrix = (UnitCostMatrix) ((IStructuredSelection) selection).getFirstElement();
							if (matrix.getCostLines().isEmpty()) {
								evaluateAction.recomputeSettings(matrix);
							}
						}
					}
				}
			}
		});
	}
}
