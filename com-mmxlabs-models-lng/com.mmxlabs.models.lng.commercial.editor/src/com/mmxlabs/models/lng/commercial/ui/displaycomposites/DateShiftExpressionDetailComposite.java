/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * @author Simon Goodall
 * 
 */
public class DateShiftExpressionDetailComposite extends DefaultDetailComposite {

	public DateShiftExpressionDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);

	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(4, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case for min/max volumes - ensure text box has enough width for around 7 digits.
				// Note: Should really render the font to get width - this is ok on my system, but other systems (default font & size, resolution, dpi etc) could make this wrong
				final EStructuralFeature feature = editor.getFeature();
				if (feature == CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY //
						|| feature == CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE //
				) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
//					gd.widthHint = 80;
					//
					// // FIXME: Hack pending proper APi to manipulate labels
					if (feature == CommercialPackage.Literals.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Specific day?");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}

					return gd;
				}

				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 3;
				return gd;
			}
		};
	}
}
