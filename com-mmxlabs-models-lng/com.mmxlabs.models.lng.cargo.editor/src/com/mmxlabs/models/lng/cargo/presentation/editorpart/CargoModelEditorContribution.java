package com.mmxlabs.models.lng.cargo.presentation.editorpart;

import org.eclipse.swt.SWT;
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
	private int pageNumber;
	private CargoModelViewer viewer;

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject,
			final UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.rootObject = rootObject;
		this.modelObject = modelObject;
	}

	@Override
	public void addPages(final Composite parent) {
		this.viewer = new CargoModelViewer(parent, SWT.NONE, editorPart);
		viewer.init(editorPart.getAdapterFactory(), CargoPackage.eINSTANCE.getCargoModel_Cargos());
		viewer.setInput(modelObject);
		this.pageNumber = editorPart.addPage(viewer.getControl());
		
	}	
}
