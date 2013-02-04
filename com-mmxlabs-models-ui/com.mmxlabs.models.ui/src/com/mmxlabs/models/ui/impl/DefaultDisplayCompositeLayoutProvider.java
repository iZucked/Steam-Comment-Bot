/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

public class DefaultDisplayCompositeLayoutProvider implements
		IDisplayCompositeLayoutProvider {

	@Override
	public boolean showLabelFor(MMXRootObject root, EObject value,
			IInlineEditor editor) {
		return true;
	}

	@Override
	public Layout createTopLevelLayout(MMXRootObject root, EObject value, int numberOfDetailLayouts) {
		return new GridLayout(numberOfDetailLayouts, true);
	}

	@Override
	public Layout createDetailLayout(MMXRootObject root, EObject value) {
		return new GridLayout(2, false);
	}

	@Override
	public Object createEditorLayoutData(MMXRootObject root, EObject value,
			IInlineEditor editor, Control control) {
		return new GridData(SWT.FILL, SWT.CENTER, true, false);
	}

	@Override
	public Object createTopLayoutData(MMXRootObject root, EObject value,
			EObject detail) {
		return new GridData(GridData.FILL_BOTH);
	}

	@Override
	public Object createLabelLayoutData(MMXRootObject root, EObject value,
			IInlineEditor editor, Control control, final Label label) {
		if (editor instanceof ILabelLayoutDataProvidingEditor) {
			return ((ILabelLayoutDataProvidingEditor) editor).createLabelLayoutData(root, value, control, label);
		}
		else {
			return new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		}
	}
}
