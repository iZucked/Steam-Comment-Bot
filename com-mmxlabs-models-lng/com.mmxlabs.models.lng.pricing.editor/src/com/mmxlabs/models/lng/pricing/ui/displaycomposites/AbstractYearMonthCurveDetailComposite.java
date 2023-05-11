/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class AbstractYearMonthCurveDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	public AbstractYearMonthCurveDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eclass) {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(4, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				// Special case
				final var feature = editor.getFeature();
				if (feature == PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT || feature == PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;
					gd.horizontalSpan = 1;
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Units");
							label.setToolTipText("Curve units. E.g. $/mmBtu");
						}
						editor.setLabel(null);
					} else {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("/");
						}
						editor.setLabel(null);
					}
					return gd;
				}

				final Object data = super.createEditorLayoutData(root, value, editor, control);
				if (data instanceof GridData gd) {
					gd.horizontalSpan = 3;
				}
				return data;
			}
		};
	}
}
