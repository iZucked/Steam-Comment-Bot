/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DerivedIndexValidExpressionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof NamedIndexContainer<?>) {
			final NamedIndexContainer<?> namedIndexContainer = (NamedIndexContainer<?>) target;
			final Index<?> data = namedIndexContainer.getData();
			if (data instanceof DerivedIndex<?>) {
				final DerivedIndex<?> c = (DerivedIndex<?>) data;
				if (c.getExpression() == null) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Index %s]Price index is missing a price expression.", namedIndexContainer.getName()));
					dcsd.addEObjectAndFeature(target, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA);
					failures.add(dcsd);
				} else {
					final PriceIndexType priceIndexType = PriceExpressionUtils.getPriceIndexType(namedIndexContainer);
					if (priceIndexType != null) {
						final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, target, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, c.getExpression(),
								priceIndexType);
						if (!result.isOk()) {
							final String message = String.format("[Index %s] %s", namedIndexContainer.getName(), result.getErrorDetails());
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dcsd.addEObjectAndFeature(target, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA);
							failures.add(dcsd);
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
