/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link StartHeelOptions} .
 * 
 * @authorFM
 * 
 */
public class StartHeelOptionsCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		return new StartHeelOptionsDetailComposite(parent, SWT.NONE, toolkit);
	}
}
