/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class OpportunityDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	public OpportunityDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext dialogContext) {
		boolean changed = false;
		for (final IInlineEditor editor : editors) {
			final var feature = editor.getFeature();
			final boolean visible = dialogContext.getDialogController().getEditorVisibility(object, feature);
			final List<Control> controls = dialogContext.getEditorControls(object, feature);
			if (controls != null) {
				for (final Control control : controls) {
					if (control.isVisible() != visible) {
						changed = true;
					}

					// Always do state change as we can sometimes be inconsistent.
					setControlVisibility(control, visible);

					final Object layoutData = control.getLayoutData();
					if (layoutData instanceof GridData) {
						final GridData gridData = (GridData) layoutData;
						if (gridData.exclude != !visible) {
							changed = true;
						}
						if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME || feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME) {
							if ((object instanceof BuyOpportunity && object.eGet(AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_MODE) == VolumeMode.FIXED)
									|| (object instanceof SellOpportunity && object.eGet(AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_MODE) == VolumeMode.FIXED)) {
								if (!(control instanceof Label)) {
									gridData.horizontalSpan = 3;
								}
							} else {
								gridData.horizontalSpan = 1;
							}
						}

						// Always do state change as we can sometimes be inconsistent.
						gridData.exclude = !visible;
					}

				}
			}
		}
		if (changed) {
			layout(true);
		}

		return changed;
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(6, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				final var feature = editor.getFeature();
				if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME //
						|| feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME //
						|| feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_UNITS //
						|| feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME //
						|| feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME //
						|| feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_UNITS) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

					if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME || feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Volume");
						}
						editor.setLabel(null);
						if ((value instanceof BuyOpportunity && value.eGet(AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_MODE) == VolumeMode.FIXED)
								|| (value instanceof SellOpportunity && value.eGet(AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_MODE) == VolumeMode.FIXED)) {
							gd.horizontalSpan = 2;
						} else {
							gd.horizontalSpan = 1;

						}
					} else {
						gd.widthHint = 16;
						editor.setLabel(null);
					}
					if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_UNITS || feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_UNITS) {
						gd.grabExcessHorizontalSpace = true;
					}

					return gd;
				} else if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE || feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS

						|| feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE || feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE_UNITS) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

					if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE || feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Window");
						}
						editor.setLabel(null);
					} else {
						gd.widthHint = 16;
						editor.setLabel(null);
					}

					return gd;
				}

				// Anything else needs to fill the space.
				final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 5;
				return gd;
			}
		};
	}

}
