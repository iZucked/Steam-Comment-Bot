package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class ExpressionPriceParametersComponentHelper extends DefaultComponentHelper {

	public ExpressionPriceParametersComponentHelper() {
		super(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS);
		ignoreFeatures.add(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PREFERRED_PBS);
		
		if(!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PRICING_BASES)) {
			ignoreFeatures.add(CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PRICING_BASIS);
		}
	}

}
