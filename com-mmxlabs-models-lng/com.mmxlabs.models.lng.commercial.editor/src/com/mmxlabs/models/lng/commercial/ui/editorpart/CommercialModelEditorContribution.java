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
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

public class CommercialModelEditorContribution extends BaseJointModelEditorContribution<CommercialModel> {
	private int pageNumber;
	private ContractEditorPane salesContractEditorPane;
	private ContractEditorPane purchaseContractEditorPane;
	private EntityEditorPane entityEditorPane;

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		
		entityEditorPane = new EntityEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		purchaseContractEditorPane = new ContractEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		salesContractEditorPane = new ContractEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());

		entityEditorPane.createControl(sash);
		purchaseContractEditorPane.createControl(sash);
		salesContractEditorPane.createControl(sash);
		
		entityEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_Entities()), editorPart.getAdapterFactory());
		purchaseContractEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts()), editorPart.getAdapterFactory());
		salesContractEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts()), editorPart.getAdapterFactory());
		
		entityEditorPane.getViewer().setInput(modelObject);
		purchaseContractEditorPane.getViewer().setInput(modelObject);
		salesContractEditorPane.getViewer().setInput(modelObject);
		
		purchaseContractEditorPane.defaultSetTitle("Purchase Contracts");
		salesContractEditorPane.defaultSetTitle("Sales Contracts");
		
		pageNumber = editorPart.addPage(sash);
		editorPart.setPageText(pageNumber, "Commercial");
	}


	@Override
	public void setLocked(boolean locked) {
		entityEditorPane.setLocked(locked);
		salesContractEditorPane.setLocked(locked);
		purchaseContractEditorPane.setLocked(locked);
	}
}
