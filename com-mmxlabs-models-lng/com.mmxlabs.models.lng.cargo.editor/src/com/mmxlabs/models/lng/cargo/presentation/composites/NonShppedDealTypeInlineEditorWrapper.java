/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

/**
 * Implementation of {@link IInlineEditorExternalNotificationListener} to disable editors depending upon FOB/DES status
 * 
 * @author Simon Goodall
 * 
 */
public class NonShppedDealTypeInlineEditorWrapper extends IInlineEditorEnablementWrapper {
	private boolean wrapperEnabled = false;

	public NonShppedDealTypeInlineEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {
		return false;
	}

	@Override
	protected boolean isEnabled() {
		return wrapperEnabled;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {

		wrapperEnabled = false;
		if (!(object instanceof SpotSlot)) {
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (loadSlot.isDESPurchase()) {
					wrapperEnabled = true;
				}
			} else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				if (dischargeSlot.isFOBSale()) {
					wrapperEnabled = true;
				}
			}
		}

		if (wrapperEnabled) {
			super.display(dialogContext, scenario, object, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
		} else {
			super.display(dialogContext, scenario, null, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
		}
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		return null;
	}
}