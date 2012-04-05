/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Extension of {@link DefaultDetailComposite} which adds support for extensions in {@link MMXObject#getExtensions()}
 * 
 * @author hinton
 *
 */
public class MMXObjectDetailComposite extends DefaultDetailComposite {
	public MMXObjectDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void display(MMXRootObject root, EObject object, final Collection<EObject> range) {
		// handle extension fields.
		super.display(root, object, range);
	}

	
}
