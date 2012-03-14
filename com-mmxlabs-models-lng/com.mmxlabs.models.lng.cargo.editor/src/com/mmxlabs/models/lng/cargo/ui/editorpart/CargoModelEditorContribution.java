/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int pageNumber = 0;
	private CargoModelViewer viewerPane;

	@Override
	public void addPages(final Composite parent) {
		this.viewerPane = new CargoModelViewer(editorPart.getSite().getPage(), editorPart);
		viewerPane.createControl(parent);
		viewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_Cargos()),
				editorPart.getAdapterFactory());
		viewerPane.getViewer().setInput(modelObject);
		pageNumber = editorPart.addPage(viewerPane.getControl());
		editorPart.setPageText(pageNumber, "Cargos");
	}

	@Override
	protected void lock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void unlock() {
		// TODO Auto-generated method stub
		
	}
}
