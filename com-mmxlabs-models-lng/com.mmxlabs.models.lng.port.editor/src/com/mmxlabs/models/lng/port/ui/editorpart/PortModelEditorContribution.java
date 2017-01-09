/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

public class PortModelEditorContribution extends BaseJointModelEditorContribution<PortModel> {
//	private PortEditorPane portViewerPane;
//	private PortGroupEditorPane groupViewerPane;
//	private int pageNumber = 1;

	@Override
	public void addPages(Composite parent) {
//		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
//		portViewerPane = new PortEditorPane(editorPart.getSite().getPage(), editorPart, editorPart);
//		portViewerPane.createControl(sash);
//		portViewerPane.init(Collections.singletonList(PortPackage.eINSTANCE.getPortModel_Ports()),
//				editorPart.getAdapterFactory());
//		
//		groupViewerPane = new PortGroupEditorPane(editorPart.getSite().getPage(), editorPart, editorPart);
//		groupViewerPane.createControl(sash);
//		groupViewerPane.init(Collections.singletonList(PortPackage.eINSTANCE.getPortModel_PortGroups()),
//				editorPart.getAdapterFactory());
//		
//		portViewerPane.getViewer().setInput(modelObject);
//		groupViewerPane.getViewer().setInput(modelObject);
//		
//		pageNumber = editorPart.addPage(sash);
//		editorPart.setPageText(pageNumber, "Ports");
	}

	@Override
	public void setLocked(boolean locked) {
//		portViewerPane.setLocked(locked);
//		groupViewerPane.setLocked(locked);
	}
}
