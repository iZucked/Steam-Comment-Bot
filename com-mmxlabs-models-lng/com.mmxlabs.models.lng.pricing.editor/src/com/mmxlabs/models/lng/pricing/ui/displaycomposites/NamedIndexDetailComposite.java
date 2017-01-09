/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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

public class NamedIndexDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	private Composite contentComposite;

	public NamedIndexDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(4, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case
				final EStructuralFeature feature = editor.getFeature();
				if (feature == PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT || feature == PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;
					gd.horizontalSpan = 1;
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT) {
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
				if (data instanceof GridData) {
					((GridData) data).horizontalSpan = 3;
				}
				return data;
			}
		};
	}

	// @Override
	// public void createControls(IDialogEditingContext dialogContext, MMXRootObject root, EObject object, EMFDataBindingContext dbc) {
	// // TODO Auto-generated method stub
	// this.object = object;
	// toolkit.adapt(this);
	// setDefaultHelpContext(object);
	// // contentComposite = toolkit.createComposite(this);
	//
	// final GridLayout layout = new GridLayout(4, false);
	// layout.verticalSpacing = 8;
	// this.setLayout(layout);
	// for (final IInlineEditor editor : editors) {
	//
	// final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(this, SWT.NONE) : null;
	// editor.setLabel(label);
	// final Control control = editor.createControl(this, dbc, toolkit);
	// dialogContext.registerEditorControl(object, editor.getFeature(), control);
	//
	// control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
	// control.setData(LABEL_CONTROL_KEY, label);
	// control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	// if (label != null) {
	// toolkit.adapt(label, true, false);
	// label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	// label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
	// dialogContext.registerEditorControl(object, editor.getFeature(), label);
	// }
	// }
	// }

}
