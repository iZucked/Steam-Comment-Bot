/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;

/**
 * Implementation of {@link IInlineEditorExternalNotificationListener} to disable editors depending upon FOB/DES status
 * 
 * @since 2.0
 * @author Simon Goodall
 * 
 */
public class SlotInlineEditorChangedListener implements IInlineEditorExternalNotificationListener {

	private IInlineEditor editor;
	private EObject input;

	@Override
	public void notifyChanged(final Notification notification) {
//		if (notification.getFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
//			if (input instanceof LoadSlot) {
//				editor.setEditorEnabled(!((LoadSlot) input).isDESPurchase());
//			}
//
//		}
//		if (notification.getFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
//			if (input instanceof DischargeSlot) {
//				editor.setEditorEnabled(!((DischargeSlot) input).isFOBSale());
//			}
//		}
//		if (notification.getFeature() == CargoPackage.eINSTANCE.getSlot_Cargo()) {
//			if (input instanceof DischargeSlot) {
//				final DischargeSlot slot = (DischargeSlot) input;
//				editor.setEditorEnabled(!(slot.isFOBSale() && slot.getCargo() != null));
//			} else if (input instanceof LoadSlot) {
//				final LoadSlot slot = (LoadSlot) input;
//				editor.setEditorEnabled(!(slot.isDESPurchase() && slot.getCargo() != null));
//			}
//		}
	}

	@Override
	public void postDisplay(final IInlineEditor editor, final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
//		this.editor = editor;
//		this.input = object;
//		if (input instanceof LoadSlot) {
//			final LoadSlot slot = (LoadSlot) input;
//			editor.setEditorEnabled(!(slot.isDESPurchase() && slot.getCargo() != null));
//		} else if (input instanceof DischargeSlot) {
//			final DischargeSlot slot = (DischargeSlot) input;
//			editor.setEditorEnabled(!(slot.isFOBSale() && slot.getCargo() != null));
//		} else if (input instanceof Cargo) {
//			editor.setEditorEnabled(((Cargo) input).getCargoType() == CargoType.FLEET);
//		}
	}
}