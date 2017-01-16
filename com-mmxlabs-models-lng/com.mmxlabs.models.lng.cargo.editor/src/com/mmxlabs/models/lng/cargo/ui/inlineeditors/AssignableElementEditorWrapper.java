/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
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
	private IDialogEditingContext dialogContext;
	private MMXRootObject scenario;
	private Collection<EObject> range;

	public AssignableElementEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		if (wrapped.getFeature() == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX) {
			if (notification.getFeature() == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
				if (notification.getNewValue() instanceof CharterInMarket) {
					enabled = true;
				} else {
					enabled = false;
				}
				EObject editorTarget = getEditorTarget();
				if (enabled) {
					super.display(dialogContext, scenario, editorTarget, range);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, getFeature(), true);
					getLabel().pack();
				} else {
					super.display(dialogContext, scenario, editorTarget, range);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, getFeature(), false);
					getLabel().pack();

				}
			}
		}

		return false;
	}

	@Override
	protected boolean isEnabled() {
		return enabled;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		this.dialogContext = dialogContext;
		this.scenario = scenario;
		this.range = range;

		enabled = false;
		final EStructuralFeature feature = wrapped.getFeature();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			if (cargo.getCargoType() == CargoType.FLEET) {
				if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE || feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED) {
					enabled = true;
				} else if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX) {
					enabled = cargo.getVesselAssignmentType() instanceof CharterInMarket;
				}
			}
		} else if (object instanceof VesselEvent) {
			VesselEvent vesselEvent = (VesselEvent) object;
			if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
				enabled = true;
			} else if (feature == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX) {
				enabled = vesselEvent.getVesselAssignmentType() instanceof CharterInMarket;
			}
		}

		if (enabled) {
			super.display(dialogContext, scenario, object, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
		} else {
			super.display(dialogContext, scenario, object, range);
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
		}
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		control = super.createControl(parent, dbc, toolkit);
		return control;
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		// TODO Auto-generated method stub
		return null;
	}

}