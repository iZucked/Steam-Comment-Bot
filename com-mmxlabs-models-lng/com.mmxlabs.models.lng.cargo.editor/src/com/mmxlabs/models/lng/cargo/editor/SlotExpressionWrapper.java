/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.util.SlotEditorUtil;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
public class SlotExpressionWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	public SlotExpressionWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == CargoPackage.eINSTANCE.getSlot_Contract()) {
			if (notification.getNotifier() instanceof Slot<?> slot) {
				enabled = true;
				if (notification.getNewValue() instanceof final @NonNull Contract contract) {
					if (SlotEditorUtil.disallowsExpressionOverride(contract)) {
						enabled = false;
						dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
						dialogContext.getDialogController().updateEditorVisibility();
						return true;
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
				} else if (notification.getOldValue() instanceof final @NonNull Contract contract && SlotEditorUtil.disallowsExpressionOverride(contract)) {
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
		if (object instanceof Slot<?> slot) {
			final Contract contract = slot.getContract();
			if (contract != null) {
				LNGPriceCalculatorParameters priceInfo = contract.getPriceInfo();
				if (priceInfo != null) {

					if (wrapped instanceof PriceExpressionWithFormulaeCurvesInlineEditor pbie //
							&& priceInfo instanceof final ExpressionPriceParameters epp) {
						if (!epp.getPreferredFormulae().isEmpty()) {
							pbie.addValues(epp.getPreferredFormulae(), true);
						} else if (scenario instanceof final LNGScenarioModel lngScenarioModel) {
							final PricingModel pm = ScenarioModelUtil.getPricingModel(lngScenarioModel);
							if (pm != null && !pm.getFormulaeCurves().isEmpty()) {
								pbie.addValues(pm.getFormulaeCurves(), true);
							}
						}
					}

					EAnnotation eAnnotation = priceInfo.eClass().getEAnnotation("http://minimaxlabs.com/models/commercial/slot/expression");
					if (eAnnotation != null) {
						String value = eAnnotation.getDetails().get("allowExpressionOverride");
						if ("false".equalsIgnoreCase(value)) {
							enabled = false;
							dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
							dialogContext.getDialogController().updateEditorVisibility();
							dialogContext.getDialogController().relayout();
							super.display(dialogContext, scenario, object, range);

							return;

						}
					}
				}
			} else if (scenario instanceof final LNGScenarioModel lngScenarioModel) {
				final PricingModel pm = ScenarioModelUtil.getPricingModel(lngScenarioModel);
				if (pm != null && !pm.getFormulaeCurves().isEmpty()) {
					if (wrapped instanceof PriceExpressionWithFormulaeCurvesInlineEditor pbie) {
						pbie.addValues(pm.getFormulaeCurves(), true);
					}
				}
			}

		}
		super.display(dialogContext, scenario, object, range);
	}
}