/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class PortModelEditorContribution extends BaseJointModelEditorContribution<PortModel> {
	private PortEditorPane portViewerPane;
	private PortGroupEditorPane groupViewerPane;
	private int pageNumber = 1;

	@Override
	public void addPages(Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		portViewerPane = new PortEditorPane(editorPart.getSite().getPage(), editorPart);
		portViewerPane.createControl(sash);
		portViewerPane.init(Collections.singletonList(PortPackage.eINSTANCE.getPortModel_Ports()),
				editorPart.getAdapterFactory());
		
		groupViewerPane = new PortGroupEditorPane(editorPart.getSite().getPage(), editorPart);
		groupViewerPane.createControl(sash);
		groupViewerPane.init(Collections.singletonList(PortPackage.eINSTANCE.getPortModel_PortGroups()),
				editorPart.getAdapterFactory());
		
		portViewerPane.getViewer().setInput(modelObject);
		groupViewerPane.getViewer().setInput(modelObject);
		
		pageNumber = editorPart.addPage(sash);
		editorPart.setPageText(pageNumber, "Ports");
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
