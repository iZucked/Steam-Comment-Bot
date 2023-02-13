/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.ui.displaycomposites.RepositioningFeeTermsDetailComposite;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
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

	public RepositioningFeeDetailComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit, Runnable resizeAction) {
		super(parent, style, dialogContext, toolkit, resizeAction);
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldContract = (GenericCharterContract) value;
		
		if (oldContract != null) {
			createDefaultChildCompositeSection(dialogContext, root, oldContract, range, dbc, oldContract.eClass(), startHeelComposite);
		}
		
		doDisplay(oldContract);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany() && ref != CommercialPackage.eINSTANCE.getGenericCharterContract_EndHeel()
				&& ref != CommercialPackage.eINSTANCE.getGenericCharterContract_BallastBonusTerms() 
				&& ref != CommercialPackage.eINSTANCE.getGenericCharterContract_RepositioningFeeTerms();
	}
}