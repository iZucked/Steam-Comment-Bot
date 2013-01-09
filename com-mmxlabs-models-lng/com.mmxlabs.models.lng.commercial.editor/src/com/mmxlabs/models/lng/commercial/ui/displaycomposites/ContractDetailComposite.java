package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * @author Simon Goodall
 * 
 */
public class ContractDetailComposite extends DefaultDetailComposite {

	private final boolean top;

	public ContractDetailComposite(final Composite parent, final int style, final boolean top) {
		super(parent, style);

		this.top = top;
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the top
		boolean topElement = true;

		// Here the exceptions are listed for the elements which should go into the bottom
		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedListsArePermissive()) {
			topElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContracts()) {
			topElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPorts()) {
			topElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue()) {
			topElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MinCvValue()) {
			topElement = false;
		}
		// Do not add elements if they are for the wrong section.
		if (top != topElement) {
			return;
		}

		super.addInlineEditor(editor);
	}
}
