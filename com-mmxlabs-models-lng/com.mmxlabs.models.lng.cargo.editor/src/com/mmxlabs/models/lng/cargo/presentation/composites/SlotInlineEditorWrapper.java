/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import org.eclipse.emf.common.notify.Notification;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * @generated NOT
 */
public class SlotInlineEditorWrapper extends IInlineEditorEnablementWrapper {

	public SlotInlineEditorWrapper(final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(Notification notification) {
		final Object feature = notification.getFeature();
		if (input instanceof LoadSlot && feature == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase())
			return true;
		if (input instanceof DischargeSlot && feature == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale())
			return true;
		return false;
	}

	@Override
	protected boolean isEnabled() {
		if (input instanceof LoadSlot) {
			return (!((LoadSlot) input).isDESPurchase());
		} else if (input instanceof DischargeSlot) {
			return (!((DischargeSlot) input).isFOBSale());
		}
		return true;
	}


}