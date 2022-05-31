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

import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link BuyOpportunityTopLevelComposite} application.
 * 
 * @author Simon Goodall
 * 
 */
public class BuyOpportunityCompositeFactory extends DefaultDisplayCompositeFactory {
	@Override
	public String getRenderingLabel(final List<EObject> inputs) {
		if (inputs.isEmpty()) {
			return null;
		}

		if (inputs.get(0) instanceof final BuyOpportunity op) {
			if (inputs.size() == 1 && op.isSetName()) {
				return "Buy Opportunity: " + op.getName();
			}
			return "Buy Opportunit" + (inputs.size() == 1 ? "y" : "ies");
		}

		return null;
	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new BuyOpportunityTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new OpportunityDetailComposite(parent, SWT.NONE, toolkit);
	}
}
