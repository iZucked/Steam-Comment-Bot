/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.presentation.composites;

import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.editor.TransferAgreementExpressionWrapper;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class TransferAgreementComponentHelper extends DefaultComponentHelper {

	public TransferAgreementComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_AGREEMENT);
		
		ignoreFeatures.add(TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_FORMULAE);
		
		addEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION, topClass -> {
			return new TransferAgreementExpressionWrapper(new PriceExpressionWithFormulaeCurvesInlineEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION));
		});
		
		addEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU, topClass -> {
			return new ReferenceInlineEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU) {
				@Override
				protected void doSetOverride(final Object value, final boolean forceCommandExecution) {
					if (currentlySettingValue) {
						return;
					}
					if (value == null && !valueList.isEmpty()) {
						doSetValue(valueList.get(0), forceCommandExecution);
					} else {
						doSetValue(value, forceCommandExecution);
					}
				}
			};
		});
		
		addEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_BU, topClass -> {
			return new ReferenceInlineEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_BU) {
				@Override
				protected void doSetOverride(final Object value, final boolean forceCommandExecution) {
					if (currentlySettingValue) {
						return;
					}
					if (value == null && !valueList.isEmpty()) {
						doSetValue(valueList.get(0), forceCommandExecution);
					} else {
						doSetValue(value, forceCommandExecution);
					}
				}
			};
		});
	}
}
