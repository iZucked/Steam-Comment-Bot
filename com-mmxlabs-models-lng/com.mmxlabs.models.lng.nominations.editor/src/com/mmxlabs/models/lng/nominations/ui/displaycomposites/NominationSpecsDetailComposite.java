/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.displaycomposites;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class NominationSpecsDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	class NominationSpecDisplayCompositeLayoutProvider extends DefaultDisplayCompositeLayoutProvider {
		@Override
		public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
			return new GridLayout(4, false);
		}

		@Override
		public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

			// Special case
			final EStructuralFeature feature = editor.getFeature();
			if (feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE || feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS) {
				final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				// 64 - magic constant from MultiDetailDialog
				// gd.widthHint = 80;
				gd.horizontalSpan = 1;
				// FIXME: Hack pending proper APi to manipulate labels
				if (feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS) {
					final Label label = editor.getLabel();
					if (label != null) {
					//	label.setText("Prior Units");
						label.setText("");
						label.setToolTipText("Days or months prior to.");
					}
					editor.setLabel(null);
				} 
//						else {
//						final Label label = editor.getLabel();
//						if (label != null) {
//							label.setText("/");
//						}
//						editor.setLabel(null);
//					}
				return gd;
			}
			if (feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE || feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS) {
				final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				// 64 - magic constant from MultiDetailDialog
				// gd.widthHint = 80;
				gd.horizontalSpan = 1;
				// FIXME: Hack pending proper APi to manipulate labels
				if (feature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS) {
					final Label label = editor.getLabel();
					if (label != null) {
					//	label.setText("Prior Units");
						label.setText("");
						label.setToolTipText("Days or months prior to.");
					}
					editor.setLabel(null);
				} 
//						else {
//						final Label label = editor.getLabel();
//						if (label != null) {
//							label.setText("/");
//						}
//						editor.setLabel(null);
//					}
				return gd;
			}

			final Object data = super.createEditorLayoutData(root, value, editor, control);
			if (data instanceof GridData) {
				((GridData) data).horizontalSpan = 3;
			}
			return data;
		}
	}

	public NominationSpecsDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new NominationSpecDisplayCompositeLayoutProvider();
	}
}
