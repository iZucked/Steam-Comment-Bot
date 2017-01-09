/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
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
public class ReturnActualsInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
		// if (notification.getFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase() || notification.getFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
		//
		// if (input instanceof LoadSlot) {
		// final LoadSlot loadSlot = (LoadSlot) input;
		// if (loadSlot.isDESPurchase()) {
		// editor.setEditorEnabled(true);
		// editor.setEditorVisible(true);
		// } else {
		// editor.setEditorEnabled(false);
		// editor.setEditorVisible(false);
		// }
		// } else if (input instanceof DischargeSlot) {
		// final DischargeSlot dischargeSlot = (DischargeSlot) input;
		// if (dischargeSlot.isFOBSale()) {
		// editor.setEditorEnabled(true);
		// editor.setEditorVisible(true);
		// } else {
		// editor.setEditorEnabled(false);
		// editor.setEditorVisible(false);
		// }
		// }
		// }
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.editor = editor;
		this.input = object;
		if (input instanceof ReturnActuals) {
			boolean enabled = false;
			ReturnActuals returnActuals = (ReturnActuals) input;

			EObject container = returnActuals.eContainer();
			if (container instanceof CargoActuals) {
				CargoActuals cargoActuals = (CargoActuals) container;
				Cargo cargo = cargoActuals.getCargo();
				if (cargo != null && cargo.getCargoType() == CargoType.FLEET) {
					enabled = true;
				} else {
					EList<SlotActuals> actuals = cargoActuals.getActuals();
					for (SlotActuals slotActuals : actuals) {
						Slot slot = slotActuals.getSlot();
						if (slot instanceof LoadSlot) {
							LoadSlot loadSlot = (LoadSlot) slot;
							if (loadSlot.isDESPurchase() && loadSlot.isDivertible()) {
								enabled = true;
								break;
							}
						}
					}
				}

			}

			editor.setEditorEnabled(enabled);
		}
	}
}