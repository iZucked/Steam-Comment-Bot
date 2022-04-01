/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A {@link IInlineEditorEnablementWrapper} to hide or show the vessel assignment controls depending upon cargo/slot types
 * 
 * @author Simon Goodall
 */
public class NominatedVesselEditorWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private Control control;
	private IDialogEditingContext dialogContext;
	private MMXRootObject scenario;
	private Collection<EObject> range;

	public NominatedVesselEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (object instanceof SubContractProfile) {
			SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) object;

			if (notification.getFeature() == ADPPackage.eINSTANCE.getSubContractProfile_ContractType()) {
				if (notification.getNotifier() == input) {
					enabled = false;
					if ((notification.getNewValue() == ContractType.DES && subContractProfile.eContainer() instanceof PurchaseContractProfile)
							|| (notification.getNewValue() == ContractType.FOB && subContractProfile.eContainer() instanceof SalesContractProfile)) {
						enabled = true;
						dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
						dialogContext.getDialogController().updateEditorVisibility();
						super.display(dialogContext, scenario, input, range);
						getLabel().pack();
						return true;
					}
					dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
					dialogContext.getDialogController().updateEditorVisibility();
					return true;
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

		enabled = true; // FIXME: Notifications do not work, so always enable. We need to make the wizard more compliant with dialog API. Currently lots of "fake" implementations.
		final EStructuralFeature feature = wrapped.getFeature();
		if (object instanceof SubContractProfile<?, ?>) {
			SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) object;
			if (subContractProfile.getContractType() == ContractType.DES && subContractProfile.eContainer() instanceof PurchaseContractProfile) {
				enabled = true;
			}
			if (subContractProfile.getContractType() == ContractType.FOB && subContractProfile.eContainer() instanceof SalesContractProfile) {
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

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		return null;
	}

}