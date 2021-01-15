/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

public class FleetProfileTopLevelCompositeFactory extends DefaultDisplayCompositeFactory {
	public FleetProfileTopLevelCompositeFactory() {

	}

//	@Override
//	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
//		return new SubProfileDetailComposite(composite, SWT.NONE, toolkit);
//	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new FleetProfileTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}

}
