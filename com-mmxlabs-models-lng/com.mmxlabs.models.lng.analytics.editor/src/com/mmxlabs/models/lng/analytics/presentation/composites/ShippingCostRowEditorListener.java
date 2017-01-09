/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

public class ShippingCostRowEditorListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
		if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType()) {
			if (input instanceof ShippingCostRow) {
				final ShippingCostRow shippingCostRow = (ShippingCostRow) input;
				DestinationType type = shippingCostRow.getDestinationType();
				if (editor.getFeature() == AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue()) {
					editor.setEditorEnabled(type != DestinationType.END && type != DestinationType.DISCHARGE);
				}
				if (editor.getFeature() == AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice()) {
					editor.setEditorEnabled(type != DestinationType.END);
				}
			}
		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;

		if (input instanceof ShippingCostRow) {
			final ShippingCostRow shippingCostRow = (ShippingCostRow) input;
			DestinationType type = shippingCostRow.getDestinationType();
			if (editor.getFeature() == AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue()) {
				editor.setEditorEnabled(type != DestinationType.END && type != DestinationType.DISCHARGE);
			}
			if (editor.getFeature() == AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice()) {
				editor.setEditorEnabled(type != DestinationType.END);
			}
		}
	}

}