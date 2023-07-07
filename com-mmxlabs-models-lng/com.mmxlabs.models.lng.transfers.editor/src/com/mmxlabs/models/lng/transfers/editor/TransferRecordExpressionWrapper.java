/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A Class to wrap the slot price expression to see if it should be shown with
 * particular contracts
 * 
 * @author Simon Goodall
 * 
 */
public class TransferRecordExpressionWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	public TransferRecordExpressionWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == TransfersPackage.eINSTANCE.getTransferRecord_TransferAgreement()) {
			if (notification.getNotifier() instanceof TransferRecord) {
				enabled = true;
				if (notification.getNewValue() != null) {
					
					final TransferAgreement agreement = (TransferAgreement) notification.getNewValue();

					EAnnotation eAnnotation = agreement.eClass().getEAnnotation("http://minimaxlabs.com/models/commercial/slot/expression");
					if (eAnnotation != null) {
						String value = eAnnotation.getDetails().get("allowExpressionOverride");
						if ("false".equalsIgnoreCase(value)) {
							enabled = false;
							dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
							dialogContext.getDialogController().updateEditorVisibility();
							return true;
						}
					}

					dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
					dialogContext.getDialogController().updateEditorVisibility();
					super.display(dialogContext, scenario, input, range);
					Label label = getLabel();
					if (label != null) {
						label.pack();
					}
					dialogContext.getDialogController().relayout();
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

		enabled = true;
		if (object instanceof final TransferRecord transferRecord) {
			final TransferAgreement agreement = transferRecord.getTransferAgreement();
			if (agreement != null) {
				if (wrapped instanceof PriceExpressionWithFormulaeCurvesInlineEditor pbie) {
					if (!agreement.getPreferredFormulae().isEmpty()) {
						pbie.addValues(agreement.getPreferredFormulae(), true);
					} else if (scenario instanceof final LNGScenarioModel lngScenarioModel) {
						final PricingModel pm = ScenarioModelUtil.getPricingModel(lngScenarioModel);
						if (!pm.getFormulaeCurves().isEmpty()) {
							pbie.addValues(pm.getFormulaeCurves(), true);
						}
					}
				}
				
				EAnnotation eAnnotation = agreement.eClass().getEAnnotation("http://minimaxlabs.com/models/commercial/slot/expression");
				if (eAnnotation != null) {
					String value = eAnnotation.getDetails().get("allowExpressionOverride");
					if ("false".equalsIgnoreCase(value)) {
						enabled = false;
						dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
						dialogContext.getDialogController().updateEditorVisibility();
						return;
					}
				}
			} else if (scenario instanceof final LNGScenarioModel lngScenarioModel) {
				final PricingModel pm = ScenarioModelUtil.getPricingModel(lngScenarioModel);
				if (!pm.getFormulaeCurves().isEmpty()) {
					if (wrapped instanceof PriceExpressionWithFormulaeCurvesInlineEditor pbie) {
						pbie.addValues(pm.getFormulaeCurves(), true);
					}
				}
			}

		}
		super.display(dialogContext, scenario, object, range);
	}
}