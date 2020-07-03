/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

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
	public SellOpportunityCompositeFactory() {

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
