/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class CommercialModelEditorContribution implements IJointModelEditorContribution {
	CommercialModel subModel;
	MMXRootObject rootModel;
	JointModelEditorPart part;
	private int pageNumber;
	
	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		this.part = editorPart;
		this.rootModel = rootObject;
		this.subModel = (CommercialModel) modelObject;
	}

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		
		EntityEditorPane entEp = new EntityEditorPane(part.getSite().getPage(), part);
		ContractEditorPane pconEp = new ContractEditorPane(part.getSite().getPage(), part);
		ContractEditorPane sconEp = new ContractEditorPane(part.getSite().getPage(), part);

		entEp.createControl(sash);
		sconEp.createControl(sash);
		pconEp.createControl(sash);
		
		entEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_Entities()), part.getAdapterFactory());
		sconEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts()), part.getAdapterFactory());
		pconEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts()), part.getAdapterFactory());
		
		entEp.getViewer().setInput(subModel);
		sconEp.getViewer().setInput(subModel);
		pconEp.getViewer().setInput(subModel);
		
		pconEp.defaultSetTitle("Purchase Contracts");
		sconEp.defaultSetTitle("Sales Contracts");
		
		pageNumber = part.addPage(sash);
		part.setPageText(pageNumber, "Commercial");
	}
}
