/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 */
public interface ILabelLayoutDataProvidingEditor {
	public Object createLabelLayoutData(MMXRootObject root, EObject value, Control control, final Label label);
}
