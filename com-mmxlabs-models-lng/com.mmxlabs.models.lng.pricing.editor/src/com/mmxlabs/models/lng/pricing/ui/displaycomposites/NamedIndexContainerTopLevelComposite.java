/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite to show a commodity index (not including its DataIndex children)
 * 
 * @author Simon McGregor
 * 
 */
public class NamedIndexContainerTopLevelComposite extends DefaultTopLevelComposite {

	public NamedIndexContainerTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Don't display DataIndex child objects for editing.
	 */
	@Override
	protected void createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		/*
		 * Note: the logic is implemented in the #createChildArea method because the #shouldDisplay method does not permit the particular value of a child object to be interrogated.
		 */

		if ((value instanceof DataIndex) == false) {
			super.createChildArea(root, object, parent, ref, value);
		}
	}

}
