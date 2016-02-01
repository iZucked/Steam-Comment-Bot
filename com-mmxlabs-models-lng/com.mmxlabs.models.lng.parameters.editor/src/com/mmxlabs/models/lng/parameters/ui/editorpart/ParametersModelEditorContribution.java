/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.ui.editorpart;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

/**
 * @author hinton
 *
 */
public class ParametersModelEditorContribution extends BaseJointModelEditorContribution<ParametersModel> {
//	private OptimiserSettingsEditorPane pane;

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution#addPages(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void addPages(Composite parent) {
//		pane = new OptimiserSettingsEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
//		
//		pane.createControl(parent);
//		pane.init(
//				Arrays.asList(new EReference[] {OptimiserPackage.eINSTANCE.getOptimiserModel_Settings()}),
//				editorPart.getAdapterFactory());
//		pane.getViewer().setInput(modelObject);
//		
//		int pageNumber = editorPart.addPage(pane.getControl());
//		editorPart.setPageText(pageNumber, "Optimiser Settings");
	}

	@Override
	public void setLocked(boolean locked) {
//		pane.setLocked(locked);
	}

}
