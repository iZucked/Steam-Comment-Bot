/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A {@link IInlineEditorEnablementWrapper} to hide or show the vessel assignment controls depending upon volume mode
 * 
 * @author Simon Goodall
 */
public class VolumeModeEditorWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private Control control;
	private IDialogEditingContext dialogContext;
	private MMXRootObject scenario;
	private Collection<EObject> range;

	private Collection<EStructuralFeature> rangeFeatures = Sets.newHashSet( //
			AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_UNITS, //
			AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME, //
			AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME, //
			AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_UNITS, //
			AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME, //
			AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME //
	);

	public VolumeModeEditorWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		if (rangeFeatures.contains(wrapped.getFeature())) {
			if (notification.getFeature() == AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_MODE || notification.getFeature() == AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_MODE) {

				if (notification.getNewValue() == VolumeMode.NOT_SPECIFIED) {
					enabled = false;
				} else if (notification.getNewValue() == VolumeMode.FIXED) {
					// Just disable the min controls
					enabled = !(wrapped.getFeature() == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME || wrapped.getFeature() == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME);
				} else if (notification.getNewValue() == VolumeMode.RANGE) {
					enabled = true;
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
		VolumeMode mode = VolumeMode.NOT_SPECIFIED;
		if (object instanceof BuyOpportunity) {
			BuyOpportunity buyOpportunity = (BuyOpportunity) object;
			mode = buyOpportunity.getVolumeMode();
		} else if (object instanceof SellOpportunity) {

			SellOpportunity sellOpportunity = (SellOpportunity) object;
			mode = sellOpportunity.getVolumeMode();
		}

		if (mode == VolumeMode.NOT_SPECIFIED) {
			enabled = false;
		} else if (mode == VolumeMode.FIXED) {
			// Just disable the min controls
			enabled = !((feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME) || (feature == AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME));
		} else if (mode == VolumeMode.RANGE) {
			enabled = true;
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