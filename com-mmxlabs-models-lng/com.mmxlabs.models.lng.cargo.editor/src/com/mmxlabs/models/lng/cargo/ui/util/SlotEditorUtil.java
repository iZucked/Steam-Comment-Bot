package com.mmxlabs.models.lng.cargo.ui.util;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;

@NonNullByDefault
public class SlotEditorUtil {

	private SlotEditorUtil() {
	}

	public static boolean disallowsExpressionOverride(final Contract contract) {
		final LNGPriceCalculatorParameters priceInfo = contract.getPriceInfo();
		if (priceInfo != null) {
			final EAnnotation eAnnotation = priceInfo.eClass().getEAnnotation("http://minimaxlabs.com/models/commercial/slot/expression");
			if (eAnnotation != null) {
				final String value = eAnnotation.getDetails().get("allowExpressionOverride");
				return "false".equalsIgnoreCase(value);
			}
		}
		return false;
	}

}
