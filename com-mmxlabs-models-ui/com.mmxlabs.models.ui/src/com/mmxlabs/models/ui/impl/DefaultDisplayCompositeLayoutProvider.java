/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.ILabelLayoutDataProvidingEditor;

public class DefaultDisplayCompositeLayoutProvider implements IDisplayCompositeLayoutProvider {

	@Override
	public boolean showLabelFor(final MMXRootObject root, final EObject value, final IInlineEditor editor) {
		return editor.hasLabel();
	}

	@Override
	public Layout createTopLevelLayout(final MMXRootObject root, final EObject value, final int numberOfDetailLayouts) {
		return new GridLayout(numberOfDetailLayouts, true);
	}

	@Override
	public Layout createDetailLayout(final MMXRootObject root, final EObject value) {

		// TODO: replace this with a GridBagLayout or GroupLayout; for editors without a label,
		// we want the editor to take up two cells rather than one.
		return new GridLayout(2, false);
	}

	@Override
	public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
		final Object result = editor.createLayoutData(root, value, control);
		if (result == null) {
			return new GridData(SWT.FILL, SWT.CENTER, true, false);
		} else {
			return result;
		}
	}

	@Override
	public Object createTopLayoutData(final MMXRootObject root, final EObject value, final EObject detail) {
		return new GridData(GridData.FILL_BOTH);
	}

	@Override
	public Object createLabelLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control, final Label label) {
		if (editor instanceof ILabelLayoutDataProvidingEditor) {
			return ((ILabelLayoutDataProvidingEditor) editor).createLabelLayoutData(root, value, control, label);
		} else {
			return new GridData(SWT.LEFT, SWT.CENTER, false, false);
		}
	}
}
