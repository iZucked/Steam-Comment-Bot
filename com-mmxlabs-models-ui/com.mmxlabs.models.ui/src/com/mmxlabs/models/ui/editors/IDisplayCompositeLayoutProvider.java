/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * This provides a layout to an IDisplayComposite
 * 
 * @author hinton
 *
 */
public interface IDisplayCompositeLayoutProvider {
	public boolean showLabelFor(final MMXRootObject root, final EObject value, final IInlineEditor editor);
	public Layout createTopLevelLayout(final MMXRootObject root, final EObject value, int numDetailLayouts);
	public Layout createDetailLayout(final MMXRootObject root, final EObject value);
	public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control);
	public Object createLabelLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control, Label label);
	public Object createTopLayoutData(final MMXRootObject root, final EObject value, final EObject detail);
}
