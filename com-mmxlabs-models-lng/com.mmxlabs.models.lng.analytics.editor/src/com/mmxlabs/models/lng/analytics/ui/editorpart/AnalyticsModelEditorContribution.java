/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

/**
 * @author hinton
 * 
 */
public class AnalyticsModelEditorContribution extends BaseJointModelEditorContribution<AnalyticsModel> implements IPropertySourceProvider {

	@Override
	public void addPages(Composite parent) {
		// final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		// final UnitCostMatrixViewerPane viewer = new UnitCostMatrixViewerPane(editorPart.getSite().getPage(), editorPart, editorPart);
		//
		// viewer.createControl(sash);
		// viewer.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_RoundTripMatrices()), editorPart.getAdapterFactory());
		// viewer.getViewer().setInput(modelObject);
		//
		// final CostMatrixViewer matrixViewer = new CostMatrixViewer(editorPart.getSite().getPage(), editorPart, editorPart, viewer);
		// matrixViewer.createControl(sash);
		//
		// editorPart.setPageText(editorPart.addPage(sash), "Cost Matrices");
	}

	@Override
	public void setLocked(boolean locked) {

	}

	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object instanceof UnitCostLine) {
//			return new UnitCostLinePropertySource((UnitCostLine) object);
		} else if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		}
		return null;
	}
}
