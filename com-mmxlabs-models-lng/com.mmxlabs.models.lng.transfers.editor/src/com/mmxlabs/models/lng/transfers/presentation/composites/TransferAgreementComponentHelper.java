/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.presentation.composites;

import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.editor.TransferAgreementExpressionWrapper;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class TransferAgreementComponentHelper extends DefaultComponentHelper {

	public TransferAgreementComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_AGREEMENT);
		
		ignoreFeatures.add(TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_FORMULAE);
		
		addEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION, topClass -> {
			return new TransferAgreementExpressionWrapper(new PriceExpressionWithFormulaeCurvesInlineEditor(TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION));
		});
	}
}
