/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.ui.editor.NominatedValueInlineEditor;
import com.mmxlabs.models.lng.nominations.ui.editor.NomineeDateInlineEditor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;

public class NominationDetailComposite extends NominationSpecsDetailComposite implements IDisplayComposite {

	public NominationDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new NominationSpecsDetailComposite.NominationSpecDisplayCompositeLayoutProvider() {
			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				final Object data = super.createEditorLayoutData(root, value, editor, control);
				if (editor instanceof NomineeDateInlineEditor) {
					final Label label = editor.getLabel();
					if (label != null) {
						label.setText("Date");
						label.setToolTipText("Date when nominee event happens.");
						editor.setLabel(null);
					}	
				}
				if (editor instanceof NominatedValueInlineEditor) {
					final Label label = editor.getLabel();
					if (label != null) {
						if (value instanceof AbstractNomination) {
							AbstractNomination n = (AbstractNomination)value;
							if (n.getType() != null && n.getType().toLowerCase().contains("window")) {
								label.setToolTipText("Window nominated values should be in format DD/MM/YYYY +XX[d,h,m] where XX is window size, d = days, h = hours, m = months.");
							}
						}
					}
				}
				return data;
			}
		};
	}
}
