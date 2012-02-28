/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class CargoModelEditorContribution implements IJointModelEditorContribution {
	private UUIDObject modelObject;
	private MMXRootObject rootObject;
	private JointModelEditorPart editorPart;
	private int pageNumber = 0;
	private CargoModelViewer viewerPane;

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject,
			final UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.rootObject = rootObject;
		this.modelObject = modelObject;
	}

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
}
