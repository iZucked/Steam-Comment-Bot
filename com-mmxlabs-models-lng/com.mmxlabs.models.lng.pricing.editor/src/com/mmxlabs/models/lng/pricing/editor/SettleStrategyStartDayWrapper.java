/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
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
public class SettleStrategyStartDayWrapper extends IInlineEditorEnablementWrapper {
	public SettleStrategyStartDayWrapper(IInlineEditor wrapped) {
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
		if (notification.getFeature() == PricingPackage.eINSTANCE.getSettleStrategy_LastDayOfTheMonth()) {

			enabled = !notification.getNewBooleanValue();
			final SettleStrategy instrument = (SettleStrategy) notification.getNotifier();
			if (enabled) {
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
				dialogContext.getDialogController().updateEditorVisibility();
				super.display(dialogContext, scenario, instrument, range);
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
		if (object instanceof SettleStrategy && getFeature() == PricingPackage.eINSTANCE.getSettleStrategy_DayOfTheMonth()) {
			final SettleStrategy instrument = (SettleStrategy) object;
			enabled = !instrument.isLastDayOfTheMonth();
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