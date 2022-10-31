/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class StartHeelOptionsDetailComposite extends DefaultDetailComposite {

	public StartHeelOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(7, false);
			}
			
			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				
				final var feature = editor.getFeature();
				if (feature == CommercialPackage.Literals.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE 
						|| feature == CommercialPackage.Literals.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					gd.minimumWidth = 64;
					gd.horizontalSpan = 3;
					return gd;
				}
				
				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 2;
				return gd;
			}
		};
	}

}