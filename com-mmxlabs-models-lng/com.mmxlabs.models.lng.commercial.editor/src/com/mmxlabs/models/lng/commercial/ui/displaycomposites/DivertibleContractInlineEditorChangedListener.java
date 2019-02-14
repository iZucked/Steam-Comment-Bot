/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
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
public class DivertibleContractInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
		if (notification.getFeature() == CommercialPackage.eINSTANCE.getContract_ContractType()) {

			if (input instanceof PurchaseContract) {
				final PurchaseContract contract = (PurchaseContract) input;
				if (notification.getNewValue() != ContractType.FOB) {
					editor.setEditorEnabled(true);
				} else {
					editor.setEditorEnabled(false);
				}
			} else if (input instanceof SalesContract) {
				final SalesContract contract = (SalesContract) input;
				if (notification.getNewValue() != ContractType.DES) {
					editor.setEditorEnabled(true);
				} else {
					editor.setEditorEnabled(false);
				}
			} else {
				editor.setEditorEnabled(false);
			}
		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;
		if (input instanceof PurchaseContract) {
			final PurchaseContract contract = (PurchaseContract) input;
			if (contract.getContractType() != ContractType.FOB) {
				editor.setEditorEnabled(true);
			} else {
				editor.setEditorEnabled(false);
			}
		} else if (input instanceof SalesContract) {
			final SalesContract contract = (SalesContract) input;
			if (contract.getContractType() != ContractType.DES) {
				editor.setEditorEnabled(true);
			} else {
				editor.setEditorEnabled(false);
			}
		} else {
			editor.setEditorEnabled(false);
		}
	}
}