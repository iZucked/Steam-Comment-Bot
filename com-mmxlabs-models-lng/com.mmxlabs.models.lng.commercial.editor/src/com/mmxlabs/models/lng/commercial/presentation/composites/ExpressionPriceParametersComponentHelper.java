/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class ExpressionPriceParametersComponentHelper extends DefaultComponentHelper {

	public ExpressionPriceParametersComponentHelper() {
		super(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS);
		ignoreFeatures.add(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PREFERRED_FORMULAE);
		
		addEditor(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION, topClass -> {
			return new EPPPriceWrapper(new PriceExpressionWithFormulaeCurvesInlineEditor(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION));
		});
	}

}
