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
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.ui.actions.EvaluateUnitCostMatrixAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * A viewer pane for editing unit cost matrix definitions
 * 
 * @author hinton
 * 
 */
public class UnitCostMatrixViewerPane extends ScenarioTableViewerPane {
	public UnitCostMatrixViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");

		addTypicalColumn("Hire Rate", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_NotionalDayRate(), getEditingDomain()));

		addTypicalColumn("Vessel", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_Vessel(), getReferenceValueProviderCache(), getEditingDomain()));

		addTypicalColumn("Speed", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_Speed(), getEditingDomain()));

		addTypicalColumn("Base Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_BaseFuelPrice(), getEditingDomain()));

		addTypicalColumn("LNG Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostMatrix_CargoPrice(), getEditingDomain()));

		final EvaluateUnitCostMatrixAction evaluateAction = new EvaluateUnitCostMatrixAction(getJointModelEditorPart());
		getToolBarManager().appendToGroup(EDIT_GROUP, evaluateAction);
		getToolBarManager().update(true);
		viewer.addSelectionChangedListener(evaluateAction);
		defaultSetTitle("Unit Cost Matrices");

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
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
