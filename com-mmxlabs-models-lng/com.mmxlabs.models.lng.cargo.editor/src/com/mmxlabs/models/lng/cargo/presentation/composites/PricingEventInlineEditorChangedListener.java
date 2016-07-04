/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class PricingEventInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {

		if (editor.getFeature() == CargoPackage.Literals.SLOT__PRICING_EVENT && notification.getFeature() == CargoPackage.Literals.SLOT__PRICING_DATE) {
			if (notification.getNewValue() == null) {
				editor.setEditorEnabled(true);
			} else {
				editor.setEditorEnabled(false);
			}
		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;

		if (input instanceof Slot) {
			Slot slot = (Slot) input;
			if (editor.getFeature() == CargoPackage.Literals.SLOT__PRICING_EVENT) {
				editor.setEditorEnabled(!slot.isSetPricingDate());
			}
		}
	}
}