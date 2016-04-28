package com.mmxlabs.models.lng.pricing.ui.autocomplete;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.ui.editors.autocomplete.IContentProposalFactory;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;

public class PriceExpressionProposalFactory implements IContentProposalFactory {

	@Override
	public @Nullable IMMXContentProposalProvider create(@Nullable final EStructuralFeature feature) {
		if (feature != null) {

			final EAnnotation eAnnotation = feature.getEAnnotation(ExpressionAnnotationConstants.ANNOTATION_NAME);
			if (eAnnotation != null) {
				String value = eAnnotation.getDetails().get(ExpressionAnnotationConstants.ANNOTATION_KEY);
				if (ExpressionAnnotationConstants.TYPE_COMMODITY.equals(value)) {
					return new PriceExpressionProposalProvider(PriceIndexType.COMMODITY);
				} else if (ExpressionAnnotationConstants.TYPE_CHARTER.equals(value)) {
					return new PriceExpressionProposalProvider(PriceIndexType.CHARTER);
				} else if (ExpressionAnnotationConstants.TYPE_BASE_FUEL.equals(value)) {
					return new PriceExpressionProposalProvider(PriceIndexType.BUNKERS);
				}
			}
		}
		return null;
	}
}
