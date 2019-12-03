/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
 * Factory for {@link LoadSlotTopLevelComposite} application.
 * 
 * @author Simon Goodall & FM
 * 
 */
public class PaperCompositeFactory extends DefaultDisplayCompositeFactory {
	public PaperCompositeFactory() {

	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new PaperDetailComposite(parent, SWT.NONE, toolkit);
	}
}
