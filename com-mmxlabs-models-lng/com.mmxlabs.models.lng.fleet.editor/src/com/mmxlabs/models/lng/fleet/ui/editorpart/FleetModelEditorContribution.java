/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

public class FleetModelEditorContribution extends BaseJointModelEditorContribution<FleetModel> {	
	private VesselViewerPane vesselViewerPane;
//	private VesselClassViewerPane vesselClassViewerPane;
	private VesselEventViewerPane eventViewerPane;

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		
		vesselViewerPane = new VesselViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		vesselViewerPane.createControl(sash);
		vesselViewerPane.init(Collections.singletonList(FleetPackage.eINSTANCE.getFleetModel_Vessels()),
				editorPart.getAdapterFactory());
		
		eventViewerPane = new VesselEventViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		eventViewerPane.createControl(sash);
		eventViewerPane.init(Collections.singletonList(FleetPackage.eINSTANCE.getFleetModel_VesselEvents()), editorPart.getAdapterFactory());
		
		vesselViewerPane.getViewer().setInput(modelObject);
		eventViewerPane.getViewer().setInput(modelObject);
		
		int eventPage = editorPart.addPage(sash);
		editorPart.setPageText(eventPage, "Fleet");
		
//		
//		vesselClassViewerPane = new VesselClassViewerPane(editorPart.getSite().getPage(), editorPart, editorPart);
//		vesselClassViewerPane.createControl(sash);
//		vesselClassViewerPane.init(Collections.singletonList(FleetPackage.eINSTANCE.getFleetModel_VesselClasses()),
//				editorPart.getAdapterFactory());
//		
//		vesselViewerPane.getViewer().setInput(modelObject);
//		eventViewerPane.getViewer().setInput(modelObject);
//		vesselClassViewerPane.getViewer().setInput(modelObject);
//		
//		pageNumber = editorPart.addPage(sash);
//		editorPart.setPageText(pageNumber, "Fleet");
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution#lock()
	 */
	@Override
	public void setLocked(boolean locked) {
//		vesselClassViewerPane.setLocked(locked);
		vesselViewerPane.setLocked(locked);
		eventViewerPane.setLocked(locked);
	}
}
