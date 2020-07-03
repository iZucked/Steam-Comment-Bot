/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A Class to wrap {@link IInlineEditor}s which are part of a PaperDeal data structure. This handle the visibility of the control.
 * 
 * @author Farukh Mukhamedov
 * 
 */
public class PaperDealTypeExtensionWrapper extends IInlineEditorEnablementWrapper {
	public PaperDealTypeExtensionWrapper(IInlineEditor wrapped) {
		super(wrapped);
	}

	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	private Control control;

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == CargoPackage.eINSTANCE.getPaperDeal_PricingType()) {

			enabled = false;
			final PaperDeal paper = (PaperDeal) notification.getNotifier();
			final PaperPricingType ppt = (PaperPricingType) notification.getNewValue();
			if (ppt == PaperPricingType.PERIOD_AVG) {
				if (getFeature() == CargoPackage.eINSTANCE.getPaperDeal_StartDate() 
						|| getFeature() == CargoPackage.eINSTANCE.getPaperDeal_EndDate()) {
					enabled = true;
				}
			} else if (getFeature() == CargoPackage.eINSTANCE.getPaperDeal_PricingMonth()) {
				enabled = true;
			}
			if (enabled) {
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
				dialogContext.getDialogController().updateEditorVisibility();
				super.display(dialogContext, scenario, paper, range);
				getLabel().pack();
				dialogContext.getDialogController().relayout();
			} else {
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
				dialogContext.getDialogController().updateEditorVisibility();
			}
			return true;
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
		if (object instanceof PaperDeal) {
			final PaperDeal paper = (PaperDeal) object;
			if (paper.getPricingType() == PaperPricingType.PERIOD_AVG) {
				if (getFeature() == CargoPackage.eINSTANCE.getPaperDeal_StartDate() 
						|| getFeature() == CargoPackage.eINSTANCE.getPaperDeal_EndDate()) {
					enabled = true;
				}
			} else if (getFeature() == CargoPackage.eINSTANCE.getPaperDeal_PricingMonth()) {
				enabled = true;
			}
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), enabled);
			super.display(dialogContext, scenario, object, range);
		}
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		control = super.createControl(parent, dbc, toolkit);
		return control;
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value,
			Control control) {
		return null;
	}
}