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
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class CommercialModelEditorContribution extends BaseJointModelEditorContribution<CommercialModel> {
	private int pageNumber;

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		
		EntityEditorPane entEp = new EntityEditorPane(editorPart.getSite().getPage(), editorPart);
		ContractEditorPane pconEp = new ContractEditorPane(editorPart.getSite().getPage(), editorPart);
		ContractEditorPane sconEp = new ContractEditorPane(editorPart.getSite().getPage(), editorPart);

		entEp.createControl(sash);
		sconEp.createControl(sash);
		pconEp.createControl(sash);
		
		entEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_Entities()), editorPart.getAdapterFactory());
		sconEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts()), editorPart.getAdapterFactory());
		pconEp.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts()), editorPart.getAdapterFactory());
		
		entEp.getViewer().setInput(modelObject);
		sconEp.getViewer().setInput(modelObject);
		pconEp.getViewer().setInput(modelObject);
		
		pconEp.defaultSetTitle("Purchase Contracts");
		sconEp.defaultSetTitle("Sales Contracts");
		
		pageNumber = editorPart.addPage(sash);
		editorPart.setPageText(pageNumber, "Commercial");
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
