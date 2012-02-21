package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class FleetModelEditorContribution implements IJointModelEditorContribution {

	private JointModelEditorPart editorPart;
	private UUIDObject modelObject;
	private VesselViewerPane vesselViewerPane;
	private VesselClassViewerPane vesselClassViewerPane;
	private int pageNumber;

	@Override
	public void init(JointModelEditorPart editorPart, MMXRootObject rootObject,
			UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.modelObject = modelObject;
	}

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		vesselViewerPane = new VesselViewerPane(editorPart.getSite().getPage(), editorPart);
		vesselViewerPane.createControl(sash);
		vesselViewerPane.init(Collections.singletonList(FleetPackage.eINSTANCE.getFleetModel_Vessels()),
				editorPart.getAdapterFactory());
		
		vesselClassViewerPane = new VesselClassViewerPane(editorPart.getSite().getPage(), editorPart);
		vesselClassViewerPane.createControl(sash);
		vesselClassViewerPane.init(Collections.singletonList(FleetPackage.eINSTANCE.getFleetModel_VesselClasses()),
				editorPart.getAdapterFactory());
		
		vesselViewerPane.getViewer().setInput(modelObject);
		vesselClassViewerPane.getViewer().setInput(modelObject);
		
		pageNumber = editorPart.addPage(sash);
		editorPart.setPageText(pageNumber, "Fleet");
	}

}
