/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A {@link IInlineEditorEnablementWrapper} to hide or show the vessel assignment controls depending upon volume mode
 * 
 * @author Simon Goodall
 */
public class WindowModeEditorWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private Control control;
	private IDialogEditingContext dialogContext;
	private MMXRootObject scenario;
	private Collection<EObject> range;

	private Collection<EStructuralFeature> rangeFeatures = Sets.newHashSet( //
			AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW, //
			AnalyticsPackage.Literals.SELL_OPPORTUNITY__SPECIFY_WINDOW //
	);

	public WindowModeEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		if (rangeFeatures.contains(notification.getFeature())) {
			if (notification.getNewBooleanValue()) {
				enabled = true;
			} else {
				enabled = false;
			}
			EObject editorTarget = getEditorTarget();
			if (enabled) {
//				super.display(dialogContext, scenario, editorTarget, range);
				if (notification.getFeature() == AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW) {
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE, true);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS, true);
				} else {
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE, true);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE_UNITS, true);
				}
			} else {
//				super.display(dialogContext, scenario, editorTarget, range);
				if (notification.getFeature() == AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW) {
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE, false);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS, false);
				} else {
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE, false);
					dialogContext.getDialogController().setEditorVisibility(editorTarget, AnalyticsPackage.Literals.SELL_OPPORTUNITY__WINDOW_SIZE_UNITS, false);
				}
			}
			dialogContext.getDialogController().updateEditorVisibility();

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
		if (getFeature() == AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW || getFeature() == AnalyticsPackage.Literals.SELL_OPPORTUNITY__SPECIFY_WINDOW) {
			enabled = true;
		} else if (object instanceof BuyOpportunity) {
			BuyOpportunity buyOpportunity = (BuyOpportunity) object;
			enabled = buyOpportunity.isSpecifyWindow();
		} else if (object instanceof SellOpportunity) {
			SellOpportunity sellOpportunity = (SellOpportunity) object;
			enabled = sellOpportunity.isSpecifyWindow();
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