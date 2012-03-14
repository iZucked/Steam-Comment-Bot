/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.ui.editorpart;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * @author hinton
 *
 */
public class OptimiserModelEditorContribution extends BaseJointModelEditorContribution<OptimiserModel> {
	private OptimiserSettingsEditorPane pane;

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution#addPages(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void addPages(Composite parent) {
		pane = new OptimiserSettingsEditorPane(editorPart.getSite().getPage(), editorPart);
		
		pane.createControl(parent);
		pane.init(
				Arrays.asList(new EReference[] {OptimiserPackage.eINSTANCE.getOptimiserModel_Settings()}),
				editorPart.getAdapterFactory());
		pane.getViewer().setInput(modelObject);
		
		int pageNumber = editorPart.addPage(pane.getControl());
		editorPart.setPageText(pageNumber, "Optimiser Settings");
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution#lock()
	 */
	@Override
	protected void lock() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution#unlock()
	 */
	@Override
	protected void unlock() {
		// TODO Auto-generated method stub
		
	}

}
