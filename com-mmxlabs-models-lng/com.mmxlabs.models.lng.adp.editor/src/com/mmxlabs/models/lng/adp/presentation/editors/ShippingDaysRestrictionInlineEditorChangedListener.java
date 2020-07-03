/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.commercial.ContractType;
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
public class ShippingDaysRestrictionInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
		if (notification.getFeature() == ADPPackage.eINSTANCE.getSubContractProfile_ContractType()) {

			if (input instanceof SubContractProfile) {
				SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) input;
				if (notification.getNewValue() != ContractType.FOB && subContractProfile.eContainer() instanceof PurchaseContractProfile) {
					editor.setEditorEnabled(true);
					// editor.setEditorVisible(true);
				} else if (notification.getNewValue() != ContractType.DES && subContractProfile.eContainer() instanceof SalesContractProfile) {
					editor.setEditorEnabled(true);
					// editor.setEditorVisible(true);
				} else {
					editor.setEditorEnabled(false);
					// editor.setEditorVisible(false);
				}
			}
		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;
		if (input instanceof SubContractProfile) {
			SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) input;
			if (subContractProfile.getContractType() == ContractType.DES && subContractProfile.eContainer() instanceof PurchaseContractProfile) {
				editor.setEditorEnabled(true);
				// editor.setEditorVisible(true);
			} else if (subContractProfile.getContractType() == ContractType.FOB && subContractProfile.eContainer() instanceof SalesContractProfile) {
				editor.setEditorEnabled(true);
				// editor.setEditorVisible(true);
			} else {
				editor.setEditorEnabled(false);
				// editor.setEditorVisible(false);
			}
		}
	}
}