/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link SellOpportunityTopLevelComposite} application.
 * 
 * @author Simon Goodall
 * 
 */
public class SellOpportunityCompositeFactory extends DefaultDisplayCompositeFactory {
	 
	@Override
	public String getRenderingLabel(List<EObject> inputs) {
		if (inputs.isEmpty()) {
			return null;
		}
		
		if (inputs.get(0) instanceof SellOpportunity op) {
			if (inputs.size() == 1 && op.isSetName()) {
				return "Sell Opportunity: " + op.getName();	
			}
			return "Sell Opportunit" + (inputs.size() == 1 ? "y" : "ies");
		}
		
		return null;
	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new SellOpportunityTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new OpportunityDetailComposite(parent, SWT.NONE, toolkit);
	}
}
