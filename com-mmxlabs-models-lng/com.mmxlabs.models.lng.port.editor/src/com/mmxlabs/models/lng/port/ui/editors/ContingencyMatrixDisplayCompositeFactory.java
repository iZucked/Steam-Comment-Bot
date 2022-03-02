/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

public class ContingencyMatrixDisplayCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createToplevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		return new ContingencyMatrixLevelComposite(parent, SWT.NONE, dialogContext, toolkit);
	}
}
