/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * @author Simon Goodall
 * 
 */
public class ContractDetailComposite extends DefaultDetailComposite {

	private final boolean mainTab;

	public ContractDetailComposite(final Composite parent, final int style, final boolean top, final FormToolkit toolkit) {
		super(parent, style, toolkit);

		this.mainTab = top;
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the main tab
		boolean mainTabElement = true;

		// Here the exceptions are listed for the elements which should go into the bottom
		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedListsArePermissive()) {
			mainTabElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContracts()) {
			mainTabElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPorts()) {
			mainTabElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue()) {
			mainTabElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MinCvValue()) {
			mainTabElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_PurchaseDeliveryType()) {
			mainTabElement = false;
		}

		// Do not add elements if they are for the wrong section.
		if (mainTab != mainTabElement) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}
}
