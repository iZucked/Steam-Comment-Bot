/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.ui.displaycomposites.BallastBonusTermsDetailComposite;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author FM
 * 
 */
public class BallastBonusDetailComposite extends BallastBonusTermsDetailComposite {
	protected GenericCharterContract oldContract = null;

	public BallastBonusDetailComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit, Runnable resizeAction) {
		super(parent, style, dialogContext, toolkit, resizeAction);
	}

	@Override
	protected boolean changeBallastBonusType() {
		return changeBallastBonusType(oldContract);
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldContract = (GenericCharterContract) value;
		
		if (oldContract != null) {
			createDefaultChildCompositeSection(dialogContext, root, oldContract, range, dbc, oldContract.eClass(), endHeelComposite);
		}
		doDisplay(dialogContext, root, dbc, oldContract);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany() && ref != CommercialPackage.eINSTANCE.getGenericCharterContract_StartHeel() 
				&& ref != CommercialPackage.eINSTANCE.getGenericCharterContract_RepositioningFeeTerms();
	}

}