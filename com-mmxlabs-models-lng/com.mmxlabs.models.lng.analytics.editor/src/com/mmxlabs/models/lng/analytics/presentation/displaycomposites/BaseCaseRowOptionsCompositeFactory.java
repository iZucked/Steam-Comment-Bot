/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
 * Factory for {@link BaseCaseRowOptionsTopLevelComposite} application.
 * 
 */
public class BaseCaseRowOptionsCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new BaseCaseRowOptionsTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		// This is not expected to be called. The CargoTopLevelComposite will create
		// it's own instances directly.
		throw new UnsupportedOperationException("Unexpected method invocations");
	}
}
