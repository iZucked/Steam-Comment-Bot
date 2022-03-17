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

/**
 * Factory for {@link PortDetailComposite}.
 * 
 * @author Simon Goodall
 * 
 */
public class PortDetailCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new PortDetailComposite(parent, SWT.NONE, toolkit);
	}
}
