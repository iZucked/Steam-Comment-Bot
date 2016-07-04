/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * Extension of {@link DefaultDetailComposite} which adds support for extensions in {@link MMXObject#getExtensions()}
 * 
 * @author hinton
 * 
 */
public class MMXObjectDetailComposite extends DefaultDetailComposite {
	/**
	 */
	public MMXObjectDetailComposite(Composite parent, int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, MMXRootObject root, EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		// handle extension fields.
		super.display(dialogContext, root, object, range, dbc);
	}

}
