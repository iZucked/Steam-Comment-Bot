/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.ui.displaycomposites.RepositioningFeeTermsDetailComposite;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * 
 * @author FM based on Hinton
 * 
 */
public class RepositioningFeeDetailComposite extends RepositioningFeeTermsDetailComposite {
	private GenericCharterContract oldContract = null;

	public RepositioningFeeDetailComposite(final Composite parent, final int style, final FormToolkit toolkit, Runnable resizeAction) {
		super(parent, style, toolkit, resizeAction);
		repositioningFeeCheckbox.setVisible(false);
	}
	
	protected boolean createComposite() {
		return createComposite(oldContract);
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldContract = (GenericCharterContract) value;
		doDisplay(oldContract);
	}
}