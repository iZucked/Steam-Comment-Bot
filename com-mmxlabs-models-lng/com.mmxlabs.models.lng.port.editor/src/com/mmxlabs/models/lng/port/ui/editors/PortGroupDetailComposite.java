/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class PortGroupDetailComposite extends DefaultDetailComposite {
	public PortGroupDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				if (editor.getFeature() == PortPackage.eINSTANCE.getPortGroup_Contents()) {
					return new GridData(SWT.FILL, SWT.FILL, true, true);
				}
				return super.createEditorLayoutData(root, value, editor, control);
			}

			@Override
			public Object createLabelLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control, final Label label) {
				if (editor.getFeature() == PortPackage.eINSTANCE.getPortGroup_Contents()) {
					return new GridData(SWT.RIGHT, SWT.TOP, false, false);
				}
				return super.createLabelLayoutData(root, value, editor, control, label);
			}

		};
	}
}
