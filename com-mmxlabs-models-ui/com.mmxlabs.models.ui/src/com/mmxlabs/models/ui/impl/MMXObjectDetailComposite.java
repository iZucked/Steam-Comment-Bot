/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXObject;

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
}
