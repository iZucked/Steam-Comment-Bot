/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * @author Simon Goodall
 * 
 */
public class ContractDetailComposite extends DefaultDetailComposite {

	private final boolean topOfPane;

	public ContractDetailComposite(final Composite parent, final int style, final boolean top, final FormToolkit toolkit) {
		super(parent, style, toolkit);

		this.topOfPane = top;

	}

	@Override
	protected void setDefaultHelpContext(EObject object) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "com.mmxlabs.lingo.doc.DataModel_lng_commercial_Contract");
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the main tab
		boolean topOfPaneElement = true;

		// Here the exceptions are listed for the elements which should go into the bottom
		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedListsArePermissive()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContracts()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPorts()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MinCvValue()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getPurchaseContract_SalesDeliveryType()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_PurchaseDeliveryType()) {
			topOfPaneElement = false;
		}

		// Do not add elements if they are for the wrong section.
		if (topOfPane != topOfPaneElement) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}
}
