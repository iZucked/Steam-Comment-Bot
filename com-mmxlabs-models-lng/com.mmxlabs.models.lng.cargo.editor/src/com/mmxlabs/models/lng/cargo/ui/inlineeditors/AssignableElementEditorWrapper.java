/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A {@link IInlineEditorEnablementWrapper} to hide or show the vessel assignment controls depending upon cargo/slot types
 * 
 * @author Simon Goodall
 */
public class AssignableElementEditorWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private Control control;

	public AssignableElementEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		// TODO: Cargo/Slot state does not change in editor, so no need to respond to notifications

		return false;
	}

	@Override
	protected boolean isEnabled() {
		return enabled;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {

		enabled = false;
		final EStructuralFeature feature = wrapped.getFeature();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			if (cargo.getCargoType() == CargoType.FLEET) {
				if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT || feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED) {
					enabled = true;
				}
			}
		} else if (object instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) object;
			if (loadSlot.isDESPurchase()) {
				if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
					enabled = true;
				}
			}
		} else if (object instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) object;
			if (dischargeSlot.isFOBSale()) {
				if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
					enabled = true;
				}
			}
		} else if (object instanceof VesselEvent) {
			if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
				enabled = true;
			}
		}

		if (enabled) {
			super.display(dialogContext, scenario, object, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
		} else {
			super.display(dialogContext, scenario, null, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
		}
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		control = super.createControl(parent, dbc, toolkit);
		return control;
	}
}