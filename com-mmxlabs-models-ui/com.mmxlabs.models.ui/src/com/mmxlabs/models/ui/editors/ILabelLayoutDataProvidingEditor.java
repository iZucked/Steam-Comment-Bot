package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @since 2.0
 */
public interface ILabelLayoutDataProvidingEditor {
	public Object createLabelLayoutData(MMXRootObject root, EObject value, Control control, final Label label);
}
