package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.cargo.ui.displaycomposites.EndHeelLastPriceInlineEditor;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class EndHeelOptionsComponentHelper extends DefaultComponentHelper {

	public EndHeelOptionsComponentHelper() {
		super(CommercialPackage.Literals.END_HEEL_OPTIONS);
		
		ignoreFeatures.add(CommercialPackage.Literals.END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE);
		
		addEditor(CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION, topClass -> {
			return new EndHeelLastPriceInlineEditor(CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION);
		});
	}

}
