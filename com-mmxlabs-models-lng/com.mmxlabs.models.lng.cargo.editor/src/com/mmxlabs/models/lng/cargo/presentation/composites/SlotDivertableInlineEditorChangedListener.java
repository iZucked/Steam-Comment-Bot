/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

/**
 * Implementation of {@link IInlineEditorExternalNotificationListener} to disable editors depending upon FOB/DES status
 * 
 * @author Simon Goodall
 * 
 */
public class SlotDivertableInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
		if (notification.getFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase() || notification.getFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {

			if (input instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) input;
				if (loadSlot.isDESPurchase()) {
					editor.setEditorEnabled(true);
					editor.setEditorVisible(true);
				} else {
					editor.setEditorEnabled(false);
					editor.setEditorVisible(false);
				}
			} else if (input instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) input;
				if (dischargeSlot.isFOBSale()) {
					editor.setEditorEnabled(true);
					editor.setEditorVisible(true);
				} else {
					editor.setEditorEnabled(false);
					editor.setEditorVisible(false);
				}
			}
		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;
		if (input instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) input;
			if (loadSlot.isDESPurchase()) {
				editor.setEditorEnabled(true);
				editor.setEditorVisible(true);
			} else {
				editor.setEditorEnabled(false);
				editor.setEditorVisible(false);
			}
		} else if (input instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) input;
			if (dischargeSlot.isFOBSale()) {
				editor.setEditorEnabled(true);
				editor.setEditorVisible(true);
			} else {
				editor.setEditorEnabled(false);
				editor.setEditorVisible(false);
			}
		}
	}
}