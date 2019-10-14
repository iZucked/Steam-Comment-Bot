/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;


public class AbstractNominationCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new AbstractNominationDetailTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}
	
	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new NominationDetailComposite(parent, SWT.NONE, toolkit);
	}
	
}
